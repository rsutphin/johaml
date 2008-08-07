package edu.northwestern.bioinformatics.haml;

/**
 * @author Rhett Sutphin
 */
public class SassEngine extends EngineBase<SassEngine> {
    public SassEngine(String sass) {
        super(sass);
    }

    @Override
    protected String getBridgeScript() {
        return "require 'sass_bridge'";
    }
}
