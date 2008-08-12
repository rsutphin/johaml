import edu.northwestern.bioinformatics.johaml.SassEngine

it "converts to CSS", {
  new SassEngine("p\n  :border 1px solid blue").render().shouldBe "p {\n  border: 1px solid blue; }\n"
}

it "obeys the style option", {
  sass = """
p
  :width 50%
  :color green
  """
  new SassEngine(sass).addOption("style", "compact").render().shouldBe "p { width: 50%; color: green; }\n"
}

it "evaluates logic", {
  sass = """
!center_width = 30em
.footer
  :width= !center_width + 10em
  """
  new SassEngine(sass).addOption("style", "compact").render().shouldEqual(".footer { width: 40em; }\n")
}

it "accepts option definitions", {
  engine = new SassEngine("!foo = 0")
  engine.addOption("filename", "(inline)")
  engine.options.size().shouldBe 1
  ensure(engine.options) {
    has(filename: "(inline)")
  }
}

it "accepts chained options definitions", {
  engine = new SassEngine("!foo = 0")
  engine.addOption("attribute_syntax", "normal").addOption("style", "expanded")
  engine.options.size().shouldBe 2
}

// Pending
// it "resolves @imports"
