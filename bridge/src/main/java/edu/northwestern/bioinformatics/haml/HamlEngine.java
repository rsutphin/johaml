package edu.northwestern.bioinformatics.haml;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

import java.util.LinkedHashMap;
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
    private String haml;
    private String html;
    private Map<String, Object> options;
    private Map<String, Object> locals;
    private Object evaluationContext;

    static {
        BSFManager.registerScriptingEngine("ruby", "org.jruby.javasupport.bsf.JRubyEngine", new String[] { "rb" });
    }

    public HamlEngine(String haml) {
        this.haml = haml;
        options = new LinkedHashMap<String, Object>();
        locals = new LinkedHashMap<String, Object>();
    }

    ////// CHAINABLE CONFIGURATION

    public HamlEngine addOption(String name, Object value) {
        getOptions().put(name, value);
        return this;
    }

    public HamlEngine addLocal(String name, Object value) {
        getLocals().put(name, value);
        return this;
    }

    public HamlEngine setEvaluationContext(Object context) {
        this.evaluationContext = context;
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
            throw new RuntimeException("Error invoking JRuby", e);
        }
    }

    public String toHtml() {
        return render();
    }

    ////// SIMPLE ACCESSORS

    public String getHaml() {
        return haml;
    }

    public Object getEvaluationContext() {
        return evaluationContext;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public Map<String, Object> getLocals() {
        return locals;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
