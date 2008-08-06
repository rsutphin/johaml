# From http://onestepback.org/index.cgi/Tech/Ruby/BlankSlate.rdoc
# Modified to leave in methods that are invoked by Haml::Engine
class BlankSlate
  def self.__haml_required_method(m)
    [
      /^instance/,
      /^is_a\?$/,
      /^send$/,
      /^extend$/
    ].inject(false) { |result, re| result || m =~ re }
  end

  instance_methods.each { |m| undef_method m unless m =~ /^__/ || __haml_required_method(m)  }
end