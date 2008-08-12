require 'blank_slate'

module Freemarker

class CallableTaglibFactory
  def initialize(fm_taglib_factory, evaluation_context)
    @fm_taglib_factory = fm_taglib_factory
    @evaluation_context = evaluation_context
  end

  def [](taglib_uri)
    CallableTaglib.new(@fm_taglib_factory[taglib_uri], @evaluation_context)
  end
end

class CallableTaglib < BlankSlate
  def initialize(fm_taglib, evaluation_context)
    @taglib = fm_taglib
    @evaluation_context = evaluation_context
  end

  def method_missing(name, *args, &block)
    tag = @taglib.get(name.to_s)
    raise "No tag named #{name}" unless tag
    options = args.pop || { }
    # desymbolize keys & convert/wrap values for freemarker
    options = options.inject({}) { |opts, pair| opts[pair.first.to_s] = __convert_value(pair.last); opts }
    __execute_tag(tag, options, &block)
  end

  private

  def __convert_value(val)
    Java::FreemarkerExtBeans::BeansWrapper.getDefaultInstance().wrap(val)
  end

  def __execute_tag(tag, options, &body)
    out =
      if block_given?
        eval("Java::EduNorthwesternBioinformaticsJohamlFreemarker::HamlBufferWriter.new(_hamlout)", body.binding)
      else
        Java::JavaIo::StringWriter.new
      end
    tw = tag.getWriter(out, options)

    if block_given?
      @evaluation_context.instance_variable_set(:@_jsp_writer, tw)
      eval(<<-SETUP, body.binding)
        _outer_hamlout ||= []
        _outer_hamlout.push _hamlout
        _hamlout = Freemarker::WriterHamlBuffer.new(_hamlout, _hamlout.options, @_jsp_writer)
      SETUP
      @evaluation_context.instance_variable_set(:@_jsp_writer, nil)
    end

    if tw.onStart() != Java::FreemarkerTemplate::TransformControl::SKIP_BODY
      begin
        if block_given?
          body.call
        end
      end while tw.afterBody() == Java::FreemarkerTemplate::TransformControl::REPEAT_EVALUATION
    end
    
    if block_given?
      eval(<<-TEARDOWN, body.binding)
        _hamlout = _outer_hamlout.pop
      TEARDOWN
    end

    tw.close()

    unless block_given?
      out.toString()
    end
  end
end

# A subclass of Haml::Buffer which uses a java.io.Writer as the internal buffer
class WriterHamlBuffer < Haml::Buffer
  def initialize(upper, options, writer)
    super(upper, options)
    @buffer = AppendableWriter.new(writer)
  end

  class AppendableWriter
    def initialize(writer)
      @writer = writer
    end

    def <<(s)
      @writer.write(s)
      self
    end
  end
end

end # module