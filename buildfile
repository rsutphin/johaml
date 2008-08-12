require 'buildr'
require 'buildr/jetty'

repositories.remote << 'http://www.ibiblio.org/maven2'

# URL will change when the version changes (not just the version number)
HAML_VERSION = '2.0.2'
HAML_ZIP_URL = "http://rubyforge.org/frs/download.php/40512/haml-2.0.2.zip"

# TODO: how to do this in build.yml?
LOGBACK = group(%w{log4j-bridge logback-core logback-classic},
  :under => "ch.qos.logback", :version => "0.9.7")
SLF4J = group('slf4j-api', 'jcl104-over-slf4j',
  :under => "org.slf4j", :version => "1.4.2")

desc "A bridge library for using Haml & Sass as view-layer code in java applications"
define 'johaml' do
  project.version = '0.0'
  project.group = 'edu.northwestern.bioinformatics.johaml'
  compile.options.target = '1.5'
  
  define 'sample' do
    compile.with LOGBACK, SLF4J, :string_taglib, :jclang
    package(:war).with :libs => [ compile.dependencies, project('bridge'), project('bridge').compile.dependencies ]
  end
  
  define 'bridge' do
    resources.enhance do
      # Preferred option, but it doesn't work due to BUILDR-106 (will fixed in 1.3.3)
      # haml = download(
      #   artifact("com.hamptoncatlin.haml:haml:zip:#{HAML_VERSION}") => HAML_ZIP_URL)
      tmpfile = _("target/haml-#{HAML_VERSION}.zip")
      download(tmpfile => HAML_ZIP_URL).invoke
      haml = artifact("com.hamptoncatlin.haml:haml:zip:#{HAML_VERSION}").from(tmpfile)

      haml_dir = unzip(_("target/resources/haml-#{HAML_VERSION}") => haml).from_path("haml-#{HAML_VERSION}").target
      haml_dir.invoke
    end

    resources.filter.using :ant, 'HAML_VERSION' => HAML_VERSION
    resources.from _("src/main/ruby")

    # Hopefully this won't be necessary after JRuby 1.1.4
    custom_jruby = artifact("edu.northwestern.bioinformatics.jruby:patched-jruby-complete:jar:1.1.3").
      from(_('lib/patched-jruby-complete-1.1.3.jar'))
    compile.with :servlet, :jsp_api, custom_jruby, :bsf, :jcl, :jcio, :freemarker
    
    test.using :easyb
    test.with :string_taglib, :spring_mock, :spring, :jclang
    test.resources.enhance do
      dir = _("target/spec/resources/WEB-INF/lib")
      mkdir_p dir
      test.dependencies.map(&:to_s).grep(/jar$/).each do |f|
        cp f, File.join(dir, File.basename(f))
      end
    end

    package(:jar).include(_("target/resources/**/*")).exclude(_("target/spec/**/*"))
  end
end

task 'samples' => [project('johaml:sample').package(:war), jetty.use] do |t|
  jetty.deploy 'http://localhost:8080/', t.prerequisites.first
end
