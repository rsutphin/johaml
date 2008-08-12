package freemarker.core;

import edu.northwestern.bioinformatics.johaml.EngineBase;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * Adapter element for allowing {@link edu.northwestern.bioinformatics.johaml.freemarker.TemplateBase}
 * subclasses to execute in the context of a Freemarker {@link Environment}.
 * <p>
 * Ideally this would be in <code>edu.northwestern.bioinformatics.haml.freemarker</code>; however,
 * {@link TemplateElement} has a package-private abstract method, so it needs to be in this package
 * to work.
 *
 * @author Rhett Sutphin
 */
public class HamlAdapterPseudoTemplateElement<E extends EngineBase<E>> extends TemplateElement {
    private E engine;

    public HamlAdapterPseudoTemplateElement(E engine) {
        this.engine = engine;
    }

    @Override
    void accept(Environment environment) throws TemplateException, IOException {
        String rendered = engine.render();
        environment.getOut().write(rendered);
    }

    @Override
    public String getDescription() {
        return "An adapter element to hook into Freemarker's template processing environment";
    }

    @Override
    public String getCanonicalForm() {
        return String.format("[%s adapter]", engine.getClass().getSimpleName());
    }
}
