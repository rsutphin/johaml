# TODO: obvious
$LOAD_PATH << '/Library/Ruby/Gems/1.8/gems/haml-2.0.0/lib';

require 'haml';

# Haml wants these to be actual ruby hashes
options = $bridge.options.inject({}) { |h, p| h[p.first.to_sym] = p.last; h }
locals  = $bridge.locals.inject({})  { |h, p| h[p.first.to_sym] = p.last; h }

# Some option values need to be symbols in order to work
%w(output format).map { |k| k.to_sym }.each { |k| options[k] = options[k].to_sym if options[k] }

$bridge.html = Haml::Engine.new($bridge.haml, options).to_html($bridge.evaluationContext || Object.new, locals)