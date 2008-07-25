require 'spec'
require 'java'

import "edu.northwestern.bioinformatics.haml.HamlEngine"
import "java.io.StringReader"

describe 'HamlEngine' do
  describe 'from String' do
    it "can write back to an HTML string" do
      HamlEngine.new("%p Hello").toHtml().should == "<p>Hello</p>"
    end
  end
end