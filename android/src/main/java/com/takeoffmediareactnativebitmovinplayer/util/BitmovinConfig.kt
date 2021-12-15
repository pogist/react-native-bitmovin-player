package com.takeoffmediareactnativebitmovinplayer.util

import com.facebook.react.bridge.ReadableMap
import com.bitmovin.player.api.PlayerConfig
import com.bitmovin.player.api.ui.StyleConfig
import com.bitmovin.player.api.ui.ScalingMode
import com.bitmovin.player.api.PlaybackConfig
import com.bitmovin.player.api.SeekMode
import com.bitmovin.player.api.media.MediaFilter
import com.bitmovin.player.api.LicensingConfig
import com.bitmovin.player.api.advertising.AdvertisingConfig
import com.bitmovin.player.api.advertising.AdItem
import com.bitmovin.player.api.advertising.AdSource
import com.facebook.react.bridge.DynamicFromObject
import com.bitmovin.player.api.advertising.AdSourceType
import com.bitmovin.player.api.casting.RemoteControlConfig
import com.bitmovin.player.api.media.AdaptationConfig
import com.bitmovin.player.api.live.LiveConfig
import com.bitmovin.player.api.live.SynchronizationConfigEntry
import com.bitmovin.player.api.live.LiveSynchronizationMethod
import com.bitmovin.player.api.live.LowLatencyConfig
import com.bitmovin.player.api.live.LowLatencySynchronizationConfig
import com.bitmovin.player.api.TweaksConfig
import com.bitmovin.player.api.DeviceDescription
import com.bitmovin.player.api.buffer.BufferConfig
import com.bitmovin.player.api.buffer.BufferMediaTypeConfig

object BitmovinConfig {
  // TODO: Add support for configuration callbacks. Like the ones on NetworkConfig.
  @JvmStatic
  fun parsePlayerConfig(config: ReadableMap): PlayerConfig {
    val key = config.getString("key")
    val playerConfig = key?.let { PlayerConfig(it) } ?: PlayerConfig()
    playerConfig.styleConfig = parseStyleConfig(
      config.getMap("style")
    )
    playerConfig.playbackConfig = parsePlaybackConfig(
      config.getMap("playback")
    )
    playerConfig.licensingConfig = parseLicensingConfig(
      config.getMap("licensing")
    )
    playerConfig.advertisingConfig = parseAdvertisingConfig(
      config.getMap("advertising")
    )
    playerConfig.remoteControlConfig = parseRemoteControlConfig(
      config.getMap("remoteControl")
    )
    playerConfig.adaptationConfig = parseAdaptationConfig(
      config.getMap("adaptation")
    )
    playerConfig.liveConfig = parseLiveConfig(
      config.getMap("live")
    )
    playerConfig.tweaksConfig = parseTweaksConfig(
      config.getMap("tweaks")
    )
    playerConfig.bufferConfig = parseBufferConfig(
      config.getMap("buffer")
    )
    return playerConfig
  }

  private fun parseStyleConfig(style: ReadableMap?): StyleConfig {
    val styleConfig = StyleConfig()
    if (style == null) {
      return styleConfig
    }
    if (style.hasKey("uiEnabled")) {
      styleConfig.isUiEnabled = style.getBoolean("uiEnabled")
    }
    if (style.hasKey("playerUiCss")) {
      val playerUiCss = style.getString("playerUiCss")
      if (playerUiCss != null) {
        styleConfig.playerUiCss = playerUiCss
      }
    }
    if (style.hasKey("supplementalPlayerUiCss")) {
      styleConfig.supplementalPlayerUiCss = style.getString("supplementalPlayerUiCss")
    }
    if (style.hasKey("playerUiJs")) {
      val playerUiJs = style.getString("playerUiJs")
      if (playerUiJs != null) {
        styleConfig.playerUiJs = playerUiJs
      }
    }
    if (style.hasKey("hideFirstFrame")) {
      styleConfig.isHideFirstFrame = style.getBoolean("hideFirstFrame")
    }
    if (style.hasKey("scalingMode")) {
      val scalingMode = style.getString("scalingMode")?.let {
        when (it) {
          "fit" -> ScalingMode.Fit
          "zoom" -> ScalingMode.Zoom
          "stretch" -> ScalingMode.Stretch
          else -> null
        }
      }
      if (scalingMode != null) {
        styleConfig.scalingMode = scalingMode
      }
    }
    return styleConfig
  }

