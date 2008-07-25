package edu.northwestern.bioinformatics.haml;

import org.apache.bsf.BSFManager;
import org.apache.bsf.BSFException;

import java.util.Map;
import java.util.LinkedHashMap;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.IOException;

/**
 * This class encapsulates reading Haml input and outputting a rendered HTML stream (or String).
 * It is primarily a Java adapter class for Haml::Engine, providing a consistent mechanism
 * for accessing the copy of Haml that is embedded in this library.
 *
 * @author Rhett Sutphin
 */
public class HamlEngine {
    private String haml;
    private String html;
    private Map<String, Object> options;
    private Map<String, Object> locals;

    public HamlEngine(String haml) {
        this.haml = haml;
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
        BSFManager.registerScriptingEngine("ruby", "org.jruby.javasupport.bsf.JRubyEngine", new String[] { "rb" });
        BSFManager bsf = new BSFManager();
        try {
            bsf.declareBean("bridge", this, this.getClass());
            bsf.exec("ruby", "(java)", 0, 0, loadScript("haml_bridge.rb"));
            return this.html;
        } catch (BSFException e) {
            throw new RuntimeException("Error invoking JRuby", e);
        } catch (IOException e) {
            // TODO: Make specific
            throw new RuntimeException(e);
        }
    }

    private String loadScript(String name) throws IOException {
        Reader reader = new FileReader("/Users/rsutphin/java/exp/haml-in-java/bridge/src/main/ruby/" + name);
        char[] buf = new char[8192];
        int size = reader.read(buf);
        return new String(buf, 0, size);
    }

    ////// SIMPLE ACCESSORS

    public String getHaml() {
        return haml;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public Map<String, Object> getLocals() {
        return locals;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
