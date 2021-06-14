package com.takeoffmediareactnativebitmovinplayer

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager


class ReactNativeBitmovinPlayerPackage : ReactPackage {
    private val player = RNBitmovinPlayerView();
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(ReactNativeBitmovinPlayerModule(reactContext, player))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): MutableList<ViewManager<out View, out ReactShadowNode<*>>> {
      return mutableListOf(
        player
      )
    }

}
