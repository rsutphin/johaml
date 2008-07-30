require 'spec'
require 'java'

import "edu.northwestern.bioinformatics.haml.HamlEngine"

describe 'HamlEngine' do
  it "converts to an HTML string" do
    HamlEngine.new("%p Hello").toHtml().should == "<p>Hello</p>\n"
  end

  it "responds to render as well as toHtml" do
    HamlEngine.new("%p Hello").render().should == "<p>Hello</p>\n"
  end

  it "can access locals" do
    HamlEngine.new("%p= greeting").addLocal("greeting", "Welcome").toHtml().should == "<p>Welcome</p>\n"
  end

  it "obeys format option" do
    haml = <<-HAML
      %br
      %span etc
    HAML
    HamlEngine.new(haml).addOption("format", "html5").toHtml().should == "<br>\n<span>etc</span>\n"
    HamlEngine.new(haml).addOption("format", "xhtml").toHtml().should == "<br />\n<span>etc</span>\n"
  end

  it "evaluates logic" do
    haml = <<-HAML
      - colors.each do |c|
        .color= c
    HAML
    HamlEngine.new(haml).addLocal("colors", ["red", "blue", "green"]).render().should ==
      "<div class='color'>red</div>\n<div class='color'>blue</div>\n<div class='color'>green</div>\n"
  end

  it "uses the provided evaluation context" do
    context = java.util.Collections.singletonMap("color", "red")
    HamlEngine.new(".c= self['color']").setEvaluationContext(context).render().should == "<div class='c'>red</div>\n"
  end

  describe "configuration" do
    before do
      @engine = HamlEngine.new("foo")
    end

    it "accepts local variable definitions" do
      @engine.addLocal("bar", 17)
      @engine.should have(1).locals
      @engine.locals.first.should == ["bar", 17]
    end

    it "accepts option definitions" do
      @engine.addOption("output", "html5")
      @engine.should have(1).options
      @engine.options.first.should == ["output", "html5"]
    end

    it "accepts chained local and options definitions" do
      @engine.addLocal("answer", 42).
        addOption("preserve", nil).
        addLocal("something", "else")
      @engine.should have(2).locals
      @engine.should have(1).options
    end
  end
end