  private fun parsePlaybackConfig(playback: ReadableMap?): PlaybackConfig {
    val playbackConfig = PlaybackConfig()
    if (playback == null) {
      return playbackConfig
    }
    if (playback.hasKey("autoplayEnabled")) {
      playbackConfig.isAutoplayEnabled = playback.getBoolean("autoplayEnabled")
    }
    if (playback.hasKey("muted")) {
      playbackConfig.isMuted = playback.getBoolean("muted")
    }
    if (playback.hasKey("timeShiftEnabled")) {
      playbackConfig.isTimeShiftEnabled = playback.getBoolean("timeShiftEnabled")
    }
    if (playback.hasKey("tunneledPlaybackEnabled")) {
      playbackConfig.isTunneledPlaybackEnabled = playback.getBoolean("tunneledPlaybackEnabled")
    }
    if (playback.hasKey("videoCodecPriority")) {
      val videoCodecPriority = playback.getArray("videoCodecPriority")?.toArrayList()?.map {
        it.toString()
      }
      if (videoCodecPriority != null) {
        playbackConfig.videoCodecPriority = videoCodecPriority
      }
    }
    if (playback.hasKey("audioCodecPriority")) {
      val audioCodecPriority = playback.getArray("audioCodecPriority")?.toArrayList()?.map {
        it.toString()
      }
      if (audioCodecPriority != null) {
        playbackConfig.audioCodecPriority = audioCodecPriority
      }
    }
    if (playback.hasKey("seekMode")) {
      val seekMode = playback.getString("seekMode")?.let {
        when (it) {
          "exact" -> SeekMode.Exact
          "nextSync" -> SeekMode.NextSync
          "previousSync" -> SeekMode.PreviousSync
          "closestSync" -> SeekMode.ClosestSync
          else -> null
        }
      }
      if (seekMode != null) {
        playbackConfig.seekMode = seekMode
      }
    }
    if (playback.hasKey("audioFilter")) {
      val audioFilter = playback.getString("audioFilter")?.let { asMediaFilter(it) }
      if (audioFilter != null) {
        playbackConfig.audioFilter = audioFilter
      }
    }
    if (playback.hasKey("videoFilter")) {
      val videoFilter = playback.getString("videoFilter")?.let { asMediaFilter(it) }
      if (videoFilter != null) {
        playbackConfig.videoFilter = videoFilter
      }
    }
    return playbackConfig
  }

  private fun asMediaFilter(value: String): MediaFilter? =
    when (value) {
      "none" -> MediaFilter.None
      "loose" -> MediaFilter.Loose
      "strict" -> MediaFilter.Strict
      else -> null
    }

  private fun parseLicensingConfig(licensing: ReadableMap?): LicensingConfig {
    val licensingConfig = LicensingConfig()
    if (licensing == null) {
      return licensingConfig
    }
    if (licensing.hasKey("delay")) {
      licensingConfig.delay = licensing.getInt("delay")
    }
    return licensingConfig
  }

  private fun parseAdvertisingConfig(advertising: ReadableMap?): AdvertisingConfig {
    var advertisingConfig = AdvertisingConfig()
    if (advertising == null) {
      return advertisingConfig
    }
    if (advertising.hasKey("adItems")) {
      val adItems = advertising.getArray("adItems")?.toArrayList()?.mapNotNull {
        parseAdItem(it)
      }
      if (adItems != null) {
        advertisingConfig = AdvertisingConfig(adItems)
      }
    }
    return advertisingConfig
  }

