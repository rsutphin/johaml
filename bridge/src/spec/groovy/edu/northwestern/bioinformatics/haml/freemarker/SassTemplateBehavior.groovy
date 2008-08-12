import edu.northwestern.bioinformatics.johaml.freemarker.HamlFreeMarkerConfiguration
import edu.northwestern.bioinformatics.johaml.freemarker.SassTemplate

before "create config", {
  configuration = new HamlFreeMarkerConfiguration(SassTemplate.CREATOR)
}

def createTemplate(sass) {
  r = new StringReader(sass)
  return new SassTemplate("test template", r, configuration)
}

String rendered(sass, rootNode) {
  w = new StringWriter();
  createTemplate(sass).process(rootNode, w)
  return w.toString()
}

it "renders the template", {
  rendered("p\n  :white-space nowrap", null).shouldBe "p {\n  white-space: nowrap; }\n"
}

