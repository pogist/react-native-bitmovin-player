import Foundation
import UIKit
import AVFoundation

@objc(ReactNativeBitmovinPlayer)
class ReactNativeBitmovinPlayer: RCTViewManager {

    @objc(multiply:withB:withResolver:withRejecter:)
    func multiply(a: Float, b: Float, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        resolve(a*b)
    }

    override func view() -> UIView! {
        return ViewController()
    }

    override static func requiresMainQueueSetup() -> Bool {
        return true
    }

    @objc(play)
    func play() -> Void {
//        playerView.play()
    }

}
