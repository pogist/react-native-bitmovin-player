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
    var nextCallback: Bool = false
    var customSeek: Bool = false
    var offset: TimeInterval = 0
    var hearbeat: Int = 10
    
    fileprivate var customMessageHandler: CustomMessageHandler?
    // Create player configuration
    let config = PlayerConfiguration()

    deinit {
        player?.destroy()
    }

    override func didSetProps(_ changedProps: [String]!) {

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

        if((self.configuration!["startOffset"]) != nil) {
            config.sourceItem?.options = SourceOptions();
            config.sourceItem?.options?.startOffset = self.configuration!["startOffset"] as! TimeInterval;
        }
        
        if((self.configuration!["hearbeat"]) != nil) {
            hearbeat = self.configuration!["hearbeat"] as! Int
        }

        if((self.configuration!["title"]) != nil) {
            config.sourceItem?.itemTitle = self.configuration!["title"] as? String;
        }
        
        if((self.configuration!["hasNextEpisode"]) != nil) {
            config.sourceItem?.metadata.addEntries(from: ["hasNextEpisode": self.configuration!["hasNextEpisode"] as! Bool])
        }
        
        if((self.configuration!["advisory"]) != nil) {
            config.sourceItem?.metadata.addEntries(from: ["advisory": self.configuration!["advisory"] as Any])
        }

        if((self.configuration!["subtitle"]) != nil) {
            config.sourceItem?.itemDescription = self.configuration!["subtitle"] as? String;
        }

        player?.setup(configuration: config)
        nextCallback = false;
        if (self.autoPlay == true){
            player?.play()
        }
    }

    override init(frame:CGRect) {
        super.init(frame: frame)

        var plistDictionary: NSDictionary?
        if let path = Bundle.main.path(forResource: "Info", ofType: "plist") {
            plistDictionary = NSDictionary(contentsOfFile: path)
        }

        /**
         * Go to https://github.com/bitmovin/bitmovin-player-ui to get started with creating a custom player UI.
         */
        if (plistDictionary!["BitmovinPlayerCss"] != nil) {
            config.styleConfiguration.playerUiCss = URL(string: plistDictionary!["BitmovinPlayerCss"] as! String)!
        }
        if (plistDictionary!["BitmovinPlayerJs"] != nil) {
            config.styleConfiguration.playerUiJs = URL(string: plistDictionary!["BitmovinPlayerJs"] as! String)!
        }

        config.styleConfiguration.userInterfaceConfiguration = bitmovinUserInterfaceConfiguration

        // Create player based on player configuration
        player = Player(configuration: config)

        // Create player view and pass the player instance to it
        let playerView = BMPBitmovinPlayerView(player: player!, frame: .zero)

        // Listen to player events
        player!.add(listener: self)

        playerView.autoresizingMask = [.flexibleHeight, .flexibleWidth]
        playerView.frame = frame

        playerView.add(listener: self)

        self.addSubview(playerView)
    }

    @objc var onLoad:RCTDirectEventBlock? = nil
    @objc var onPlaying:RCTDirectEventBlock? = nil
    @objc var onPause:RCTDirectEventBlock? = nil
    @objc var onEvent:RCTDirectEventBlock? = nil
    @objc var onSeek:RCTDirectEventBlock? = nil
    @objc var onForward:RCTDirectEventBlock? = nil
    @objc var onRewind:RCTDirectEventBlock? = nil
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    @IBAction fileprivate func toggleCloseButton(_ sender: Any) {
        // Use the configured customMessageHandler to send messages to the UI
        customMessageHandler?.sendMessage("toggleCloseButton")
    }
    
    @IBAction fileprivate func toggleForwardButton(_ sender: Any) {
        // Use the configured customMessageHandler to send messages to the UI
        customMessageHandler?.sendMessage("toggleForwardButton")
    }
    
    @IBAction fileprivate func toggleRewindButton(_ sender: Any) {
        // Use the configured customMessageHandler to send messages to the UI
        customMessageHandler?.sendMessage("toggleRewindButton")
    }
    
    @IBAction fileprivate func nextEpisodeButton(_ sender: Any) {
        // Use the configured customMessageHandler to send messages to the UI
        customMessageHandler?.sendMessage("nextEpisodeButton")
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

}

