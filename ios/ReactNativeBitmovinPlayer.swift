import Foundation
import UIKit
import AVFoundation

@objc(ReactNativeBitmovinPlayer)
class ReactNativeBitmovinPlayer: RCTViewManager {

    var playerView: ViewController!

    @objc(multiply:withB:withResolver:withRejecter:)
    func multiply(a: Float, b: Float, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        resolve(a*b)
    }

    override func view() -> UIView! {
        playerView = ViewController(frame: CGRect(x:0, y:0, width: 100, height: 100))
        return playerView
    }

    override static func requiresMainQueueSetup() -> Bool {
        return true
    }

    @objc(play)
    func play() -> Void {
//        playerView.play()
    }


}
