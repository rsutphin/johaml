package edu.northwestern.bioinformatics.johaml.freemarker;

import edu.northwestern.bioinformatics.johaml.EngineBase;
import freemarker.core.Environment;
import freemarker.core.TemplateElement;
import freemarker.core.HamlAdapterPseudoTemplateElement;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateNodeModel;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

/**
 * @author Rhett Sutphin
 */
public abstract class TemplateBase<E extends EngineBase<E>> extends Template {
    protected final Log log = LogFactory.getLog(getClass());
    protected E engine;
    protected TemplateElement rootTreeNode;

    public TemplateBase(String name, Reader reader, Configuration configuration) throws IOException {
        super(name, new StringReader(""), configuration);
        String text = IOUtils.toString(reader);
        engine = createEngine(name, text);
        setCustomAttribute("content_type", getContentType());
        this.rootTreeNode = createRootTreeNode();
    }

    protected abstract E createEngine(String filename, String text);

    /**
     * The content type which the processed template should present itself as.  This should be
     * a MIME type (e.g., <code>"text/html"</code>).
     * @return
     */
    protected abstract String getContentType();

    /**
     * @see freemarker.core.HamlAdapterPseudoTemplateElement
     */
    protected TemplateElement createRootTreeNode() {
        return new HamlAdapterPseudoTemplateElement<E>(engine);
    }

    @Override
    public TemplateElement getRootTreeNode() {
        return rootTreeNode;
    }

    @Override
    public void process(Object rootNode, Writer out) throws TemplateException, IOException {
        // TODO: maybe should let the superclass handle this
        Environment env = new Environment(this, (TemplateHashModel) rootNode, out);
        env.process();
    }

    @Override
    public void process(Object o, Writer writer, ObjectWrapper objectWrapper) throws TemplateException, IOException {
        log.warn("objectWrapper is ignored for this implementation");
        this.process(o, writer);
    }

    @Override
    public void process(Object o, Writer writer, ObjectWrapper objectWrapper, TemplateNodeModel templateNodeModel) throws TemplateException, IOException {
        log.warn("objectWrapper & templateNodeModel are ignored for this implementation");
        this.process(o, writer);
    }
}
