package com.takeoffmediareactnativebitmovinplayer

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class ReactNativeBitmovinPlayerModule(
  reactContext: ReactApplicationContext,
  private val player: RNBitmovinPlayerView
) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "ReactNativeBitmovinPlayer"
    }

    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @ReactMethod
    fun multiply(a: Int, b: Int, promise: Promise) {
      promise.resolve(a * b)

    }

    @ReactMethod
    fun destroy() {
      player.destroy();
    }

}
