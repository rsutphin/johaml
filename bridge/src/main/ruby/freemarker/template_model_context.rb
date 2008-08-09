require 'blank_slate'
require 'freemarker/rubyize_freemarker_collections'
require 'freemarker/callable_taglib'

module Freemarker

class TemplateModelContext < BlankSlate
  attr_reader :jsp_taglibs

  def initialize(template_hash_model)
    @lookup = template_hash_model
    fm_taglib_factory = @lookup.get("JspTaglibs")
    @jsp_taglibs =
      if fm_taglib_factory
        class << fm_taglib_factory
          alias :original_bracket :[]

          def [](taglib_uri)
            CallableTaglib.new(self.original_bracket(taglib_uri))
          end
        end
        fm_taglib_factory
      else
        { }
      end
  end

  # These keys are included in the context passed by FreemarkerServlet, but have the form
  # of ruby constants and so will never get to method_missing.  This ensures they are available.
  %w(Request Session Application).each do |k|
    class_eval <<-RUBY
      def #{k.downcase}
        @lookup.get('#{k}')
      end
    RUBY
  end

  def params
    @lookup.get("RequestParameters")
  end

  def method_missing(method, *args)
    value = @lookup.get(method.to_s)
    return value unless value.nil?
    raise "No value in page model named '#{method}'"
  end
end

end # module