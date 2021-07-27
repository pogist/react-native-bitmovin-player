require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "takeoffmedia-react-native-bitmovin-player"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.platforms    = { :ios => "10.0" }
  s.source       = { :git => "https://github.com/jonathanm-tkf/react-native-bitmovin-player.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,mm,swift}"


  s.dependency "React-Core"

  s.dependency "BitmovinPlayer", "2.66.0"
  s.dependency "BitmovinAnalyticsCollector/Core", "1.20.0-beta2"
  s.dependency "BitmovinAnalyticsCollector/BitmovinPlayer", "1.20.0-beta2"

end
