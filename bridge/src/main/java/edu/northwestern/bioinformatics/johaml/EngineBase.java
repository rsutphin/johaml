package edu.northwestern.bioinformatics.johaml;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Rhett Sutphin
 */
public abstract class EngineBase<S extends EngineBase<S>> {
    protected final Log log = LogFactory.getLog(getClass());

    private Map<String, Object> options;
    private List<String> requires;
    private String rendered;
    private String template;

    static {
        BSFManager.registerScriptingEngine("ruby", "org.jruby.javasupport.bsf.JRubyEngine", new String[] { "rb" });
    }

    protected EngineBase(String template) {
        this.template = template;
        options = new LinkedHashMap<String, Object>();
        requires = new LinkedList<String>();
    }

    ////// CHAINABLE CONFIGURATION

    @SuppressWarnings({ "unchecked" })
    public S addOption(String name, Object value) {
        getOptions().put(name, value);
        return (S) this;
    }

    /**
     * Adds the names of ruby scripts which will be <code>require</code>d before
     * the template is executed.  The values should either be absolute file paths
     * or resources which are resolvable on the classpath. Chainable.
     *
     * @param name
     * @param value
     * @return this object (for chaining)
     */
    @SuppressWarnings({ "unchecked" })
    public S addRequire(String requireName) {
        getRequires().add(requireName);
        return (S) this;
    }

    ////// BRIDGING LOGIC

    public String render() {
        try {
            BSFManager bsf = new BSFManager();
            bsf.declareBean("bridge", this, this.getClass());
            bsf.exec("ruby", "(java)", 0, 0, getBridgeScript());
            return this.rendered;
        } catch (BSFException e) {
            if (e.getTargetException() != null) {
                log.error("There was a BSF exception invoking jruby which is about to be rethrown.  The BSFException's cause was the attached " + e.getTargetException().getClass().getName() + '.', e.getTargetException());
                StringWriter rubyStack = new StringWriter();
                e.getTargetException().printStackTrace(new PrintWriter(rubyStack));
                throw new RuntimeException(rubyStack.toString(), e);
            } else {
                throw new RuntimeException("Error invoking JRuby", e);
            }
        }
    }

    protected abstract String getBridgeScript();

    /**
     * Provides a mechanism for logging from the ruby bridge code into the java logger.
     * There's probably a better way to do this, but this is easy.
     */
    public void log(String message) {
        log.info(message);
    }

    ////// SIMPLE PROPERTY ACCESSORS

    public Map<String, Object> getOptions() {
        return options;
    }

    public List<String> getRequires() {
        return requires;
    }

    /**
     * Provides the access point into which the bridge script stores the rendered result.
     * Conceptually not public -- end user code should not need to set this ever.
     * @param content
     */
    public void setRenderedContent(String content) {
        this.rendered = content;
    }

    public String getTemplate() {
        return template;
    }
}
