package com.takeoffmediareactnativebitmovinplayer.view;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bitmovin.player.PlayerView;
import com.bitmovin.player.api.Player;
import com.bitmovin.player.api.PlayerConfig;
import com.bitmovin.player.api.source.SourceConfig;
import com.bitmovin.player.api.source.SourceType;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.takeoffmediareactnativebitmovinplayer.util.BitmovinConfig;

/**
 * Exposes a native bitmovin Player View as a React Native component.
 */
public class RCTBitmovinPlayerView extends SimpleViewManager<PlayerView> {
  private ReactApplicationContext reactContext;
  public static final String REACT_CLASS = "RCTBitmovinPlayerView";

  public RCTBitmovinPlayerView(ReactApplicationContext reactContext) {
    this.reactContext = reactContext;
  }

  @NonNull
  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @NonNull
  @Override
  protected PlayerView createViewInstance(@NonNull ThemedReactContext reactContext) {
    return new PlayerView(reactContext);
  }

  @ReactProp(name = "config")
  public void setConfig(PlayerView view, @Nullable ReadableMap config) {
    try {
      final PlayerConfig playerConfig = BitmovinConfig.parsePlayerConfig(config);
      view.setPlayer(Player.create(reactContext, playerConfig));
    } catch (Exception e) {
      Log.e(REACT_CLASS, Log.getStackTraceString(e));
    }
  }

  @ReactProp(name = "source")
  public void setSource(PlayerView view, @Nullable ReadableMap source) {
    try {
      SourceType sourceType = null;
      String sourceTypeJs = source.getString("type");
      if (sourceTypeJs != null) {
        switch (sourceTypeJs) {
          case "dash":
            sourceType = SourceType.Dash;
            break;
          case "hls":
            sourceType = SourceType.Hls;
            break;
          case "smooth":
            sourceType = SourceType.Smooth;
            break;
          case "progressive":
            sourceType = SourceType.Progressive;
            break;
        }
      }
      String url = source.getString("url");
      if (url != null && sourceType != null) {
        view.getPlayer().load(new SourceConfig(url, sourceType));
      }
    } catch (Exception e) {
      Log.e(REACT_CLASS, Log.getStackTraceString(e));
    }
  }
}
