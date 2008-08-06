# Rubyize TemplateHashModel & TemplateHashModelEx implementations
module RubyizeTemplateHashModel
  def self.included(clazz)
    if clazz.java_class.interfaces.include?(Java::FreemarkerTemplate::TemplateHashModelEx.java_class)
      clazz.class_eval do
        include Enumerable

        def each
          it = self.keys.iterator
          while it.hasNext
            k = it.next
            yield [k.to_s, self.get(k.to_s)]
          end
        end
      end
    end
  end

  def [](key)
    self.get(key.to_s)
  end
end

[
  Java::FreemarkerTemplate::SimpleHash,
  Java::FreemarkerTemplate::GeneralPurposeNothing,
  Java::FreemarkerExtServlet::HttpRequestHashModel,
  Java::FreemarkerExtServlet::HttpRequestParametersHashModel,
  Java::FreemarkerExtServlet::HttpSessionHashModel,
  Java::FreemarkerExtServlet::ServletContextHashModel,
  Java::FreemarkerExtBeans::BeanModel,
  Java::FreemarkerExtJsp::TaglibFactory,
  Java::FreemarkerExtJsp::JspContextModel
].each do |clazz|
  clazz.send(:include, RubyizeTemplateHashModel)
end

# TODO: expand this into more general TemplateCollectionModel handling, if warranted
class Java::FreemarkerTemplate::SimpleCollection
  include Enumerable

  def each
    it = self.iterator
    while (it.hasNext)
      yield it.next()
    end
  end
end
