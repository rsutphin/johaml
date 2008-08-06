require 'blank_slate'
require 'freemarker/rubyize_freemarker_collections'

class TemplateModelContext < BlankSlate
  def initialize(template_hash_model, constant_keys = nil)
    @lookup = template_hash_model
    # Untested, so leave it out for now
#    if constant_keys
#      class << self
#        constant_keys.each do |k|
#          define_method(k.downcase.to_sym) { @lookup.get(k) }
#        end
#      end
#    end
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