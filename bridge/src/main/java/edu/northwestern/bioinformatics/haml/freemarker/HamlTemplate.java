package edu.northwestern.bioinformatics.haml.freemarker;

import edu.northwestern.bioinformatics.haml.HamlEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * @author Rhett Sutphin
 */
public class HamlTemplate extends TemplateBase<HamlEngine> {

    public HamlTemplate(String name, Reader reader, Configuration configuration) throws IOException {
        super(name, reader, configuration);
    }

    @Override
    protected HamlEngine createEngine(String filename, String text) {
        return new HamlEngine(text).
            addRequire("freemarker/template_model_context").
            addOption("filename", filename);
    }

    @Override
    protected String getContentType() {
        return "text/html";
    }

    @Override
    public void process(Object rootNode, Writer writer) throws TemplateException, IOException {
        if (rootNode instanceof TemplateHashModel) {
            TemplateHashModel model = (TemplateHashModel) rootNode;
            engine.setEvaluationContextExpression("TemplateModelContext.new($bridge.evaluationContext)")
                .setEvaluationContext(model);
        }
        super.process(rootNode, writer);
    }

    public static final TemplateCreator CREATOR = new TemplateCreator() {
        public Template createTemplate(String name, Reader r, Configuration configuration) throws IOException {
            return new HamlTemplate(name, r, configuration);
        }
    };
}