  private fun parseAdItem(obj: Any): AdItem? {
    var position: String? = null
    var replaceContentDuration: Double? = null
    var sources: List<AdSource>? = null
    val adItemMap = DynamicFromObject(obj).asMap()
    if (adItemMap.hasKey("position")) {
      position = adItemMap.getString("position")
    }
    if (adItemMap.hasKey("replaceContentDuration")) {
      replaceContentDuration = adItemMap.getDouble("replaceContentDuration")
    }
    if (adItemMap.hasKey("sources")) {
      sources = adItemMap.getArray("sources")?.toArrayList()?.mapNotNull {
        parseAdSource(it)
      }
    }
    if (position != null && replaceContentDuration != null && sources != null) {
      return AdItem(position, replaceContentDuration, *sources.toTypedArray())
    }
    return null
  }

  private fun parseAdSource(obj: Any): AdSource? {
    var tag: String? = null
    var type: AdSourceType? = null
    val adSourceMap = DynamicFromObject(obj).asMap()
    if (adSourceMap.hasKey("tag")) {
      tag = adSourceMap.getString("tag")
    }
    if (adSourceMap.hasKey("type")) {
      type = adSourceMap.getString("type")?.let {
        when (it) {
          "ima" -> AdSourceType.Ima
          "unknown" -> AdSourceType.Unknown
          "progressive" -> AdSourceType.Progressive
          else -> null
        }
      }
    }
    if (type != null && tag != null) {
      return AdSource(type, tag)
    }
    return null
  }

  private fun parseRemoteControlConfig(remoteControl: ReadableMap?): RemoteControlConfig {
    val remoteControlConfig = RemoteControlConfig()
    if (remoteControl == null) {
      return remoteControlConfig
    }
    if (remoteControl.hasKey("castEnabled")) {
      remoteControlConfig.isCastEnabled = remoteControl.getBoolean("castEnabled")
    }
    if (remoteControl.hasKey("sendManifestRequestsWithCredentials")) {
      remoteControlConfig.sendManifestRequestsWithCredentials = remoteControl.getBoolean("sendManifestRequestsWithCredentials")
    }
    if (remoteControl.hasKey("sendSegmentRequestsWithCredentials")) {
      remoteControlConfig.sendSegmentRequestsWithCredentials = remoteControl.getBoolean("sendSegmentRequestsWithCredentials")
    }
    if (remoteControl.hasKey("sendDrmLicenseRequestsWithCredentials")) {
      remoteControlConfig.sendDrmLicenseRequestsWithCredentials = remoteControl.getBoolean("sendDrmLicenseRequestsWithCredentials")
    }
    if (remoteControl.hasKey("receiverStylesheetUrl")) {
      remoteControlConfig.receiverStylesheetUrl = remoteControl.getString("receiverStylesheetUrl")
    }
    if (remoteControl.hasKey("customReceiver")) {
      val customReceiver = remoteControl.getMap("customReceiver")?.toHashMap()?.mapValues {
        it.value.toString()
      }
      if (customReceiver != null) {
        remoteControlConfig.customReceiverConfig = customReceiver
      }
    }
    return remoteControlConfig
  }

  private fun parseAdaptationConfig(adaptation: ReadableMap?): AdaptationConfig {
    val adaptationConfig = AdaptationConfig()
    if (adaptation == null) {
      return adaptationConfig
    }
    if (adaptation.hasKey("startupBitrate")) {
      adaptationConfig.startupBitrate = adaptation.getInt("startupBitrate")
    }
    if (adaptation.hasKey("maxSelectableVideoBitrate")) {
      adaptationConfig.maxSelectableVideoBitrate = adaptation.getInt("maxSelectableVideoBitrate")
    }
    if (adaptation.hasKey("rebufferingAllowed")) {
      adaptationConfig.isRebufferingAllowed = adaptation.getBoolean("rebufferingAllowed")
    }
    if (adaptation.hasKey("preload")) {
      adaptationConfig.preload = adaptation.getBoolean("preload")
    }
    return adaptationConfig
  }

