$LOAD_PATH << 'haml-@HAML_VERSION@/lib';

# Haml fails when attempting to determine its version number inside the JAR, so set it explicitly
module Haml; VERSION = '@HAML_VERSION@'; end
require 'haml';

$bridge.requires.each { |r| require r }

# Haml wants these to be actual ruby hashes
options = $bridge.options.inject({}) { |h, p| h[p.first.to_sym] = p.last; h }
locals  = $bridge.locals.inject({})  { |h, p| h[p.first.to_sym] = p.last; h }

# Some option values need to be symbols in order to work
%w(output format).map { |k| k.to_sym }.each { |k| options[k] = options[k].to_sym if options[k] }

evalContext =
  if $bridge.evaluationContextExpression
    eval($bridge.evaluationContextExpression)
  elsif $bridge.evaluationContext
    $bridge.evaluationContext
  else
    Object.new
  end

$bridge.html = Haml::Engine.new($bridge.haml, options).render(evalContext, locals)