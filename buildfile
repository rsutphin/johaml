require 'buildr'
require 'buildr/jetty'

repositories.remote << 'http://www.ibiblio.org/maven2'

desc "A bridge library for using Haml & Sass as view-layer code in java applications"
define 'java-haml' do
  project.version = '0.0'
  project.group = 'edu.northwestern.bioinformatics'
  compile.options.target = '1.5'
  
  define 'sample' do
    package(:war).with :libs => project('bridge')
  end
  
  define 'bridge' do
    package :jar
    test.using :rspec
    compile.with "javax.servlet:servlet-api:jar:2.4"
  end
  
  task 'start' => [project('sample').package(:war), jetty.use] do |t|
    jetty.deploy 'http://localhost:8080/', t.prerequisites.first
  end
end