  private fun parseLiveConfig(live: ReadableMap?): LiveConfig {
    val liveConfig = LiveConfig()
    if (live == null) {
      return liveConfig
    }
    if (live.hasKey("liveEdgeOffset")) {
      liveConfig.liveEdgeOffset = live.getDouble("liveEdgeOffset")
    }
    if (live.hasKey("minTimeShiftBufferDepth")) {
      liveConfig.minTimeShiftBufferDepth = live.getDouble("minTimeShiftBufferDepth")
    }
    if (live.hasKey("lowLatency")) {
      val lowLatency = live.getMap("lowLatency")?.let {
        parseLowLatencyConfig(it)
      }
      if (lowLatency != null) {
        liveConfig.lowLatencyConfig = lowLatency
      }
    }
    if (live.hasKey("synchronization")) {
      val sync = live.getArray("synchronization")?.toArrayList()?.mapNotNull {
        parseSyncConfig(it)
      }
      if (sync != null) {
        liveConfig.synchronization = sync
      }
    }
    return liveConfig
  }

  private fun parseSyncConfig(obj: Any): SynchronizationConfigEntry? {
    var source: String? = null
    var method: LiveSynchronizationMethod? = null
    val sync = DynamicFromObject(obj).asMap()
    if (sync.hasKey("source")) {
      source = sync.getString("source")
    }
    if (sync.hasKey("method")) {
      method = sync.getString("method")?.let {
        when (it) {
          "ntp" -> LiveSynchronizationMethod.Ntp
          else -> null
        }
      }
    }
    if (source != null && method != null) {
      return SynchronizationConfigEntry(source, method)
    }
    return null
  }

  private fun parseLowLatencyConfig(lowLatency: ReadableMap): LowLatencyConfig {
    val lowLatencyConfig = LowLatencyConfig()
    if (lowLatency.hasKey("targetLatency")) {
      lowLatencyConfig.targetLatency = lowLatency.getDouble("targetLatency")
    }
    if (lowLatency.hasKey("catchup")) {
      val catchup = lowLatency.getMap("catchup")?.let {
        parseLowLatencySyncConfig(it)
      }
      if (catchup != null) {
        lowLatencyConfig.catchupConfig = catchup
      }
    }
    if (lowLatency.hasKey("fallback")) {
      val fallback = lowLatency.getMap("fallback")?.let {
        parseLowLatencySyncConfig(it)
      }
      if (fallback != null) {
        lowLatencyConfig.fallbackConfig = fallback
      }
    }
    return lowLatencyConfig
  }

  private fun parseLowLatencySyncConfig(lowLatencySync: ReadableMap): LowLatencySynchronizationConfig? {
    var playbackRateThreshold: Double? = null
    var seekThreshold: Double? = null
    var playbackRate: Double? = null
    if (lowLatencySync.hasKey("playbackRateThreshold")) {
      playbackRateThreshold = lowLatencySync.getDouble("playbackRateThreshold")
    }
    if (lowLatencySync.hasKey("seekThreshold")) {
      seekThreshold = lowLatencySync.getDouble("seekThreshold")
    }
    if (lowLatencySync.hasKey("playbackRate")) {
      playbackRate = lowLatencySync.getDouble("playbackRate")
    }
    if (playbackRateThreshold != null && seekThreshold != null && playbackRate != null) {
      return LowLatencySynchronizationConfig(
        playbackRateThreshold,
        seekThreshold,
        playbackRate.toFloat()
      )
    }
    return null
  }

