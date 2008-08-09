import edu.northwestern.bioinformatics.haml.freemarker.HamlFreeMarkerConfiguration
import edu.northwestern.bioinformatics.haml.freemarker.HamlTemplate

import freemarker.template.SimpleHash
import freemarker.template.ObjectWrapper
import freemarker.ext.jsp.TaglibFactory
import freemarker.ext.servlet.ServletContextHashModel
import freemarker.ext.servlet.HttpRequestHashModel

import org.springframework.core.io.FileSystemResourceLoader
import org.springframework.mock.web.MockServletContext
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.mock.web.MockServletConfig

import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.GenericServlet

class MockServlet extends GenericServlet {
  public void service(ServletRequest servletRequest, ServletResponse servletResponse) {
    throw new UnsupportedOperationException("service not implemented");
  }
}

before "create config", {
  configuration = new HamlFreeMarkerConfiguration(HamlTemplate.CREATOR)
  request = new MockHttpServletRequest();
  response = new MockHttpServletResponse();
  servletContext = new MockServletContext("bridge/target/resources/test", new FileSystemResourceLoader())
  servletConfig = new MockServletConfig(servletContext)
  servlet = new MockServlet()
  servlet.init(servletConfig)
  taglibFactory = new TaglibFactory(servletContext)
}

def createTemplate(haml) {
  r = new StringReader(haml)
  return new HamlTemplate("test template", r, configuration)
}

String rendered(haml, rootNode) {
  w = new StringWriter();
  createTemplate(haml).process(rootNode, w)
  return w.toString()
}

it "renders the template", {
  rendered("%p foo", null).shouldBe "<p>foo</p>\n"
}

it "exposes root node keys as local method calls", {
  root = new SimpleHash()
  root.put("answer", 42)
  rendered(".answer= answer", root).shouldBe "<div class='answer'>42</div>\n"
}

it "allows bracket indexing on Freemarker hashes", {
  root = new SimpleHash()
  node = new SimpleHash()
  root.put("structure", node)
  node.put("fine", 137)
  rendered("%i= structure['fine']", root).shouldBe "<i>137</i>\n"
}

it "exposes 'Request' root node key as 'request'", {
  root = new SimpleHash()
  root.put("Request", [ foo: "bar" ])
  rendered("%b= request['foo']", root).shouldBe "<b>bar</b>\n"
}

it "exposes 'RequestParameters' root node key as 'params'", {
  root = new SimpleHash()
  root.put("RequestParameters", [ foo: "bar" ])
  rendered("%b= params['foo']", root).shouldBe "<b>bar</b>\n"
}

it "exposes 'Session' root node key as 'session'", {
  root = new SimpleHash()
  root.put("Session", [ foo: "bar" ])
  rendered("%b= session['foo']", root).shouldBe "<b>bar</b>\n"
}

it "exposes 'Application' root node key as 'application'", {
  root = new SimpleHash()
  root.put("Application", [ foo: "bar" ])
  rendered("%b= application['foo']", root).shouldBe "<b>bar</b>\n"
}

it "exposes taglibs via the jsp_taglibs lookup", {
  root = new SimpleHash()
  root.put("JspTaglibs", [ foo: "bar" ])
  // just checking for no exception
  rendered("- tags = jsp_taglibs['foo']", root)
}

it "executes simple, no-body tags from taglibs", {
  root = new SimpleHash()
  root.put("JspTaglibs", taglibFactory)
  root.put("Application", new ServletContextHashModel(servlet, ObjectWrapper.DEFAULT_WRAPPER))
  root.put("Request", new HttpRequestHashModel(request, response, ObjectWrapper.DEFAULT_WRAPPER))
  haml = """
- Str = jsp_taglibs['http://jakarta.apache.org/taglibs/string-1.1']
.fib= Str.join(:items => [1, 1, 2, 3, 5, 8].to_java(), :separator => " - ")
"""
  rendered(haml, root).shouldBe "<div class='fib'>1 - 1 - 2 - 3 - 5 - 8</div>\n"
}

/* TODO: reenable when the verbose logging is suppressed
it "gives a reasonable error message when attempting to resolve an unknown key", {
  try {
    rendered("%em= bogon", new SimpleHash())
  } catch (RuntimeException re) {
    re.getMessage().shouldHave "No value in page model named 'bogon'"
  }
}
*/