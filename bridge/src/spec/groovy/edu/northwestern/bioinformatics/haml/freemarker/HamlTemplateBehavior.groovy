import edu.northwestern.bioinformatics.haml.freemarker.HamlFreeMarkerConfiguration
import edu.northwestern.bioinformatics.haml.freemarker.HamlTemplate
import freemarker.template.SimpleHash

before "create config", {
  configuration = new HamlFreeMarkerConfiguration()
}

def createTemplate(haml) {
  r = new StringReader(haml)
  return new HamlTemplate("test template", r, configuration)
}

String rendered(haml, rootNode) {
  w = new StringWriter();
  createTemplate(haml).process(rootNode, w)
  return w.toString()
}

it "renders the template", {
  rendered("%p foo", null).shouldBe "<p>foo</p>\n"
}

it "exposes root node keys as local method calls", {
  root = new SimpleHash()
  root.put("answer", 42)
  rendered(".answer= answer", root).shouldBe "<div class='answer'>42</div>\n"
}

it "allows bracket indexing on Freemarker hashes", {
  root = new SimpleHash()
  node = new SimpleHash()
  root.put("structure", node)
  node.put("fine", 137)
  rendered("%i= structure['fine']", root).shouldBe "<i>137</i>\n"
}

it "exposes 'Request' root node key as 'request'", {
  root = new SimpleHash()
  root.put("Request", [ foo: "bar" ])
  rendered("%b= request['foo']", root).shouldBe "<b>bar</b>\n"
}

it "exposes 'RequestParameters' root node key as 'params'", {
  root = new SimpleHash()
  root.put("RequestParameters", [ foo: "bar" ])
  rendered("%b= params['foo']", root).shouldBe "<b>bar</b>\n"
}

it "exposes 'Session' root node key as 'session'", {
  root = new SimpleHash()
  root.put("Session", [ foo: "bar" ])
  rendered("%b= session['foo']", root).shouldBe "<b>bar</b>\n"
}

it "exposes 'Application' root node key as 'application'", {
  root = new SimpleHash()
  root.put("Application", [ foo: "bar" ])
  rendered("%b= application['foo']", root).shouldBe "<b>bar</b>\n"
}

it "gives a reasonable error message when attempting to resolve an unknown key", {
  try {
    rendered("%em= bogon", new SimpleHash())
  } catch (RuntimeException re) {
    re.getMessage().shouldHave "No value in page model named 'bogon'"
  }
}