// MARK: - CustomMessageHandlerDelegate
extension ViewController: CustomMessageHandlerDelegate {
    func receivedSynchronousMessage(_ message: String, withData data: String?) -> String? {
        print("onEvent =) \(message)")
        
        if (message == "forwardButton") {
            if((self.onForward) != nil) {
                self.onForward!(["message": message, "time": self.player?.currentTime as Any, "volume": self.player?.volume as Any, "duration": self.player?.duration as Any])
                customSeek = true;
            }
            player?.seek(time: self.player!.currentTime + 10)
        }
        
        if (message == "rewindButton") {
            if((self.onRewind) != nil) {
                self.onRewind!(["message": message, "time": self.player?.currentTime as Any, "volume": self.player?.volume as Any, "duration": self.player?.duration as Any])
                customSeek = true;
            }
            player?.seek(time: self.player!.currentTime - 10)
        }
        
        if((self.onEvent) != nil) {
            self.onEvent!(["message": message, "time": self.player?.currentTime as Any, "volume": self.player?.volume as Any, "duration": self.player?.duration as Any])
        }

        return nil
    }

    func receivedAsynchronousMessage(_ message: String, withData data: String?) {
        print("received Asynchronouse Messagse", message, data ?? "")
    }
}

extension ViewController: UserInterfaceListener {
    func onControlsHide(_ event: ControlsHideEvent) {
        print("onControlsHide")
    }

    func onControlsShow(_ event: ControlsShowEvent) {
        print("onControlsShow")
    }
}

extension ViewController: PlayerListener {

    func onPlay(_ event: PlayEvent) {
        print("onPlay \(event.name)")
        if((self.onPlaying) != nil && self.player!.duration > 0) {
            self.onPlaying!(["message": "play", "time": self.player?.currentTime as Any, "volume": self.player?.volume as Any, "duration": self.player?.duration as Any])
        }
    }

    func onSeeked(_ event: SeekedEvent) {
        print("onSeeked \(event.name) \(event.timestamp) \(self.player?.currentTime as Any)")
        if (customSeek) {
            customSeek = false;
        } else if((self.onSeek) != nil) {
            self.onSeek!(["message": "seek", "time": self.player?.currentTime as Any, "volume": self.player?.volume as Any, "duration": self.player?.duration as Any])
        }
    }


    func onReady(_ event: ReadyEvent) {
        print("onReady \(event.name)")
        if((self.onLoad) != nil) {
            self.onLoad!(["message": "load", "volume": self.player?.volume as Any, "duration": self.player?.duration as Any])
        }
    }

    func onPaused(_ event: PausedEvent) {
        print("onPaused \(event.time)")
        if((self.onPause) != nil) {
            self.onPause!(["message": "pause", "time": self.player?.currentTime as Any, "volume": self.player?.volume as Any, "duration": self.player?.duration as Any])
        }
    }

    func onTimeChanged(_ event: TimeChangedEvent) {
        print("onTimeChanged \(event.currentTime) \(self.player?.duration ?? 0)")
        if (event.currentTime > (self.player?.duration ?? 0) - (self.configuration!["nextPlayback"] as! Double) && !nextCallback) {
            if((self.onEvent) != nil) {
                nextCallback = true;
                self.onEvent!(["message": "next"])
            }
        }
        
        if((event.currentTime > (offset + Double(hearbeat)) || event.currentTime < (offset - Double(hearbeat))) && event.currentTime < (self.player?.duration ?? 0)) {
            offset = event.currentTime;
            if((self.onEvent) != nil) {
                self.onEvent!(["message": "save", "time": self.player?.currentTime as Any])
            }
        }
    }

    func onDurationChanged(_ event: DurationChangedEvent) {
        print("onDurationChanged \(event.duration)")
    }

    func onError(_ event: ErrorEvent) {
        print("onError \(event.message)")
    }
}

