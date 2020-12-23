//
//   PlayerView.swift
//  ReactNativeBitmovinPlayer
//
//  Created by Jonathan Machado on 12/21/20.
//  Copyright © 2020 Facebook. All rights reserved.
//
import Foundation
import UIKit
//import PlayerKit
import AVFoundation
import BitmovinPlayer

//class PlayerView: UIView {
//
//  @objc var autoPlay: Bool = false
//  @objc var filename: NSString = ""
//  @objc var width: CGFloat = 200
//  @objc var height: CGFloat = 200
//
//  var player: RegularPlayer!
//
//  override init(frame:CGRect) {
//    super.init(frame: frame)
//    player = RegularPlayer()
//    player.view.frame = frame
//    self.addSubview(player.view)
//  }
//
//  override func didSetProps(_ changedProps: [String]!) {
//    player.set(AVURLAsset(url: URL.init(string: self.filename as String)!))
//    player.view.frame = CGRect(x: 0, y: 0, width: self.width, height: self.height)
//    if (self.autoPlay == true){
//      player.play()
//    }
//  }
//
//  public func play(){
//    self.player.play()
//  }
//
//  required init?(coder aDecoder: NSCoder) {
//    fatalError("init(coder:) has not been implemented")
//  }
//
//}

final class ViewController: UIView {

    @objc var autoPlay: Bool = false
    @objc var filename: NSString = ""
//    @objc var width: CGFloat = 300
//    @objc var height: CGFloat = 300
    
    var player: Player?

    deinit {
        player?.destroy()
    }
    
    override init(frame:CGRect) {
        super.init(frame: frame)

        // Define needed resources
        guard let streamUrl = URL(string: "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"),
              let posterUrl = URL(string: "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/poster.jpg") else {
            return
        }

        // Create player configuration
        let config = PlayerConfiguration()
        try! config.setSourceItem(url: streamUrl)

        // Set a poster image
        config.sourceItem?.posterSource = posterUrl

        // Create player based on player configuration
        player = Player(configuration: config)

        // Create player view and pass the player instance to it
        let playerView = BMPBitmovinPlayerView(player: player!, frame: .zero)
        
        // Listen to player events
        player!.add(listener: self)
        
        playerView.autoresizingMask = [.flexibleHeight, .flexibleWidth]
        playerView.frame = frame
//        playerView.backgroundColor = .black
        self.addSubview(playerView)
      }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
//    override func viewDidLoad() {
//        super.viewDidLoad()
//
//        self.view.backgroundColor = .black
//
//        // Define needed resources
//        guard let streamUrl = URL(string: "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"),
//              let posterUrl = URL(string: "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/poster.jpg") else {
//            return
//        }
//
//        // Create player configuration
//        let config = PlayerConfiguration()
//
//        do {
//
//
//

//


//
//            view.addSubview(playerView)
//            view.bringSubviewToFront(playerView)
//
//            self.player = player
//        } catch {
//            print("Configuration error: \(error)")
//        }
//    }
}

extension ViewController: PlayerListener {

    func onPlay(_ event: PlayEvent) {
        print("onPlay \(event.time)")
    }

    func onPaused(_ event: PausedEvent) {
        print("onPaused \(event.time)")
    }

    func onTimeChanged(_ event: TimeChangedEvent) {
        print("onTimeChanged \(event.currentTime)")
    }

    func onDurationChanged(_ event: DurationChangedEvent) {
        print("onDurationChanged \(event.duration)")
    }

    func onError(_ event: ErrorEvent) {
        print("onError \(event.message)")
    }
}

