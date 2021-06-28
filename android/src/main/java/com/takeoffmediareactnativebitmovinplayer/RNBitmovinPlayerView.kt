package com.takeoffmediareactnativebitmovinplayer
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.LinearLayout
import com.bitmovin.analytics.BitmovinAnalyticsConfig
import com.bitmovin.analytics.bitmovin.player.BitmovinPlayerCollector
import com.bitmovin.player.PlayerView
import com.bitmovin.player.SubtitleView
import com.bitmovin.player.api.PlaybackConfig
import com.bitmovin.player.api.Player
import com.bitmovin.player.api.PlayerConfig
import com.bitmovin.player.api.event.PlayerEvent
import com.bitmovin.player.api.event.SourceEvent
import com.bitmovin.player.api.event.on
import com.bitmovin.player.api.media.subtitle.SubtitleTrack
import com.bitmovin.player.api.media.thumbnail.ThumbnailTrack
import com.bitmovin.player.api.network.HttpRequestType
import com.bitmovin.player.api.network.NetworkConfig
import com.bitmovin.player.api.network.PreprocessHttpRequestCallback
import com.bitmovin.player.api.source.Source
import com.bitmovin.player.api.source.SourceConfig
import com.bitmovin.player.api.source.SourceOptions
import com.bitmovin.player.api.source.SourceType
import com.bitmovin.player.api.ui.StyleConfig
import com.bitmovin.player.ui.CustomMessageHandler
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import org.json.JSONObject


class RNBitmovinPlayerView() : SimpleViewManager<PlayerView>() {

  override fun getName() = "ReactNativeBitmovinPlayer"

  private lateinit var player: Player
  private lateinit var playerView: PlayerView
  private lateinit var subtitleView: SubtitleView

  private var configuration: ReadableMap? = null
  private var analyticsConfig: ReadableMap? = null
  private var analyticsCollector: BitmovinPlayerCollector? = null
  private var playerConfig: PlayerConfig = PlayerConfig()
  private var playBackConfig = PlaybackConfig()
  private var subtitleTrack: SubtitleTrack? = null
  private var thumbnailTrack: ThumbnailTrack? = null
  private var heartbeat: Int = 10
  private var offset: Double = 0.0
  private var nextCallback: Boolean = false
  private var customSeek: Boolean = false

  private var reactContextGlobal: ThemedReactContext? = null;

  // Create a custom javascriptInterface object which takes over the Bitmovin Web UI -> native calls
  val javascriptInterface = object : Any() {
    @JavascriptInterface
    fun closePlayerAsync(data: String) {
      val map: WritableMap = Arguments.createMap()
      map.putString("message", "closePlayer")
      map.putString("time", player.currentTime.toString())
      map.putString("volume", player.volume.toString())
      map.putString("duration", player.duration.toString())
      player.destroy()
      try {
        reactContextGlobal
          ?.getJSModule(RCTDeviceEventEmitter::class.java)
          ?.emit("onEvent", map)
        analyticsCollector!!.detachPlayer();
      } catch (e: Exception) {
        Log.e("ReactNative", "Caught Exception: " + e.message)
      }
    }
    @JavascriptInterface
    fun nextEpisodeAsync(data: String) {
      val map: WritableMap = Arguments.createMap()
      map.putString("message", "nextEpisode")
      map.putString("time", player.currentTime.toString())
      map.putString("volume", player.volume.toString())
      map.putString("duration", player.duration.toString())
      player.destroy()
      try {
        reactContextGlobal
          ?.getJSModule(RCTDeviceEventEmitter::class.java)
          ?.emit("onEvent", map)
        analyticsCollector!!.detachPlayer();
      } catch (e: Exception) {
        Log.e("ReactNative", "Caught Exception: " + e.message)
      }
    }
    @JavascriptInterface
    fun forwardButtonAsync(data: String) {
      val map: WritableMap = Arguments.createMap()
      map.putString("message", "forwardButton")
      map.putString("time", player.currentTime.toString())
      map.putString("volume", player.volume.toString())
      map.putString("duration", player.duration.toString())
      player.seek(player.currentTime + 10);
      customSeek = true;
      try {
        reactContextGlobal
          ?.getJSModule(RCTDeviceEventEmitter::class.java)
          ?.emit("onForward", map)

      } catch (e: Exception) {
        Log.e("ReactNative", "Caught Exception: " + e.message)
      }
    }
    @JavascriptInterface
    fun rewindButtonAsync(data: String) {
      val map: WritableMap = Arguments.createMap()
      map.putString("message", "rewindButton")
      map.putString("time", player.currentTime.toString())
      map.putString("volume", player.volume.toString())
      map.putString("duration", player.duration.toString())
      player.seek(player.currentTime - 10);
      customSeek = true;
      try {
        reactContextGlobal
          ?.getJSModule(RCTDeviceEventEmitter::class.java)
          ?.emit("onRewind", map)

      } catch (e: Exception) {
        Log.e("ReactNative", "Caught Exception: " + e.message)
      }
    }
  }

