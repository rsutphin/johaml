package edu.northwestern.bioinformatics.johaml.freemarker;

import org.jruby.RubyObject;
import org.jruby.RubyString;

import java.io.IOException;
import java.io.Writer;

/**
 * Bridge class which adapts an Haml::Buffer into a {@link java.io.Writer}.
 * <p>
 * Can't seem to implement abstract classes in JRuby yet, so this
 * has to be implemented in java.
 *
 * @author Rhett Sutphin
 */
public class HamlBufferWriter extends Writer {
    private RubyObject hamlBuffer;

    public HamlBufferWriter(RubyObject hamlBuffer) {
        this.hamlBuffer = hamlBuffer;
    }

    public void write(char cbuf[], int off, int len) throws IOException {
        String s = new String(cbuf, off, len);
        hamlBuffer.callMethod(hamlBuffer.getRuntime().getCurrentContext(),
            "push_text", RubyString.newString(hamlBuffer.getRuntime(), s));
    }

    public void flush() throws IOException { }

    public void close() throws IOException { }
}
