package edu.northwestern.bioinformatics.haml.freemarker;

import edu.northwestern.bioinformatics.haml.HamlEngine;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
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
public class HamlTemplate extends Template {
    private static final Log log = LogFactory.getLog(HamlTemplate.class);

    private HamlEngine engine;

    public HamlTemplate(String name, Reader reader, Configuration configuration) throws IOException {
        super(name, new StringReader(""), configuration);
        String text = IOUtils.toString(reader);
        engine = new HamlEngine(text);
        setCustomAttribute("content_type", "text/html");
    }

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
