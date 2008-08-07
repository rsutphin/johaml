package edu.northwestern.bioinformatics.haml;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
public class HamlEngine extends EngineBase<HamlEngine> {

    private Map<String, Object> locals;
    private Object evaluationContext;
    private String evaluationContextExpression;

    public HamlEngine(String haml) {
        super(haml);
        locals = new LinkedHashMap<String, Object>();
    }

    ////// CHAINABLE CONFIGURATION

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
     * Adds options which will be passed to <code>Haml::Engine</code>.  Chainable.
     *
     * @see http://haml.hamptoncatlin.com/docs/rdoc/classes/Haml.html
     * @param name
     * @param value
     * @return this object (for chaining)
     */
    @Override // for docs only
    public HamlEngine addOption(String name, Object value) {
        return super.addOption(name, value);
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

    @Override
    protected String getBridgeScript() {
        return "require 'haml_bridge'";
    }

    public String toHtml() {
        return render();
    }

    ////// SIMPLE ACCESSORS

    public Map<String, Object> getLocals() {
        return locals;
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

}
