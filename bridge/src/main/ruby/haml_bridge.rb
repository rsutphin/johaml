# TODO: obvious
$LOAD_PATH << '/Library/Ruby/Gems/1.8/gems/haml-2.0.0/lib';

require 'haml';

# Haml wants these to be actual ruby hashes
options = $bridge.options.inject({}) { |h, p| h[p.first] = p.last }
locals = $bridge.locals.inject({}) { |h, p| h[p.first] = p.last }

$bridge.html = Haml::Engine.new($bridge.haml, options).to_html(Object.new, locals)