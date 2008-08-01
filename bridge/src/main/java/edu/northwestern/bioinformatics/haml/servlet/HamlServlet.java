package edu.northwestern.bioinformatics.haml.servlet;

import edu.northwestern.bioinformatics.haml.freemarker.HamlFreeMarkerConfiguration;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.Configuration;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.servlet.ServletException;

/**
 * @author Rhett Sutphin
 */
public class HamlServlet extends FreemarkerServlet {
    @Override
    protected Configuration createConfiguration() {
        return new HamlFreeMarkerConfiguration();
    }
}
