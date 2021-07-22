package com.takeoffmediareactnativebitmovinplayer;

import android.webkit.JavascriptInterface;

import com.bitmovin.analytics.BitmovinAnalyticsConfig;
import com.bitmovin.analytics.bitmovin.player.BitmovinPlayerCollector;
import com.bitmovin.player.api.event.listener.OnReadyListener;
import com.bitmovin.player.api.event.listener.OnPlayListener;
import com.bitmovin.player.api.event.listener.OnPausedListener;
import com.bitmovin.player.api.event.listener.OnTimeChangedListener;
import com.bitmovin.player.api.event.listener.OnStallStartedListener;
import com.bitmovin.player.api.event.listener.OnStallEndedListener;
import com.bitmovin.player.api.event.listener.OnPlaybackFinishedListener;
import com.bitmovin.player.api.event.listener.OnRenderFirstFrameListener;
import com.bitmovin.player.api.event.listener.OnErrorListener;
import com.bitmovin.player.api.event.listener.OnMutedListener;
import com.bitmovin.player.api.event.listener.OnUnmutedListener;
import com.bitmovin.player.api.event.listener.OnSeekListener;
import com.bitmovin.player.api.event.listener.OnSeekedListener;
import com.bitmovin.player.api.event.listener.OnFullscreenEnterListener;
import com.bitmovin.player.api.event.listener.OnFullscreenExitListener;
import com.bitmovin.player.config.PlayerConfiguration;
import com.bitmovin.player.BitmovinPlayer;
import com.bitmovin.player.BitmovinPlayerView;
import com.bitmovin.player.ui.CustomMessageHandler;
import com.bitmovin.player.ui.FullscreenHandler;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class ReactNativeBitmovinPlayerManager extends SimpleViewManager<BitmovinPlayerView> implements FullscreenHandler, LifecycleEventListener {

  public static final String REACT_CLASS = "ReactNativeBitmovinPlayer";

  private BitmovinPlayerView _playerView;
  private BitmovinPlayer _player;
  private boolean _fullscreen;
  private ThemedReactContext _reactContext;
  private Integer heartbeat = 30;
  private Double offset = 0.0;
  private boolean nextCallback = false;
  private boolean customSeek = false;
  private ReadableMap configuration = null;

  @NotNull
  @Override
  public String getName() {
      return REACT_CLASS;
  }

  public Map getExportedCustomBubblingEventTypeConstants() {
    return MapBuilder.builder()
      .put(
        "onReady",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onReady")
        )
      )
      .put(
        "onEvent",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onEvent")
        )
      )
      .put(
        "onForward",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onForward")
        )
      )
      .put(
        "onRewind",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onRewind")
        )
      )
      .put(
        "onPlay",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onPlay")
        )
      )
      .put(
        "onPause",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onPause")
        )
      )
      .put(
        "onTimeChanged",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onTimeChanged")
        )
      )
      .put(
        "onStallStarted",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onStallStarted")
        )
      )
      .put(
        "onStallEnded",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onStallEnded")
        )
      )
      .put(
        "onPlaybackFinished",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onPlaybackFinished")
        )
      )
      .put(
        "onRenderFirstFrame",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onRenderFirstFrame")
        )
      )
      .put(
        "onError",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "_onPlayerError")
        )
      )
      .put(
        "onMuted",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onMuted")
        )
      )
      .put(
        "onUnmuted",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onUnmuted")
        )
      )
      .put(
        "onSeek",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onSeek")
        )
      )
      .put(
        "onSeeked",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onSeeked")
        )
      )
      .put(
        "onFullscreenEnter",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onFullscreenEnter")
        )
      )
      .put(
        "_onFullscreenExit",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "_onFullscreenExit")
        )
      )
      .build();
    }

  // Create a custom javascriptInterface object which takes over the Bitmovin Web UI -> native calls
  Object javascriptInterface = new Object() {
    @JavascriptInterface
    public void closePlayerAsync(String data) {
      WritableMap map = Arguments.createMap();
      map.putString("message", "closePlayer");
      map.putString("time", String.valueOf(_player.getCurrentTime()));
      map.putString("volume", String.valueOf(_player.getVolume()));
      map.putString("duration", String.valueOf(_player.getDuration()));
      _player.destroy();
      try {
        _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
          _playerView.getId(),
          "onEvent",
          map);
      } catch (Exception e) {
        throw new ClassCastException(String.format("Cannot onEvent closePlater error message: %s", e.getMessage()));
      }
    }
    @JavascriptInterface
    public void nextEpisodeAsync(String data) {
      WritableMap map = Arguments.createMap();
      map.putString("message", "nextEpisode");
      map.putString("time", String.valueOf(_player.getCurrentTime()));
      map.putString("volume", String.valueOf(_player.getVolume()));
      map.putString("duration", String.valueOf(_player.getDuration()));
      _player.destroy();
      try {
        _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
          _playerView.getId(),
          "onEvent",
          map);
      } catch (Exception e) {
        throw new ClassCastException(String.format("Cannot onEvent nextEpisode error message: %s", e.getMessage()));
      }
    }
    @JavascriptInterface
    public void forwardButtonAsync(String data) {
      WritableMap map = Arguments.createMap();
      map.putString("message", "forwardButton");
      map.putString("time", String.valueOf(_player.getCurrentTime()));
      map.putString("volume", String.valueOf(_player.getVolume()));
      map.putString("duration", String.valueOf(_player.getDuration()));
      _player.seek(_player.getCurrentTime() + 10);
      customSeek = true;
      try {
        _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
          _playerView.getId(),
          "onForward",
          map);
      } catch (Exception e) {
        throw new ClassCastException(String.format("Cannot onForward error message: %s", e.getMessage()));
      }
    }
    @JavascriptInterface
    public void rewindButtonAsync(String data) {
      WritableMap map = Arguments.createMap();
      map.putString("message", "rewindButton");
      map.putString("time", String.valueOf(_player.getCurrentTime()));
      map.putString("volume", String.valueOf(_player.getVolume()));
      map.putString("duration", String.valueOf(_player.getDuration()));
      _player.seek(_player.getCurrentTime() - 10);
      customSeek = true;
      try {
        _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
          _playerView.getId(),
          "onRewind",
          map);
      } catch (Exception e) {
        throw new ClassCastException(String.format("Cannot onRewind error message: %s", e.getMessage()));
      }
    }
  };

  // Setup CustomMessageHandler for communication with Bitmovin Web UI

  @NotNull
  @Override
  public BitmovinPlayerView createViewInstance(@NotNull ThemedReactContext context) {
    _reactContext = context;
    _playerView = new BitmovinPlayerView(context);

    CustomMessageHandler customMessageHandler = new CustomMessageHandler(javascriptInterface);

    // Set the CustomMessageHandler to the playerView
    _playerView.setCustomMessageHandler(customMessageHandler);

    _player = _playerView.getPlayer();
    _fullscreen = false;

    setListeners();

    nextCallback = false;

    return _playerView;
  }

  @Override
  public void onDropViewInstance(@NotNull BitmovinPlayerView view) {
    _playerView.onDestroy();

    super.onDropViewInstance(view);

    _player = null;
    _playerView = null;
  }

  @ReactProp(name = "analytics")
  public void setAnalytics(BitmovinPlayerView view, ReadableMap analytics) {
    String title = "";
    String videoId = "";
    String userId = "";
    String cdnProvider = "";
    String customData1 = "";
    String customData2 = "";
    String customData3 = "";
    if (analytics != null && analytics.getString("title") != null) {
      title = analytics.getString("title");
    }
    if (analytics != null && analytics.getString("videoId") != null) {
      videoId = analytics.getString("videoId");
    }
    if (analytics != null && analytics.getString("userId") != null) {
      userId = analytics.getString("userId");
    }
    if (analytics != null && analytics.getString("cdnProvider") != null) {
      cdnProvider = analytics.getString("cdnProvider");
    }
    if (analytics != null && analytics.getString("customData1") != null) {
      customData1 = analytics.getString("customData1");
    }
    if (analytics != null && analytics.getString("customData2") != null) {
      customData2 = analytics.getString("customData2");
    }
    if (analytics != null && analytics.getString("customData3") != null) {
      customData3 = analytics.getString("customData3");
    }
    if (
      analytics != null && analytics.getString("licenseKey") != null &&
        !analytics.getString("licenseKey").equals("")
    ) {
      // Create a BitmovinAnalyticsConfig using your Bitmovin analytics license key and (optionally) your Bitmovin Player Key
      BitmovinAnalyticsConfig bitmovinAnalyticsConfig = new BitmovinAnalyticsConfig(analytics.getString("licenseKey"));
      bitmovinAnalyticsConfig.setVideoId(videoId);
      bitmovinAnalyticsConfig.setTitle(title);
      bitmovinAnalyticsConfig.setCustomUserId(userId);
      bitmovinAnalyticsConfig.setCdnProvider(cdnProvider);
      bitmovinAnalyticsConfig.setCustomData1(customData1);
      bitmovinAnalyticsConfig.setCustomData2(customData2);
      bitmovinAnalyticsConfig.setCustomData3(customData3);

      // Create a BitmovinPlayerCollector object using the BitmovinAnalyitcsConfig you just created
      BitmovinPlayerCollector analyticsCollector = new BitmovinPlayerCollector(bitmovinAnalyticsConfig, _reactContext);

      // Attach your player instance
      analyticsCollector.attachPlayer(_player);
    } else {
      throw new ClassCastException("Cannot connect Analytics, add you license key.");
    }
  }

  @ReactProp(name = "configuration")
  public void setConfiguration(BitmovinPlayerView view, ReadableMap config) {
    configuration = config;
    PlayerConfiguration configuration = new PlayerConfiguration();

    ReadableMap styleMap = null;
    String advisory;
    boolean hasNextEpisode;

    if (config != null && config.getString("url") != null) {
      configuration.setSourceItem(Objects.requireNonNull(config.getString("url")));

      if (config.getString("title") != null) {
        Objects.requireNonNull(configuration.getSourceItem()).setTitle(config.getString("title"));
      }

      if (config.getString("subtitle") != null) {
        Objects.requireNonNull(configuration.getSourceItem()).setDescription(config.getString("subtitle"));
      }


      if (config.getString("poster") != null) {
        Objects.requireNonNull(configuration.getSourceItem()).setPosterImage(config.getString("poster"), false);
      }
      Objects.requireNonNull(configuration.getStyleConfiguration()).setHideFirstFrame(true);

      if (config.hasKey("style")) {
        styleMap = config.getMap("style");
      }

      if (styleMap != null) {
        if (styleMap.hasKey("uiEnabled") && !styleMap.getBoolean("uiEnabled")) {
          configuration.getStyleConfiguration().setUiEnabled(false);
        }

        if (styleMap.hasKey("uiCss") && styleMap.getString("uiCss") != null) {
          configuration.getStyleConfiguration().setPlayerUiCss(styleMap.getString("uiCss"));
        }

        if (styleMap.hasKey("supplementalUiCss") && styleMap.getString("supplementalUiCss") != null) {
          configuration.getStyleConfiguration().setSupplementalPlayerUiCss(styleMap.getString("supplementalUiCss"));
        }

        if (styleMap.hasKey("uiJs") && styleMap.getString("uiJs") != null) {
          configuration.getStyleConfiguration().setPlayerUiJs(styleMap.getString("uiJs"));
        }

        if (styleMap.hasKey("fullscreenIcon") && styleMap.getBoolean("fullscreenIcon")) {
          _playerView.setFullscreenHandler(this);
        }

      }

      if (config.hasKey("startOffset")) {
        Objects.requireNonNull(configuration.getSourceConfiguration()).setStartOffset(config.getDouble("startOffset"));
      }

      hasNextEpisode = config.getBoolean("hasNextEpisode");

      if (config.getMap("advisory") != null) {
        HashMap metaDataMap = new HashMap();
        metaDataMap.put("hasNextEpisode",hasNextEpisode ? "true" : "false");
        try {
          advisory = Objects.requireNonNull(config.getMap("advisory")).toString();
          metaDataMap.put("advisory", new JSONObject(advisory).toString());
        } catch (JSONException e) {
          e.printStackTrace();
        }
        Objects.requireNonNull(configuration.getSourceItem()).setMetadata(metaDataMap);
      }

      if (config.getString("subtitles") != null) {
        Objects.requireNonNull(configuration.getSourceItem()).addSubtitleTrack(
          config.getString("subtitles"),
          null,
          "en",
          "en",
          false,
          "en"
        );
      }

      if (config.getString("thumbnails") != null) {
        Objects.requireNonNull(configuration.getSourceItem()).addThumbnailTrack(config.getString("thumbnails"));
      }

      if (config.getString("heartbeat") != null) {
        heartbeat = Integer.valueOf(config.getString("hearbeat"));
      }

      _player.setup(configuration);

    }
  }

  @Override
  public boolean isFullScreen() {
    return _fullscreen;
  }

  @Override
  public void onResume() {}

  @Override
  public void onPause() {}

  @Override
  public void onDestroy() {}

  @Override
  public void onFullscreenRequested() {
    _fullscreen = true;
  }

  @Override
  public void onFullscreenExitRequested() {
    _fullscreen = false;
  }

  @Override
  public void onHostResume() {
    _playerView.onResume();
  }

  @Override
  public void onHostPause() {
    _playerView.onPause();
  }

  @Override
  public void onHostDestroy() {
    _playerView.onDestroy();
  }

  private void setListeners() {
    _player.addEventListener((OnReadyListener) event -> {
      WritableMap map = Arguments.createMap();
      map.putString("message", "load");
      map.putString("volume", String.valueOf(_player.getVolume()));
      map.putString("duration", String.valueOf(_player.getDuration()));
      _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
        _playerView.getId(),
        "onReady",
        map);
    });
    _player.addEventListener((OnPlayListener) event -> {
      WritableMap map = Arguments.createMap();
      map.putString("message", "play");
      map.putDouble("time", event.getTime());
      map.putString("volume", String.valueOf(_player.getVolume()));
      map.putString("duration", String.valueOf(_player.getDuration()));

      _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
        _playerView.getId(),
        "onPlay",
        map);
    });
    _player.addEventListener((OnPausedListener) event -> {
      WritableMap map = Arguments.createMap();
      map.putString("message", "pause");
      map.putDouble("time", event.getTime());
      map.putString("volume", String.valueOf(_player.getVolume()));
      map.putString("duration", String.valueOf(_player.getDuration()));

      _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
        _playerView.getId(),
        "onPause",
        map);
    });
    _player.addEventListener((OnTimeChangedListener) event -> {

      // next
      if (configuration != null && configuration.hasKey("nextPlayback")) {
        if (event.getTime() <= _player.getDuration() - (configuration.getDouble("nextPlayback")) && nextCallback) {
          nextCallback = false;
        }
        if (event.getTime() > _player.getDuration() - (configuration.getDouble("nextPlayback")) && !nextCallback) {
          nextCallback = true;

          WritableMap map = Arguments.createMap();
          map.putString("message", "next");
          _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
            _playerView.getId(),
            "onEvent",
            map);
        }
      }

      // save
      if((event.getTime() > (offset + heartbeat) || event.getTime() < (offset - heartbeat)) && event.getTime() < (_player.getDuration())) {
        offset = event.getTime();
        WritableMap map = Arguments.createMap();
        map.putString("message", "save");
        map.putString("time", String.valueOf(_player.getCurrentTime()));
        map.putString("volume", String.valueOf(_player.getVolume()));
        map.putString("duration", String.valueOf(_player.getDuration()));
        _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
          _playerView.getId(),
          "onEvent",
          map);
      }

    });

    _player.addEventListener((OnStallStartedListener) event -> {
      WritableMap map = Arguments.createMap();

      _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
        _playerView.getId(),
        "onStallStarted",
        map);
    });

    _player.addEventListener((OnStallEndedListener) event -> {
      WritableMap map = Arguments.createMap();

      _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
        _playerView.getId(),
        "onStallEnded",
        map);
    });

    _player.addEventListener((OnPlaybackFinishedListener) event -> {
      WritableMap map = Arguments.createMap();

      _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
        _playerView.getId(),
        "onPlaybackFinished",
        map);
    });

    _player.addEventListener((OnRenderFirstFrameListener) event -> {
      WritableMap map = Arguments.createMap();

      _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
        _playerView.getId(),
        "onRenderFirstFrame",
        map);
    });

    _player.addEventListener((OnErrorListener) event -> {
      WritableMap map = Arguments.createMap();
      WritableMap errorMap = Arguments.createMap();

      errorMap.putInt("code", event.getCode());
      errorMap.putString("message", event.getMessage());

      map.putMap("error", errorMap);

      _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
        _playerView.getId(),
        "onError",
        map);
    });

    _player.addEventListener((OnMutedListener) event -> {
      WritableMap map = Arguments.createMap();

      _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
        _playerView.getId(),
        "onMuted",
        map);
    });

    _player.addEventListener((OnUnmutedListener) event -> {
      WritableMap map = Arguments.createMap();

      _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
        _playerView.getId(),
        "onUnmuted",
        map);
    });

    _player.addEventListener((OnSeekListener) event -> {
      WritableMap map = Arguments.createMap();
      map.putString("message", "seek");
      map.putString("time", String.valueOf(_player.getCurrentTime()));
      map.putDouble("seekTarget", event.getSeekTarget());
      map.putDouble("position", event.getPosition());
      map.putString("volume", String.valueOf(_player.getVolume()));
      map.putString("duration", String.valueOf(_player.getDuration()));
      if (customSeek) {
        customSeek = false;
      } else {
        _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
          _playerView.getId(),
          "onSeek",
          map);
      }
    });

    _player.addEventListener((OnSeekedListener) event -> {
      WritableMap map = Arguments.createMap();

      _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
        _playerView.getId(),
        "onSeeked",
        map);
    });

    _player.addEventListener((OnFullscreenEnterListener) event -> {
      WritableMap map = Arguments.createMap();

      _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
        _playerView.getId(),
        "onFullscreenEnter",
        map);
    });

    _player.addEventListener((OnFullscreenExitListener) event -> {
      WritableMap map = Arguments.createMap();

      _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
        _playerView.getId(),
        "onFullscreenExit",
        map);
      }
    );
  }
}
