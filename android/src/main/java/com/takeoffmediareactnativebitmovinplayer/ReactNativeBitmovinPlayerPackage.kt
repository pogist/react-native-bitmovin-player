package com.takeoffmediareactnativebitmovinplayer

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.takeoffmediareactnativebitmovinplayer.view.RCTBitmovinPlayerView


class ReactNativeBitmovinPlayerPackage : ReactPackage {
  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
    return listOf(ReactNativeBitmovinPlayerModule(reactContext))
  }

  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>>{
    return listOf(
      ReactNativeBitmovinPlayerManager(),
      RCTBitmovinPlayerView(reactContext)
    )
  }
}
