require 'blank_slate'

module Freemarker

class CallableTaglib < BlankSlate
  def initialize(fm_taglib)
    @taglib = fm_taglib
  end

  def method_missing(name, *args)
    tag = @taglib.get(name.to_s)
    raise "No tag named #{name}" unless tag
    options = args.pop || { }
    # desymbolize keys & convert/wrap values for freemarker
    options = options.inject({}) { |opts, pair| opts[pair.first.to_s] = __convert_value(pair.last); opts }
    __execute_tag(tag, options)
  end

  private

  def __convert_value(val)
    Java::FreemarkerExtBeans::BeansWrapper.getDefaultInstance().wrap(val)
  end

  def __execute_tag(tag, options)
    out = Java::JavaIo::StringWriter.new
    tw = tag.getWriter(out, options)

    if tw.onStart() != Java::FreemarkerTemplate::TransformControl::SKIP_BODY
      begin
        # TODO: need to pass the body in here somehow -- yield?
      end while tw.afterBody() == Java::FreemarkerTemplate::TransformControl::REPEAT_EVALUATION
    end

    out.toString()
  end
end

end # module