package com.takeoffmediareactnativebitmovinplayer
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.LinearLayout
import com.bitmovin.player.PlayerView
import com.bitmovin.player.api.PlaybackConfig
import com.bitmovin.player.api.Player
import com.bitmovin.player.api.PlayerConfig
import com.bitmovin.player.api.event.PlayerEvent
import com.bitmovin.player.api.event.SourceEvent
import com.bitmovin.player.api.event.on
import com.bitmovin.player.api.media.subtitle.SubtitleTrack
import com.bitmovin.player.api.media.thumbnail.ThumbnailTrack
import com.bitmovin.player.api.source.SourceConfig
import com.bitmovin.player.ui.CustomMessageHandler
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp


class RNBitmovinPlayerView() : SimpleViewManager<PlayerView>() {

  override fun getName() = "ReactNativeBitmovinPlayer"

  private lateinit var player: Player
  private lateinit var playerView: PlayerView
  private var configuration: ReadableMap? = null
  private var playerConfig: PlayerConfig = PlayerConfig()
  private var playBackConfig = PlaybackConfig()
  private var subtitleTrack: SubtitleTrack? = null
  private var thumbnailTrack: ThumbnailTrack? = null
  private var heartbeat: Int = 10
  private var offset: Double = 0.0
  private var nextCallback: Boolean = false

  private var reactContextGlobal: ThemedReactContext? = null;

  // Create a custom javascriptInterface object which takes over the Bitmovin Web UI -> native calls
  val javascriptInterface = object : Any() {
    @JavascriptInterface
    fun closePlayer(data: String): String? {
      // finish()
      return null
    }
  }

  @ReactProp(name = "autoPlay")
  fun setAutoPlay(view: PlayerView, autoPlay: Boolean?) {
    if (autoPlay != null && autoPlay == true) {
      playBackConfig.isAutoplayEnabled = true
    }
  }

  @ReactProp(name = "configuration")
  fun setConfiguration(view: PlayerView, config: ReadableMap) {
    configuration = config;
    val sourceItem: SourceConfig?
    // Create a new source configuration

    if (configuration != null && configuration!!.getString("url") != null) {
      //load the SourceConfig into the player
      sourceItem = SourceConfig.fromUrl(configuration!!.getString("url").toString())

      if (configuration!!.getString("subtitles") != null) {
        subtitleTrack = SubtitleTrack(configuration!!.getString("subtitles").toString(), null, "en", "en", false, "en")

        sourceItem.addSubtitleTrack(subtitleTrack!!);
      }

      if (configuration!!.getString("thumbnails") != null) {
        thumbnailTrack = ThumbnailTrack(configuration!!.getString("thumbnails").toString());
      }

      player.load(sourceItem)
    }


    if (configuration != null && configuration!!.getString("title") != null) {

    }

    if (configuration != null && configuration!!.getString("heartbeat") != null) {
      heartbeat = configuration!!.getString("hearbeat")?.toInt() ?: 30
    }
  }

  // Setup CustomMessageHandler for communication with Bitmovin Web UI
  private val customMessageHandler = CustomMessageHandler(javascriptInterface)

  private fun onPlay(event: PlayerEvent.Play) {

    val map: WritableMap = Arguments.createMap()
    map.putString("message", "play")
    map.putString("time", player.currentTime.toString())
    map.putString("volume", player.volume.toString())
    map.putString("duration", player.duration.toString())

    try {
      reactContextGlobal
        ?.getJSModule(RCTDeviceEventEmitter::class.java)
          ?.emit("onPlay", map)

    } catch (e: Exception) {
      Log.e("ReactNative", "Caught Exception: " + e.message)
    }
  }

