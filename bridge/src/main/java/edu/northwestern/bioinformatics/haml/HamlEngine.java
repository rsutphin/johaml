package edu.northwestern.bioinformatics.haml;

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
 * This class encapsulates reading Haml input and outputting a rendered HTML stream (or String).
 * It is primarily a Java adapter class for Haml::Engine, providing a consistent mechanism
 * for accessing the copy of Haml that is embedded in this library.
 * <p>
 *
 * @see http://haml.hamptoncatlin.com/docs
 * @author Rhett Sutphin
 */
public class HamlEngine {
    private static final Log log = LogFactory.getLog(HamlEngine.class);

    private String haml;
    private String html;
    private Map<String, Object> options;
    private Map<String, Object> locals;
    private List<String> requires;
    private Object evaluationContext;
    private String evaluationContextExpression;

    static {
        BSFManager.registerScriptingEngine("ruby", "org.jruby.javasupport.bsf.JRubyEngine", new String[] { "rb" });
    }

    public HamlEngine(String haml) {
        this.haml = haml;
        options = new LinkedHashMap<String, Object>();
        locals = new LinkedHashMap<String, Object>();
        requires = new LinkedList<String>();
    }

    ////// CHAINABLE CONFIGURATION

    /**
     * Adds options which will be passed to <code>Haml::Engine</code>.  Chainable.
     *
     * @see http://haml.hamptoncatlin.com/docs/rdoc/classes/Haml/Engine.html
     * @param name
     * @param value
     * @return this object (for chaining)
     */
    public HamlEngine addOption(String name, Object value) {
        getOptions().put(name, value);
        return this;
    }

    /**
     * Adds locals which will be available when the template executes.  Chainable.
     *
     * @see http://haml.hamptoncatlin.com/docs/rdoc/classes/Haml/Engine.html
     * @param name
     * @param value
     * @return this object (for chaining)
     */
    public HamlEngine addLocal(String name, Object value) {
        getLocals().put(name, value);
        return this;
    }

    /**
     * Adds the names of ruby scripts which will be <code>require</code>d before
     * the template is executed.  The values should either be absolute file paths
     * resources which are resolvable on the classpath. Chainable.
     *
     * @param name
     * @param value
     * @return this object (for chaining)
     */
    public HamlEngine addRequire(String requireName) {
        getRequires().add(requireName);
        return this;
    }

    /**
     * Provide an object to be used as the evaluation context when the template is evaluated.
     * methods on this object will be available to be executed by the template.
     * <p>
     * If both this and the {@link #getEvaluationContextExpression()} are null, <code>Object.new</code>
     * will be used.
     *
     * @param context
     * @return this object (for chaining)
     */
    public HamlEngine setEvaluationContext(Object context) {
        this.evaluationContext = context;
        return this;
    }

    /**
     * Provide a string of ruby code which will create an object to use as an evaluation context.
     * You will probably want to use this in conjunction with {@link #addRequire} in order to
     * avoid making this expression too complex.
     * <p>
     * If this expression is set, the value set into {@link #setEvaluationContext} will not be
     * used directly by this class.  However, its value can be accessed in this ruby expression as
     * <code>$bridge.evaluationContext</code>.  This can be handy if you want to pass complex
     * information into the evaluation context expression without serializing it.
     *
     * @param expression
     * @return this object (for chaining)
     */
    public HamlEngine setEvaluationContextExpression(String expression) {
        this.evaluationContextExpression = expression;
        return this;
    }

    ////// WRITERS

    public String render() {
        try {
            BSFManager bsf = new BSFManager();
            bsf.declareBean("bridge", this, this.getClass());
            bsf.exec("ruby", "(java)", 0, 0, "require 'haml_bridge'");
            return this.html;
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

    public String toHtml() {
        return render();
    }

    /**
     * Provides a mechanism for logging from haml_bridge.rb into the java logger.  There's probably
     * a better way to do this, but this is easy.
     */
    public void log(String message) {
        log.info(message);
    }

    ////// SIMPLE ACCESSORS

    public String getHaml() {
        return haml;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public Map<String, Object> getLocals() {
        return locals;
    }

    public List<String> getRequires() {
        return requires;
    }

    /**
     * @see #setEvaluationContext
     */
    public Object getEvaluationContext() {
        return evaluationContext;
    }

    /**
     * @see #setEvaluationContextExpression
     */
    public String getEvaluationContextExpression() {
        return evaluationContextExpression;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
