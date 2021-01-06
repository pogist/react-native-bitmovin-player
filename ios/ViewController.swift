//
//  ViewController.swift
//  ReactNativeBitmovinPlayer
//
//  Created by Jonathan Machado on 12/21/20.
//  Copyright © 2020 Facebook. All rights reserved.
//
import UIKit
import BitmovinPlayer

final class ViewController: UIView {
    @objc var autoPlay: Bool = false
    @objc var configuration: NSDictionary? = nil

    var player: Player?
    fileprivate var customMessageHandler: CustomMessageHandler?
    // Create player configuration
    let config = PlayerConfiguration()
    
    /**
     * Go to https://github.com/bitmovin/bitmovin-player-ui to get started with creating a custom player UI.
     */
    let cssURL = URL.init(string: "https://stagev2-app-assets.britbox.takeoffmedia.com/player/uat/native/bitmovinplayer-ui.min.css")
    
    let jsURL = URL.init(string: "https://stagev2-app-assets.britbox.takeoffmedia.com/player/uat/native/bitmovinplayer-ui.min.js")
    
    
//    config.styleConfiguration.playerUiCss = cssURL
//    config.styleConfiguration.playerUiJs = jsURL

//    config.styleConfiguration.userInterfaceConfiguration = bitmovinUserInterfaceConfiguration

    deinit {
        player?.destroy()
    }

    override func didSetProps(_ changedProps: [String]!) {
        config.styleConfiguration.playerUiJs = jsURL!
        config.styleConfiguration.playerUiCss = cssURL!
        
        config.styleConfiguration.userInterfaceConfiguration = bitmovinUserInterfaceConfiguration
            
        try! config.setSourceItem(url: URL.init(string: self.configuration!["url"] as! String)!)
        if((self.configuration!["poster"]) != nil) {
            config.sourceItem?.posterSource = URL.init(string: self.configuration!["poster"] as! String)!
        }
        if((self.configuration!["subtitles"]) != nil) {
            let subtitleTrack = SubtitleTrack(url: URL(string: self.configuration!["subtitles"] as! String),
              label: "en",
              identifier: "en",
              isDefaultTrack: true,
              language: "en")
            config.sourceItem?.add(subtitleTrack: subtitleTrack)
        }
        if((self.configuration!["thumbnails"]) != nil) {
            let thumbnailsTrack = ThumbnailTrack(url: URL(string: self.configuration!["thumbnails"] as! String)!, label: "thumbnails", identifier: "thumbnails", isDefaultTrack: true)
            config.sourceItem?.thumbnailTrack = thumbnailsTrack;
        }
        player?.setup(configuration: config)
        if (self.autoPlay == true){
            player?.play()
        }
    }

    override init(frame:CGRect) {
        super.init(frame: frame)

        // Create player based on player configuration
        player = Player(configuration: config)

        // Create player view and pass the player instance to it
        let playerView = BMPBitmovinPlayerView(player: player!, frame: .zero)

        // Listen to player events
        player!.add(listener: self)

        playerView.autoresizingMask = [.flexibleHeight, .flexibleWidth]
        playerView.frame = frame

        self.addSubview(playerView)
    }

    @objc var onLoad:RCTDirectEventBlock? = nil
    @objc var onPlaying:RCTDirectEventBlock? = nil
    @objc var onPause:RCTDirectEventBlock? = nil

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    @IBAction fileprivate func toggleCloseButton(_ sender: Any) {
        // Use the configured customMessageHandler to send messages to the UI
        customMessageHandler?.sendMessage("toggleCloseButton")
    }

    fileprivate var bitmovinUserInterfaceConfiguration: BitmovinUserInterfaceConfiguration {
        // Configure the JS <> Native communication
        let bitmovinUserInterfaceConfiguration = BitmovinUserInterfaceConfiguration()
        // Create an instance of the custom message handler
        customMessageHandler = CustomMessageHandler()
        customMessageHandler?.delegate = self
        bitmovinUserInterfaceConfiguration.customMessageHandler = customMessageHandler
        return bitmovinUserInterfaceConfiguration
    }

    func onClose() -> Void {
        if ((player) != nil) {
            print("onClose")
        }
    }
}

// MARK: - CustomMessageHandlerDelegate
extension ViewController: CustomMessageHandlerDelegate {
    func receivedSynchronousMessage(_ message: String, withData data: String?) -> String? {
        if message == "closePlayer" {
//            dismiss(animated: true, completion: nil)
        }

        return nil
    }

    func receivedAsynchronousMessage(_ message: String, withData data: String?) {
        print("received Asynchronouse Messagse", message, data ?? "")
    }
}


extension ViewController: PlayerListener {

    func onPlay(_ event: PlayEvent) {
        print("onPlay \(event.time)")
        if((self.onPlaying) != nil) {
            self.onPlaying!(["message": "play"])
        }
    }

    func onReady(_ event: ReadyEvent) {
        print("onReady \(event.name)")
        if((self.onLoad) != nil) {
            self.onLoad!(["message": "load"])
        }
    }

    func onPaused(_ event: PausedEvent) {
        print("onPaused \(event.time)")
        if((self.onPause) != nil) {
            self.onPause!(["message": "pause"])
        }
    }

    func onTimeChanged(_ event: TimeChangedEvent) {
        print("onTimeChanged \(event.currentTime) \(self.player?.duration ?? 0)")
    }

    func onDurationChanged(_ event: DurationChangedEvent) {
        print("onDurationChanged \(event.duration)")
    }

    func onError(_ event: ErrorEvent) {
        print("onError \(event.message)")
    }
}

