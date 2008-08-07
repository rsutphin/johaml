package edu.northwestern.bioinformatics.haml.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.Reader;
import java.io.IOException;

/**
 * @author Rhett Sutphin
 */
public interface TemplateCreator {
    Template createTemplate(String name, Reader r, Configuration configuration) throws IOException;
}