  private fun onTimeChanged(event: PlayerEvent.TimeChanged) {
    try {
      // next
//      if (configuration != null && configuration!!.getString("nextPlayback") != null) {
//        if (event.time <= player.duration - (configuration!!.getString("nextPlayback")?.toDouble()
//            ?: 30.0) && nextCallback) {
//          nextCallback = false;
//        }
//        if (event.time > player.duration - (configuration!!.getString("nextPlayback")?.toDouble()
//            ?: 30.0) && !nextCallback) {
//          nextCallback = true;
//
//          val map: WritableMap = Arguments.createMap()
//          map.putString("message", "next")
//          reactContextGlobal
//            ?.getJSModule(RCTDeviceEventEmitter::class.java)
//            ?.emit("onEvent", map)
//        }
//      }

      // save
      if((event.time > (offset + heartbeat) || event.time < (offset - heartbeat)) && event.time < (player.duration)) {
        offset = event.time
        val map: WritableMap = Arguments.createMap()
        map.putString("message", "save")
        map.putString("time", player.currentTime.toString())
        map.putString("volume", player.volume.toString())
        map.putString("duration", player.duration.toString())
        reactContextGlobal
          ?.getJSModule(RCTDeviceEventEmitter::class.java)
          ?.emit("onEvent", map)
      }
    } catch (e: Exception) {
      Log.e("ReactNative", "Caught Exception: " + e.message)
    }
  }

  private fun onPause(event: PlayerEvent.Paused) {
    val map: WritableMap = Arguments.createMap()
    map.putString("message", "pause")
    map.putString("time", player.currentTime.toString())
    map.putString("volume", player.volume.toString())
    map.putString("duration", player.duration.toString())

    try {
      reactContextGlobal
        ?.getJSModule(RCTDeviceEventEmitter::class.java)
        ?.emit("onPause", map)

    } catch (e: Exception) {
      Log.e("ReactNative", "Caught Exception: " + e.message)
    }
  }

  private fun onLoad(event: SourceEvent.Loaded) {
    val map: WritableMap = Arguments.createMap()
    map.putString("message", "load")
    map.putString("volume", player.volume.toString())
    map.putString("duration", player.duration.toString())

    try {
      reactContextGlobal
        ?.getJSModule(RCTDeviceEventEmitter::class.java)
        ?.emit("onLoad", map)

    } catch (e: Exception) {
      Log.e("ReactNative", "Caught Exception: " + e.message)
    }
  }

  private fun onSeek(event: PlayerEvent.Seek) {
    val map: WritableMap = Arguments.createMap()
    map.putString("message", "seek")
    map.putString("time", player.currentTime.toString())
    map.putString("volume", player.volume.toString())
    map.putString("duration", player.duration.toString())

    try {
      reactContextGlobal
        ?.getJSModule(RCTDeviceEventEmitter::class.java)
        ?.emit("onSeek", map)

    } catch (e: Exception) {
      Log.e("ReactNative", "Caught Exception: " + e.message)
    }
  }

  // create view
  override fun createViewInstance(reactContext: ThemedReactContext): PlayerView {
    reactContextGlobal = reactContext;
    // Go to https://github.com/bitmovin/bitmovin-player-ui to get started with creating a custom player UI.
    // Creating a new PlayerConfig with a StyleConfig
    Log.i("jona", BuildConfig.APP_NAME);

//    playerConfig.styleConfig = StyleConfig(
//      // Set URLs for the JavaScript and the CSS
//      playerUiJs = "https://stagev2-app-assets.britbox.takeoffmedia.com/player/uat/native/js/bitmovinplayer-ui.min.js",
//      playerUiCss = "https://stagev2-app-assets.britbox.takeoffmedia.com/player/uat/native/css/bitmovinplayer-ui.min.css"
//    );
    playerConfig.playbackConfig = playBackConfig;

    // Create a Player with our PlayerConfig
    player = Player.create(reactContext, playerConfig)

    // Create new PlayerView with our Player
    playerView = PlayerView(reactContext, player)
    playerView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

    // Set the CustomMessageHandler to the playerView
    playerView.setCustomMessageHandler(customMessageHandler)
    nextCallback = false;

    player.on(::onLoad)
    player.on(::onPlay)
    player.on(::onPause)
    player.on(::onSeek)
    player.on(::onTimeChanged)

    return playerView;
  }


}
