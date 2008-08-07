package edu.northwestern.bioinformatics.haml.freemarker;

import edu.northwestern.bioinformatics.haml.EngineBase;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNodeModel;
import freemarker.template.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Writer;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author Rhett Sutphin
 */
public abstract class TemplateBase<E extends EngineBase> extends Template {
    protected final Log log = LogFactory.getLog(getClass());
    protected E engine;

    public TemplateBase(String name, Reader reader, Configuration configuration) throws IOException {
        super(name, new StringReader(""), configuration);
        String text = IOUtils.toString(reader);
        engine = createEngine(name, text);
        setCustomAttribute("content_type", getContentType());
    }

    protected abstract E createEngine(String filename, String text);

    /**
     * The content type which the processed template should present itself as.  This should be
     * a MIME type (e.g., <code>"text/html"</code>).
     * @return
     */
    protected abstract String getContentType();

    @Override
    public void process(Object rootNode, Writer writer) throws TemplateException, IOException {
        String out = engine.render();
        writer.write(out);
        writer.flush();
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