  fun destroy() {
    player.destroy()
  }

  @ReactProp(name = "autoPlay")
  fun setAutoPlay(view: PlayerView, autoPlay: Boolean?) {
    if (autoPlay != null && autoPlay == true) {
      playBackConfig.isAutoplayEnabled = true
    }
  }

  @ReactProp(name = "analytics")
  fun setAnalytics(view: PlayerView, analytics: ReadableMap) {
    analyticsConfig = analytics
    var title = "";
    var videoId = "";
    var userId = "";
    var cdnProvider = "";
    var customData1 = "";
    var customData2 = "";
    var customData3 = "";
    if (analyticsConfig != null && analyticsConfig!!.getString("title") != null) {
      title = analyticsConfig!!.getString("title").toString();
    }
    if (analyticsConfig != null && analyticsConfig!!.getString("videoId") != null) {
      videoId = analyticsConfig!!.getString("videoId").toString();
    }
    if (analyticsConfig != null && analyticsConfig!!.getString("userId") != null) {
      userId = analyticsConfig!!.getString("userId").toString();
    }
    if (analyticsConfig != null && analyticsConfig!!.getString("cdnProvider") != null) {
      cdnProvider = analyticsConfig!!.getString("cdnProvider").toString();
    }
    if (analyticsConfig != null && analyticsConfig!!.getString("customData1") != null) {
      customData1 = analyticsConfig!!.getString("customData1").toString();
    }
    if (analyticsConfig != null && analyticsConfig!!.getString("customData2") != null) {
      customData2 = analyticsConfig!!.getString("customData2").toString();
    }
    if (analyticsConfig != null && analyticsConfig!!.getString("customData3") != null) {
      customData3 = analyticsConfig!!.getString("customData3").toString();
    }

    // Create a BitmovinAnalyticsConfig using your Bitmovin analytics license key and (optionally) your Bitmovin Player Key
    val bitmovinAnalyticsConfig = BitmovinAnalyticsConfig(BuildConfig.BITMOVIN_ANALYTICS_LICENSE_KEY, BuildConfig.BITMOVIN_PLAYER_LICENSE_KEY)
    bitmovinAnalyticsConfig.videoId = videoId
    bitmovinAnalyticsConfig.title = title
    bitmovinAnalyticsConfig.customUserId = userId
    bitmovinAnalyticsConfig.cdnProvider = cdnProvider
    bitmovinAnalyticsConfig.customData1 = customData1
    bitmovinAnalyticsConfig.customData2 = customData2
    bitmovinAnalyticsConfig.customData3 = customData3

    // Create a BitmovinPlayerCollector object using the BitmovinAnalyitcsConfig you just created
    analyticsCollector = BitmovinPlayerCollector(bitmovinAnalyticsConfig, reactContextGlobal!!)

    // Attach your player instance
    analyticsCollector!!.attachPlayer(player)
  }

