require 'buildr'
require 'buildr/jetty'

repositories.remote << 'http://www.ibiblio.org/maven2'

HAML_VERSION = '2.0.1'

# TODO: how to do this in build.yml?
LOGBACK = group(%w{log4j-bridge logback-core logback-classic},
  :under => "ch.qos.logback", :version => "0.9.7")
SLF4J = group('slf4j-api', 'jcl104-over-slf4j',
  :under => "org.slf4j", :version => "1.4.2")

desc "A bridge library for using Haml & Sass as view-layer code in java applications"
define 'java-haml' do
  project.version = '0.0'
  project.group = 'edu.northwestern.bioinformatics.haml'
  compile.options.target = '1.5'
  
  define 'sample' do
    compile.with LOGBACK, SLF4J
    package(:war).with :libs => [ compile.dependencies, project('bridge'), project('bridge').compile.dependencies ]
  end
  
  define 'bridge' do
    test.using :easyb
    resources.from _("src/main/ruby")

    # Hopefully this won't be necessary after 1.1.4
    custom_jruby = artifact("edu.northwestern.bioinformatics.jruby:patched-jruby-complete:jar:1.1.3").
      from(_('lib/patched-jruby-complete-1.1.3.jar'))

    compile.with :servlet, custom_jruby, :bsf, :jcl, :jcio, :freemarker
    package(:jar).include(_("target/resources/**/*"))
  end
  
  task 'start' => [project('sample').package(:war), jetty.use] do |t|
    jetty.deploy 'http://localhost:8080/', t.prerequisites.first
  end
end