require 'json'
version = JSON.parse(File.read('package.json'))["version"]

Pod::Spec.new do |s|

  s.name           = "SPRNSynchronousList"
  s.version        = version
  s.summary        = "A list that renders synchronously on React Native."
  s.homepage       = "https://github.com/SudoPlz/react-native-synchronous-list"
  s.license        = "MIT"
  s.author         = { "Ioannis Kokkinidis" => "sudoplz@gmail.com" }
  s.ios.deployment_target = '7.0'
  s.tvos.deployment_target = '9.0'
  s.source         = { :git => "https://github.com/SudoPlz/react-native-synchronous-list.git", :tag => "v#{s.version}" }
  s.source_files   = 'SPRNSynchronousListManager/**/*.{h,m}'
  s.preserve_paths = "**/*.js"
  s.dependency 'React'

end
