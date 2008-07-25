require 'buildr'
require 'buildr/jetty'

repositories.remote << 'http://www.ibiblio.org/maven2'

HAML_VERSION = '2.0.1'

desc "A bridge library for using Haml & Sass as view-layer code in java applications"
define 'java-haml' do
  project.version = '0.0'
  project.group = 'edu.northwestern.bioinformatics.haml'
  compile.options.target = '1.5'
  
  define 'sample' do
    package(:war).with :libs => project('bridge')
  end
  
  define 'bridge' do
    package :jar
    test.using :rspec
    compile.with :servlet, :jruby, :bsf, :jcl
  end
  
  task 'start' => [project('sample').package(:war), jetty.use] do |t|
    jetty.deploy 'http://localhost:8080/', t.prerequisites.first
  end
end