  @ReactProp(name = "configuration")
  fun setConfiguration(view: PlayerView, config: ReadableMap) {
    configuration = config;
    val sourceItem: SourceConfig?
    var title = "";
    var subtitle = "";
    var url = "";
    var poster = ""
    var hasNextEpisode = false
    var advisory = ""
    // Create a new source configuration

    if (configuration != null && configuration!!.getString("url") != null) {
      //load the SourceConfig into the player
      url = configuration!!.getString("url").toString()

      if (configuration!!.getString("subtitles") != null) {
        subtitleTrack = SubtitleTrack(configuration!!.getString("subtitles").toString(), null, "en", "en", false, "en")
      }

      if (configuration!!.getString("thumbnails") != null) {
        thumbnailTrack = ThumbnailTrack(configuration!!.getString("thumbnails").toString());
      }

      if (configuration!!.getString("title") != null) {
        title = configuration!!.getString("title").toString();
      }

      if (configuration!!.getString("subtitle") != null) {
        subtitle = configuration!!.getString("subtitle").toString();
      }

      if (configuration!!.getString("poster") != null) {
        poster = configuration!!.getString("poster").toString();
      }

      hasNextEpisode = configuration!!.getBoolean("hasNextEpisode")

      if (configuration!!.getMap("advisory") != null) {
        advisory = configuration!!.getMap("advisory")?.toString()!!;
      }

      val subtitleTracks = listOf(subtitleTrack)
      val metaDataMap = mapOf(
        "hasNextEpisode" to if (hasNextEpisode) "true" else "false",
        "advisory" to JSONObject(advisory).toString()
      );

      val options = SourceOptions();

      options.startOffset = configuration!!.getDouble("startOffset")

      val source = Source.create(
        SourceConfig(
          url = url,
          type = SourceType.Dash,
          title = title,
          description = subtitle,
          posterSource = poster,
          thumbnailTrack = thumbnailTrack,
          subtitleTracks = subtitleTracks as List<SubtitleTrack>,
          metadata = metaDataMap,
          options = options
        )
      )

      player.load(source)

      // Creating a SubtitleView and assign the current player instance.
      subtitleView = SubtitleView(reactContextGlobal!!)
      subtitleView.setPlayer(player)
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
      if (configuration != null && configuration!!.hasKey("nextPlayback")) {
        if (event.time <= player.duration - (configuration!!.getDouble("nextPlayback")) && nextCallback) {
          nextCallback = false;
        }
        if (event.time > player.duration - (configuration!!.getDouble("nextPlayback")) && !nextCallback) {
          nextCallback = true;

          val map: WritableMap = Arguments.createMap()
          map.putString("message", "next")
          reactContextGlobal
            ?.getJSModule(RCTDeviceEventEmitter::class.java)
            ?.emit("onEvent", map)
        }
      }

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
    if (customSeek) {
      customSeek = false;
    } else {
      try {
        reactContextGlobal
          ?.getJSModule(RCTDeviceEventEmitter::class.java)
          ?.emit("onSeek", map)

      } catch (e: Exception) {
        Log.e("ReactNative", "Caught Exception: " + e.message)
      }
    }
  }

  // create view
  override fun createViewInstance(reactContext: ThemedReactContext): PlayerView {
    reactContextGlobal = reactContext;
    // Go to https://github.com/bitmovin/bitmovin-player-ui to get started with creating a custom player UI.
    // Creating a new PlayerConfig with a StyleConfig

    if (BuildConfig.BITMOVIN_CSS != "" && BuildConfig.BITMOVIN_JS != "") {
      playerConfig.styleConfig = StyleConfig(
        // Set URLs for the JavaScript and the CSS
        playerUiJs = BuildConfig.BITMOVIN_JS,
        playerUiCss = BuildConfig.BITMOVIN_CSS
      );
    }

    playerConfig.playbackConfig = playBackConfig;

    val networkConfiguration = NetworkConfig()
    networkConfiguration.preprocessHttpRequestCallback = PreprocessHttpRequestCallback { type, request ->
      if (type == HttpRequestType.MediaSubtitles || type == HttpRequestType.Unknown) {
        Log.d("BasicPlayback", "> REQUEST type:$type url:${request.url}")
      } else {
        Log.d("BasicPlayback", "| REQUEST type:$type")
      }
      return@PreprocessHttpRequestCallback null
    }
    playerConfig.networkConfig = networkConfiguration

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
