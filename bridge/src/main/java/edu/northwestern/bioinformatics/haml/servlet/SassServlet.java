package edu.northwestern.bioinformatics.haml.servlet;

import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.Configuration;
import edu.northwestern.bioinformatics.haml.freemarker.HamlFreeMarkerConfiguration;
import edu.northwestern.bioinformatics.haml.freemarker.HamlTemplate;

/**
 * @author Rhett Sutphin
 */
public class SassServlet extends FreemarkerServlet {
    @Override
    protected Configuration createConfiguration() {
        return new HamlFreeMarkerConfiguration(HamlTemplate.CREATOR);
    }
}
