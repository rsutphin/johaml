package edu.northwestern.bioinformatics.haml.freemarker;

import freemarker.template.Template;

import java.util.Locale;
import java.io.IOException;
import java.io.Reader;
import java.io.FileNotFoundException;

/**
 * Replacement for FreeMarker's Configuration class for using FreeMarker's
 * servlet bridge code.
 * <p>
 * Many (most) of the properties you can configure for FreeMarker are not
 * relevant to Haml and so have no effect.
 *
 * @author Rhett Sutphin
 */
public class HamlFreeMarkerConfiguration extends freemarker.template.Configuration {
    private TemplateCreator creator;

    public HamlFreeMarkerConfiguration(TemplateCreator creator) {
        this.creator = creator;
    }

    //// Bypass the cache because it 1) directly invokes "new Template()" and 2) can't be
    //// replaced because it is a private field that is directly accessed internally w/ no setter.
    //// And 3) the way HamlEngine and SassEngine are currently implemented, they aren't reusable
    //// so a cache would be useless.

    @Override
    // Note that this library ignores locale, encoding, and parse
    public Template getTemplate(String name, Locale locale, String encoding, boolean b) throws IOException {
        Object source = getTemplateLoader().findTemplateSource(name);
        if (source == null) throw new FileNotFoundException("Could not locate " + name);
        Reader r = getTemplateLoader().getReader(source, encoding);
        if (r == null) throw new FileNotFoundException("Could not read " + name);
        return creator.createTemplate(name, r, this);
    }
}
