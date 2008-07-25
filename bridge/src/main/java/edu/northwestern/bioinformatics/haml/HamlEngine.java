package edu.northwestern.bioinformatics.haml;

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * This class encapsulates reading Haml input and outputting a rendered HTML stream (or String).
 * It is primarily a Java adapter class for Haml::Engine, providing a consistent mechanism
 * for accessing the copy of Haml that is embedded in this library.
 *
 * @author Rhett Sutphin
 */
public class HamlEngine {
    private Reader reader;
    private Map<String, Object> options;
    private Map<String, Object> locals;

    public HamlEngine(String s) {
        this(new StringReader(s));
    }

    public HamlEngine(Reader reader) {
        this.reader = reader;
        options = new LinkedHashMap<String, Object>();
        locals = new LinkedHashMap<String, Object>();
    }

    ////// CHAINABLE CONFIGURATION

    public HamlEngine addOption(String name, Object value) {
        getOptions().put(name, value);
        return this;
    }

    public HamlEngine addLocal(String name, Object value) {
        getLocals().put(name, value);
        return this;
    }

    ////// WRITERS

    public String toHtml() {
        throw new UnsupportedOperationException("TODO");
    }

    ////// SIMPLE ACCESSORS

    public Map<String, Object> getOptions() {
        return options;
    }

    public Map<String, Object> getLocals() {
        return locals;
    }
}
