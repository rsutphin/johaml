package edu.northwestern.bioinformatics.johaml.servlet;

import edu.northwestern.bioinformatics.johaml.freemarker.HamlFreeMarkerConfiguration;
import edu.northwestern.bioinformatics.johaml.freemarker.SassTemplate;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.Configuration;

/**
 * @author Rhett Sutphin
 */
public class SassServlet extends FreemarkerServlet {
    @Override
    protected Configuration createConfiguration() {
        return new HamlFreeMarkerConfiguration(SassTemplate.CREATOR);
    }
}
