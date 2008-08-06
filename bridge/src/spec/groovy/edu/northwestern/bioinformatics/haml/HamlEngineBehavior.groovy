import edu.northwestern.bioinformatics.haml.HamlEngine

it "converts to an HTML string", {
  new HamlEngine("%p Hello").toHtml().shouldBe "<p>Hello</p>\n"
}

it "responds to render as well as toHtml", {
  new HamlEngine("%p Hello").render().shouldBe "<p>Hello</p>\n"
}

it "can access locals", {
  new HamlEngine("%p= greeting").addLocal("greeting", "Welcome").toHtml().shouldEqual "<p>Welcome</p>\n"
}

it "obeys format option", {
  haml = """
%br
%span etc
  """
  new HamlEngine(haml).addOption("format", "html5").toHtml().shouldEqual "<br>\n<span>etc</span>\n"
  new HamlEngine(haml).addOption("format", "xhtml").toHtml().shouldEqual "<br />\n<span>etc</span>\n"
}

it "evaluates logic", {
  haml = """
- colors.each do |c|
  .color= c
  """
  new HamlEngine(haml).addLocal("colors", ["red", "blue", "green"]).render().shouldEqual(
    "<div class='color'>red</div>\n<div class='color'>blue</div>\n<div class='color'>green</div>\n")
}

it "uses the provided evaluation context", {
  context = java.util.Collections.singletonMap("color", "red")
  new HamlEngine(".c= self['color']").setEvaluationContext(context).render().shouldEqual "<div class='c'>red</div>\n"
}

it "accepts local variable definitions", {
  engine = new HamlEngine("foo")
  engine.addLocal("bar", 17)
  engine.locals.size().shouldBe 1
  ensure(engine.locals) {
    has(bar: 17)
  }
}

it "accepts option definitions", {
  engine = new HamlEngine("foo")
  engine.addOption("format", "html5")
  engine.options.size().shouldBe 1
  ensure(engine.options) {
    has(format: "html5")
  }
}

it "accepts chained local and options definitions", {
  engine = new HamlEngine("foo")
  engine.addLocal("answer", 42).
    addOption("preserve", null).
    addLocal("something", "else")
  engine.locals.size().shouldBe 2
  engine.options.size().shouldBe 1
}
