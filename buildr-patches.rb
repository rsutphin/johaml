
# Monkey patch for BUILDR-106
if Buildr::VERSION == "1.3.2"
  module Buildr
    def download(args)
      args = URI.parse(args) if String === args
      if URI === args
        # Given only a download URL, download into a temporary file.
        # You can infer the file from task name.
        temp = Tempfile.open(File.basename(args.to_s))
        file(temp.path).tap do |task|
          # Since temporary file exists, force a download.
          class << task ; def needed? ; true ; end ; end
          task.sources << args
          task.enhance { args.download temp }
        end
      else
        # Download to a file created by the task.
        fail unless args.keys.size == 1
        uri = URI.parse(args.values.first.to_s)
        file(args.keys.first.to_s).tap do |task|
          task.sources << uri
          task.enhance { uri.download task.name }
        end
      end
    end
  end
end