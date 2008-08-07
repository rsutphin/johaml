$LOAD_PATH << 'haml-@HAML_VERSION@/lib';

require 'sass'
$bridge.requires.each { |r| require r }

# Haml wants these to be actual ruby hashes
options = $bridge.options.inject({}) { |h, p| h[p.first.to_sym] = p.last; h }

# Some option values need to be symbols in order to work
%w(style attribute_syntax).map { |k| k.to_sym }.each { |k| options[k] = options[k].to_sym if options[k] }

$bridge.renderedContent = Sass::Engine.new($bridge.template, options).render()