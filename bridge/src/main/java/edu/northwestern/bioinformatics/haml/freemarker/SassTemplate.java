package edu.northwestern.bioinformatics.haml.freemarker;

import edu.northwestern.bioinformatics.haml.SassEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;
import java.io.Reader;

/**
 * @author Rhett Sutphin
 */
public class SassTemplate extends TemplateBase<SassEngine> {
    public SassTemplate(String name, Reader reader, Configuration configuration) throws IOException {
        super(name, reader, configuration);
    }

    @Override
    protected SassEngine createEngine(String filename, String text) {
        return new SassEngine(text).
            addOption("filename", filename);
    }

    @Override
    protected String getContentType() {
        return "text/css";
    }

    public static final TemplateCreator CREATOR = new TemplateCreator() {
        public Template createTemplate(String name, Reader r, Configuration configuration) throws IOException {
            return new SassTemplate(name, r, configuration);
        }
    };
}
