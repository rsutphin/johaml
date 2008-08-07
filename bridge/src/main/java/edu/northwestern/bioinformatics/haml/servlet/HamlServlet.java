package edu.northwestern.bioinformatics.haml.servlet;

import edu.northwestern.bioinformatics.haml.freemarker.HamlFreeMarkerConfiguration;
import edu.northwestern.bioinformatics.haml.freemarker.HamlTemplate;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.Configuration;

/**
 * @author Rhett Sutphin
 */
public class HamlServlet extends FreemarkerServlet {
    @Override
    protected Configuration createConfiguration() {
        return new HamlFreeMarkerConfiguration(HamlTemplate.CREATOR);
    }
}
