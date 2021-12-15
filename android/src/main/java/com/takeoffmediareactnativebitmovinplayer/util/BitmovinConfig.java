package com.takeoffmediareactnativebitmovinplayer.util;

import com.bitmovin.player.api.DeviceDescription;
import com.bitmovin.player.api.LicensingConfig;
import com.bitmovin.player.api.PlaybackConfig;
import com.bitmovin.player.api.PlayerConfig;
import com.bitmovin.player.api.SeekMode;
import com.bitmovin.player.api.TweaksConfig;
import com.bitmovin.player.api.advertising.AdItem;
import com.bitmovin.player.api.advertising.AdSource;
import com.bitmovin.player.api.advertising.AdSourceType;
import com.bitmovin.player.api.advertising.AdvertisingConfig;
import com.bitmovin.player.api.buffer.BufferConfig;
import com.bitmovin.player.api.buffer.BufferMediaTypeConfig;
import com.bitmovin.player.api.casting.RemoteControlConfig;
import com.bitmovin.player.api.live.LiveConfig;
import com.bitmovin.player.api.live.LiveSynchronizationMethod;
import com.bitmovin.player.api.live.LowLatencyConfig;
import com.bitmovin.player.api.live.LowLatencySynchronizationConfig;
import com.bitmovin.player.api.live.SynchronizationConfigEntry;
import com.bitmovin.player.api.media.AdaptationConfig;
import com.bitmovin.player.api.media.MediaFilter;
import com.bitmovin.player.api.ui.ScalingMode;
import com.bitmovin.player.api.ui.StyleConfig;
import com.facebook.react.bridge.DynamicFromObject;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitmovinConfig {
  // TODO: Add support for configuration callbacks. Like the ones on NetworkConfig.
  public static PlayerConfig parsePlayerConfig(ReadableMap config) {
    String key = config.getString("key");
    final PlayerConfig playerConfig = key == null ? new PlayerConfig() : new PlayerConfig(key);
    // style
    ReadableMap style = config.getMap("style");
    playerConfig.setStyleConfig(parseStyleConfig(style));
    // playback
    ReadableMap playback = config.getMap("playback");
    playerConfig.setPlaybackConfig(parsePlaybackConfig(playback));
    // licensing
    ReadableMap licensing = config.getMap("licensing");
    playerConfig.setLicensingConfig(parseLicensingConfig(licensing));
    // advertising
    ReadableMap advertising = config.getMap("advertising");
    playerConfig.setAdvertisingConfig(parseAdvertisingConfig(advertising));
    // remote control
    ReadableMap remoteControl = config.getMap("remoteControl");
    playerConfig.setRemoteControlConfig(parseRemoteControlConfig(remoteControl));
    // adaptation
    ReadableMap adaptation = config.getMap("adaptation");
    playerConfig.setAdaptationConfig(parseAdaptationConfig(adaptation));
    // live
    ReadableMap live = config.getMap("live");
    playerConfig.setLiveConfig(parseLiveConfig(live));
    // tweaks
    ReadableMap tweaks = config.getMap("tweaks");
    playerConfig.setTweaksConfig(parseTweaksConfig(tweaks));
    // buffer
    ReadableMap buffer = config.getMap("buffer");
    playerConfig.setBufferConfig(parseBufferConfig(buffer));
    return playerConfig;
  }

  public static StyleConfig parseStyleConfig(ReadableMap style) {
    StyleConfig styleConfig = new StyleConfig();
    if (style == null) {
      return styleConfig;
    }
    if (style.hasKey("uiEnabled")) {
      styleConfig.setUiEnabled(style.getBoolean("uiEnabled"));
    }
    if (style.hasKey("playerUiCss")) {
      String playerUiCss = style.getString("playerUiCss");
      if (playerUiCss != null) {
        styleConfig.setPlayerUiCss(playerUiCss);
      }
    }
    if (style.hasKey("supplementalPlayerUiCss")) {
      styleConfig.setSupplementalPlayerUiCss(style.getString("supplementalPlayerUiCss"));
    }
    if (style.hasKey("playerUiJs")) {
      String playerUiJs = style.getString("playerUiJs");
      if (playerUiJs != null) {
        styleConfig.setPlayerUiJs(playerUiJs);
      }
    }
    if (style.hasKey("hideFirstFrame")) {
      styleConfig.setHideFirstFrame(style.getBoolean("hideFirstFrame"));
    }
    if (style.hasKey("scalingMode")) {
      String scalingMode = style.getString("scalingMode");
      if (scalingMode != null) {
        ScalingMode mode = null;
        switch (scalingMode) {
          case "fit":
            mode = ScalingMode.Fit;
            break;
          case "zoom":
            mode = ScalingMode.Zoom;
            break;
          case "stretch":
            mode = ScalingMode.Stretch;
            break;
        }
        if (mode != null) {
          styleConfig.setScalingMode(mode);
        }
      }
    }
    return styleConfig;
  }

  public static PlaybackConfig parsePlaybackConfig(ReadableMap playback) {
    PlaybackConfig playbackConfig = new PlaybackConfig();
    if (playback == null) {
      return playbackConfig;
    }
    if (playback.hasKey("autoplayEnabled")) {
      playbackConfig.setAutoplayEnabled(playback.getBoolean("autoplayEnabled"));
    }
    if (playback.hasKey("muted")) {
      playbackConfig.setMuted(playback.getBoolean("muted"));
    }
    if (playback.hasKey("timeShiftEnabled")) {
      playbackConfig.setTimeShiftEnabled(playback.getBoolean("timeShiftEnabled"));
    }
    if (playback.hasKey("tunneledPlaybackEnabled")) {
      playbackConfig.setTunneledPlaybackEnabled(playback.getBoolean("tunneledPlaybackEnabled"));
    }
    if (playback.hasKey("videoCodecPriority")) {
      List<Object> videoCodecPriority = playback.getArray("videoCodecPriority").toArrayList();
      playbackConfig.setVideoCodecPriority(mapToStrings(videoCodecPriority));
    }
    if (playback.hasKey("audioCodecPriority")) {
      List<Object> audioCodecPriority = playback.getArray("audioCodecPriority").toArrayList();
      playbackConfig.setAudioCodecPriority(mapToStrings(audioCodecPriority));
    }
    if (playback.hasKey("seekMode")) {
      String seekMode = playback.getString("seekMode");
      if (seekMode != null) {
        SeekMode mode = null;
        switch (seekMode) {
          case "exact":
            mode = SeekMode.Exact;
            break;
          case "nextSync":
            mode = SeekMode.NextSync;
            break;
          case "previousSync":
            mode = SeekMode.PreviousSync;
            break;
          case "closestSync":
            mode = SeekMode.ClosestSync;
            break;
        }
        if (mode != null) {
          playbackConfig.setSeekMode(mode);
        }
      }
    }
    if (playback.hasKey("audioFilter")) {
      String audioFilter = playback.getString("audioFilter");
      if (audioFilter != null) {
        MediaFilter filter = null;
        switch (audioFilter) {
          case "none":
            filter = MediaFilter.None;
            break;
          case "loose":
            filter = MediaFilter.Loose;
            break;
          case "strict":
            filter = MediaFilter.Strict;
            break;
        }
        if (filter != null) {
          playbackConfig.setAudioFilter(filter);
        }
      }
    }
    if (playback.hasKey("videoFilter")) {
      String videoFilter = playback.getString("videoFilter");
      if (videoFilter != null) {
        MediaFilter filter = null;
        switch (videoFilter) {
          case "none":
            filter = MediaFilter.None;
            break;
          case "loose":
            filter = MediaFilter.Loose;
            break;
          case "strict":
            filter = MediaFilter.Strict;
            break;
        }
        if (filter != null) {
          playbackConfig.setVideoFilter(filter);
        }
      }
    }
    return playbackConfig;
  }

  public static LicensingConfig parseLicensingConfig(ReadableMap licensing) {
    LicensingConfig licensingConfig = new LicensingConfig();
    if (licensing == null) {
      return licensingConfig;
    }
    if (licensing.hasKey("delay")) {
      licensingConfig.setDelay(licensing.getInt("delay"));
    }
    return licensingConfig;
  }

  public static AdvertisingConfig parseAdvertisingConfig(ReadableMap advertising) {
    AdvertisingConfig advertisingConfig = new AdvertisingConfig();
    if (advertising == null) {
      return advertisingConfig;
    }
    if (advertising.hasKey("adItems")) {
      List<Object> adItems = advertising.getArray("adItems").toArrayList();
      List<AdItem> parsedAdItems = parseAdItems(adItems);
      advertisingConfig = new AdvertisingConfig(parsedAdItems.toArray(new AdItem[0]));
    }
    return advertisingConfig;
  }

  private static List<String> mapToStrings(List<Object> list) {
    List<String> strings = new ArrayList<>();
    for (Object obj: list) {
      strings.add(obj.toString());
    }
    return strings;
  }

  private static List<AdItem> parseAdItems(List<Object> adItemsJs) {
    List<AdItem> adItems = new ArrayList<>();
    for (Object item: adItemsJs) {
      List<AdSource> adSources = null;
      String position = "pre";
      double replaceContentDuration = 0.0;

      ReadableMap adItemJs = new DynamicFromObject(item).asMap();
      if (adItemJs.hasKey("sources")) {
        ReadableArray adSourcesJs = adItemJs.getArray("sources");
        adSources = parseAdSources(adSourcesJs.toArrayList());
      }
      if (adItemJs.hasKey("position")) {
        position = adItemJs.getString("position");
      }
      if (adItemJs.hasKey("replaceContentDuration")) {
        replaceContentDuration = adItemJs.getDouble("replaceContentDuration");
      }
      if (adSources != null) {
        AdItem adItem = new AdItem(
          position,
          replaceContentDuration,
          adSources.toArray(new AdSource[0])
        );
        adItems.add(adItem);
      }
    }
    return adItems;
  }

  private static List<AdSource> parseAdSources(List<Object> adSourcesJs) {
    List<AdSource> adSources = new ArrayList<>();
    for (Object item: adSourcesJs) {
      String tag = null;
      AdSourceType type = null;

      ReadableMap adSourceJs = new DynamicFromObject(item).asMap();
      if (adSourceJs.hasKey("tag")) {
        tag = adSourceJs.getString("tag");
      }
      if (adSourceJs.hasKey("type")) {
        String typeJs = adSourceJs.getString("type");
        if (typeJs != null) {
          switch (typeJs) {
            case "ima":
              type = AdSourceType.Ima;
              break;
            case "progressive":
              type = AdSourceType.Progressive;
              break;
            case "unknown":
              type = AdSourceType.Unknown;
              break;
          }
        }
      }
      if (tag != null && type != null) {
        adSources.add(new AdSource(type, tag));
      }
    }
    return adSources;
  }

  public static RemoteControlConfig parseRemoteControlConfig(ReadableMap remoteControl) {
    RemoteControlConfig remoteControlConfig = new RemoteControlConfig();
    if (remoteControl == null) {
      return remoteControlConfig;
    }
    if (remoteControl.hasKey("castEnabled")) {
      remoteControlConfig.setCastEnabled(remoteControl.getBoolean("castEnabled"));
    }
    if (remoteControl.hasKey("sendManifestRequestsWithCredentials")) {
      remoteControlConfig.setSendManifestRequestsWithCredentials(remoteControl.getBoolean("sendManifestRequestsWithCredentials"));
    }
    if (remoteControl.hasKey("sendSegmentRequestsWithCredentials")) {
      remoteControlConfig.setSendSegmentRequestsWithCredentials(remoteControl.getBoolean("sendSegmentRequestsWithCredentials"));
    }
    if (remoteControl.hasKey("sendDrmLicenseRequestsWithCredentials")) {
      remoteControlConfig.setSendDrmLicenseRequestsWithCredentials(remoteControl.getBoolean("sendDrmLicenseRequestsWithCredentials"));
    }
    if (remoteControl.hasKey("receiverStylesheetUrl")) {
      String receiverStylesheetUrl = remoteControl.getString("receiverStylesheetUrl");
      if (receiverStylesheetUrl != null) {
        remoteControlConfig.setReceiverStylesheetUrl(receiverStylesheetUrl);
      }
    }
    if (remoteControl.hasKey("customReceiver")) {
      ReadableMap customReceiver = remoteControl.getMap("customReceiver");
      if (customReceiver != null) {
        Map<String, Object> customReceiverHash = customReceiver.toHashMap();
        Map<String, String> customReceiverConfig = new HashMap<>();
        for (String key: customReceiverHash.keySet()) {
          Object value = customReceiverHash.get(key);
          if (value != null) {
            customReceiverConfig.put(key, value.toString());
          }
        }
        remoteControlConfig.setCustomReceiverConfig(customReceiverConfig);
      }
    }
    return remoteControlConfig;
  }

  public static AdaptationConfig parseAdaptationConfig(ReadableMap adaptation) {
    AdaptationConfig adaptationConfig = new AdaptationConfig();
    if (adaptation == null) {
      return adaptationConfig;
    }
    if (adaptation.hasKey("startupBitrate")) {
      adaptationConfig.setStartupBitrate(adaptation.getInt("startupBitrate"));
    }
    if (adaptation.hasKey("maxSelectableVideoBitrate")) {
      adaptationConfig.setMaxSelectableVideoBitrate(adaptation.getInt("maxSelectableVideoBitrate"));
    }
    if (adaptation.hasKey("rebufferingAllowed")) {
      adaptationConfig.setRebufferingAllowed(adaptation.getBoolean("rebufferingAllowed"));
    }
    if (adaptation.hasKey("preload")) {
      adaptationConfig.setPreload(adaptation.getBoolean("preload"));
    }
    return adaptationConfig;
  }

  public static LiveConfig parseLiveConfig(ReadableMap live) {
    LiveConfig liveConfig = new LiveConfig();
    if (live == null) {
      return liveConfig;
    }
    if (live.hasKey("liveEdgeOffset")) {
      liveConfig.setLiveEdgeOffset(live.getDouble("liveEdgeOffset"));
    }
    if (live.hasKey("minTimeShiftBufferDepth")) {
      liveConfig.setMinTimeShiftBufferDepth(live.getDouble("minTimeShiftBufferDepth"));
    }
    if (live.hasKey("lowLatency")) {
      ReadableMap lowLatency = live.getMap("lowLatency");
      liveConfig.setLowLatencyConfig(parseLowLatencyConfig(lowLatency));
    }
    if (live.hasKey("synchronization")) {
      ReadableArray syncJs = live.getArray("synchronization");
      if (syncJs != null) {
        List<SynchronizationConfigEntry> sync = new ArrayList<>();
        for (Object obj: syncJs.toArrayList()) {
          String source = null;
          LiveSynchronizationMethod method = null;
          ReadableMap entry = new DynamicFromObject(obj).asMap();
          if (entry.hasKey("source")) {
            source = entry.getString("source");
          }
          if (entry.hasKey("method")) {
            String methodJs = entry.getString("method");
            if (methodJs != null) {
              switch (methodJs) {
                case "ntp":
                  method = LiveSynchronizationMethod.Ntp;
                  break;
              }
            }
          }
          if (source != null && method != null) {
            sync.add(new SynchronizationConfigEntry(source, method));
          }
        }
        liveConfig.setSynchronization(sync);
      }
    }
    return liveConfig;
  }

  private static LowLatencyConfig parseLowLatencyConfig(ReadableMap lowLatency) {
    LowLatencyConfig lowLatencyConfig = new LowLatencyConfig();
    if (lowLatency == null) {
      return lowLatencyConfig;
    }
    if (lowLatency.hasKey("targetLatency")) {
      lowLatencyConfig.setTargetLatency(lowLatency.getDouble("targetLatency"));
    }
    if (lowLatency.hasKey("catchup")) {
      ReadableMap catchup = lowLatency.getMap("catchup");
      lowLatencyConfig.setCatchupConfig(parseLowLatencySyncConfig(catchup));
    }
    if (lowLatency.hasKey("fallback")) {
      ReadableMap fallback = lowLatency.getMap("fallback");
      lowLatencyConfig.setFallbackConfig(parseLowLatencySyncConfig(fallback));
    }
    return lowLatencyConfig;
  }

  private static LowLatencySynchronizationConfig parseLowLatencySyncConfig(ReadableMap lowLatencySync) {
    double seekThreshold = 0;
    double playbackRate = 0;
    double playbackRateThreshold = 0;
    if (lowLatencySync.hasKey("seekThreshold")) {
      seekThreshold = lowLatencySync.getDouble("seekThreshold");
    }
    if (lowLatencySync.hasKey("playbackRate")) {
      playbackRate = lowLatencySync.getDouble("playbackRate");
    }
    if (lowLatencySync.hasKey("playbackRateThreshold")) {
      playbackRateThreshold = lowLatencySync.getDouble("playbackRateThreshold");
    }
    return new LowLatencySynchronizationConfig(
      playbackRateThreshold,
      seekThreshold,
      (float)playbackRate
    );
  }

  public static TweaksConfig parseTweaksConfig(ReadableMap tweaks) {
    TweaksConfig tweaksConfig = new TweaksConfig();
    if (tweaks == null) {
      return tweaksConfig;
    }
    if (tweaks.hasKey("timeChangedInterval")) {
      tweaksConfig.setTimeChangedInterval(tweaks.getDouble("timeChangedInterval"));
    }
    if (tweaks.hasKey("bandwidthEstimateWeightLimit")) {
      tweaksConfig.setBandwidthEstimateWeightLimit(tweaks.getInt("bandwidthEstimateWeightLimit"));
    }
    if (tweaks.hasKey("languagePropertyNormalization")) {
      tweaksConfig.setLanguagePropertyNormalization(tweaks.getBoolean("languagePropertyNormalization"));
    }
    if (tweaks.hasKey("localDynamicDashWindowUpdateInterval")) {
      tweaksConfig.setLocalDynamicDashWindowUpdateInterval(tweaks.getDouble("localDynamicDashWindowUpdateInterval"));
    }
    if (tweaks.hasKey("useFiletypeExtractorFallbackForHls")) {
      tweaksConfig.setUseFiletypeExtractorFallbackForHls(tweaks.getBoolean("useFiletypeExtractorFallbackForHls"));
    }
    if (tweaks.hasKey("useDrmSessionForClearPeriods")) {
      tweaksConfig.setUseDrmSessionForClearPeriods(tweaks.getBoolean("useDrmSessionForClearPeriods"));
    }
    if (tweaks.hasKey("useDrmSessionForClearSources")) {
      tweaksConfig.setUseDrmSessionForClearSources(tweaks.getBoolean("useDrmSessionForClearSources"));
    }
    if (tweaks.hasKey("shouldApplyTtmlRegionWorkaround")) {
      tweaksConfig.setShouldApplyTtmlRegionWorkaround(tweaks.getBoolean("shouldApplyTtmlRegionWorkaround"));
    }
    if (tweaks.hasKey("shouldEmitAllPendingMetadataOnStreamEnd")) {
      tweaksConfig.setShouldEmitAllPendingMetadataOnStreamEnd(tweaks.getBoolean("shouldEmitAllPendingMetadataOnStreamEnd"));
    }
    if (tweaks.hasKey("devicesThatRequireSurfaceWorkaround")) {
      ReadableArray devices = tweaks.getArray("devicesThatRequireSurfaceWorkaround");
      if (devices != null) {
        tweaksConfig.setDevicesThatRequireSurfaceWorkaround(parseDevices(devices.toArrayList()));
      }
    }
    return tweaksConfig;
  }

  private static List<DeviceDescription> parseDevices(List<Object> devicesJs) {
    List<DeviceDescription> devices = new ArrayList<>();
    for (Object obj: devicesJs) {
      ReadableMap deviceJS = new DynamicFromObject(obj).asMap();
      if (deviceJS.hasKey("name")) {
        String name = deviceJS.getString("name");
        if (name != null) {
          devices.add(new DeviceDescription.DeviceName(name));
        }
      }
      if (deviceJS.hasKey("model")) {
        String model = deviceJS.getString("model");
        if (model != null) {
          devices.add(new DeviceDescription.ModelName(model));
        }
      }
    }
    return devices;
  }

  public static BufferConfig parseBufferConfig(ReadableMap buffer) {
    BufferConfig bufferConfig = new BufferConfig();
    if (buffer == null) {
      return bufferConfig;
    }
    if (buffer.hasKey("startupThreshold")) {
      bufferConfig.setStartupThreshold(buffer.getDouble("startupThreshold"));
    }
    if (buffer.hasKey("restartThreshold")) {
      bufferConfig.setRestartThreshold(buffer.getDouble("restartThreshold"));
    }
    if (buffer.hasKey("audioAndVideo")) {
      ReadableMap audioAndVideo = buffer.getMap("audioAndVideo");
      if (audioAndVideo != null) {
        if (audioAndVideo.hasKey("forwardDuration")) {
          bufferConfig.setAudioAndVideo(
            new BufferMediaTypeConfig(audioAndVideo.getDouble("forwardDuration"))
          );
        }
      }
    }
    return bufferConfig;
  }
}