  private fun parseTweaksConfig(tweaks: ReadableMap?): TweaksConfig {
    val tweaksConfig = TweaksConfig()
    if (tweaks == null) {
      return tweaksConfig
    }
    if (tweaks.hasKey("timeChangedInterval")) {
      tweaksConfig.timeChangedInterval = tweaks.getDouble("timeChangedInterval")
    }
    if (tweaks.hasKey("bandwidthEstimateWeightLimit")) {
      tweaksConfig.bandwidthEstimateWeightLimit = tweaks.getInt("bandwidthEstimateWeightLimit")
    }
    if (tweaks.hasKey("languagePropertyNormalization")) {
      tweaksConfig.languagePropertyNormalization = tweaks.getBoolean("languagePropertyNormalization")
    }
    if (tweaks.hasKey("localDynamicDashWindowUpdateInterval")) {
      tweaksConfig.localDynamicDashWindowUpdateInterval = tweaks.getDouble("localDynamicDashWindowUpdateInterval")
    }
    if (tweaks.hasKey("useFiletypeExtractorFallbackForHls")) {
      tweaksConfig.useFiletypeExtractorFallbackForHls = tweaks.getBoolean("useFiletypeExtractorFallbackForHls")
    }
    if (tweaks.hasKey("useDrmSessionForClearPeriods")) {
      tweaksConfig.useDrmSessionForClearPeriods = tweaks.getBoolean("useDrmSessionForClearPeriods")
    }
    if (tweaks.hasKey("useDrmSessionForClearSources")) {
      tweaksConfig.useDrmSessionForClearSources = tweaks.getBoolean("useDrmSessionForClearSources")
    }
    if (tweaks.hasKey("shouldApplyTtmlRegionWorkaround")) {
      tweaksConfig.shouldApplyTtmlRegionWorkaround = tweaks.getBoolean("shouldApplyTtmlRegionWorkaround")
    }
    if (tweaks.hasKey("shouldEmitAllPendingMetadataOnStreamEnd")) {
      tweaksConfig.shouldEmitAllPendingMetadataOnStreamEnd = tweaks.getBoolean("shouldEmitAllPendingMetadataOnStreamEnd")
    }
    if (tweaks.hasKey("devicesThatRequireSurfaceWorkaround")) {
      val devices = tweaks.getArray("devicesThatRequireSurfaceWorkaround")?.toArrayList()?.mapNotNull {
        parseDevice(it)
      }
      if (devices != null) {
        tweaksConfig.devicesThatRequireSurfaceWorkaround = devices
      }
    }
    return tweaksConfig
  }

  private fun parseDevice(obj: Any): DeviceDescription? {
    val device = DynamicFromObject(obj).asMap()
    if (device.hasKey("name")) {
      return device.getString("name")?.let {
        DeviceDescription.DeviceName(it)
      }
    }
    if (device.hasKey("model")) {
      return device.getString("model")?.let {
        DeviceDescription.ModelName(it)
      }
    }
    return null
  }

  private fun parseBufferConfig(buffer: ReadableMap?): BufferConfig {
    val bufferConfig = BufferConfig()
    if (buffer == null) {
      return bufferConfig
    }
    if (buffer.hasKey("startupThreshold")) {
      bufferConfig.startupThreshold = buffer.getDouble("startupThreshold")
    }
    if (buffer.hasKey("restartThreshold")) {
      bufferConfig.restartThreshold = buffer.getDouble("restartThreshold")
    }
    if (buffer.hasKey("audioAndVideo")) {
      val audioAndVideo: BufferMediaTypeConfig? = buffer.getMap("audioAndVideo")?.let {
        if (it.hasKey("forwardDuration")) {
          BufferMediaTypeConfig(it.getDouble("forwardDuration"))
        }
        null
      }
      if (audioAndVideo != null) {
        bufferConfig.audioAndVideo = audioAndVideo
      }
    }
    return bufferConfig
  }
}
