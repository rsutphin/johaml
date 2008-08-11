require 'blank_slate'
require 'freemarker/rubyize_freemarker_collections'
require 'freemarker/callable_taglib'

module Freemarker

# Note: this class contains helpers for servlet-specific TemplateHashModels.  It might be better to
# move those bits to a subclass (or rename this class).
class TemplateModelContext < BlankSlate
  attr_reader :jsp_taglibs

  def initialize(template_hash_model)
    @lookup = template_hash_model
    fm_taglib_factory = @lookup.get("JspTaglibs")
    if fm_taglib_factory
      @jsp_taglibs = CallableTaglibFactory.new(fm_taglib_factory, self)
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