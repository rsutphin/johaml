package edu.northwestern.bioinformatics.haml.servlet;

import edu.northwestern.bioinformatics.haml.freemarker.HamlFreeMarkerConfiguration;
import edu.northwestern.bioinformatics.haml.freemarker.HamlTemplate;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.Configuration;

/**
 * Provides a simple renderer for Haml documents in a servlet container.
 *
 * @author Rhett Sutphin
 */
public class HamlServlet extends FreemarkerServlet {
    @Override
    protected Configuration createConfiguration() {
        return new HamlFreeMarkerConfiguration(HamlTemplate.CREATOR);
    }
}
