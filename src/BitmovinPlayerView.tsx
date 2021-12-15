import { requireNativeComponent } from 'react-native';

type AdSource = {
  tag: string;
  type: 'ima' | 'unknown' | 'progressive';
};

type AdItem = {
  sources: AdSource[];
  position: string;
  replaceContentDuration: number;
};

export interface AdvertisingConfig {
  adItems: AdItem[];
}

export interface AdaptationConfig {
  startupBitrate?: number;
  maxSelectableVideoBitrate?: number;
  rebufferingAllowed?: boolean;
  preload?: boolean;
}

export interface BufferConfig {
  audioAndVideo?: {
    forwardDuration: number;
  };
  startupThreshold?: number;
  restartThreshold?: number;
}

export interface LicensingConfig {
  delay?: number;
}

export interface LowLatencySynchronizationConfig {
  seekThreshold: number;
  playbackRate: number;
  playbackRateThreshold: number;
}

export interface LowLatencyConfig {
  targetLatency?: number;
  catchup?: LowLatencySynchronizationConfig;
  fallback?: LowLatencySynchronizationConfig;
}

export interface LiveConfig {
  lowLatency?: LowLatencyConfig;
  liveEdgeOffset?: number;
  minTimeShiftBufferDepth?: number;
  synchronization?: {
    source: string;
    method: 'ntp';
  }[];
}

type MediaFilter = 'none' | 'loose' | 'strict';

export interface PlaybackConfig {
  muted?: boolean;
  autoplayEnabled?: boolean;
  timeShiftEnabled?: boolean;
  tunneledPlaybackEnabled?: boolean;
  videoCodecPriority?: string[];
  audioCodecPriority?: string[];
  seekMode?: 'exact' | 'nextSync' | 'previousSync' | 'closestSync';
  audioFilter?: MediaFilter;
  videoFilter?: MediaFilter;
}

export interface RemoteControlConfig {
  customReceiver?: Record<string, string>;
  receiverStylesheetUrl?: string;
  castEnabled?: boolean;
  sendSegmentRequestsWithCredentials?: boolean;
  sendManifestRequestsWithCredentials?: boolean;
  sendDrmLicenseRequestsWithCredentials?: boolean;
}

export interface StyleConfig {
  uiEnabled?: boolean;
  playerUiCss?: string;
  supplementalPlayerUiCss?: string;
  playerUiJs?: string;
  hideFirstFrame?: boolean;
  scalingMode?: 'fit' | 'zoom' | 'stretch';
}

export interface TweaksConfig {
  timeChangedInterval?: number;
  bandwidthEstimateWeightLimit?: number;
  languagePropertyNormalization?: boolean;
  localDynamicDashWindowUpdateInterval?: number;
  useFiletypeExtractorFallbackForHls?: boolean;
  useDrmSessionForClearPeriods?: boolean;
  useDrmSessionForClearSources?: boolean;
  shouldApplyTtmlRegionWorkaround?: boolean;
  shouldEmitAllPendingMetadataOnStreamEnd?: boolean;
  devicesThatRequireSurfaceWorkaround?: (
    | { model: string }
    | { device: string }
  )[];
}

export interface PlayerConfig {
  adaptation?: AdaptationConfig;
  advertising?: AdvertisingConfig;
  buffer?: BufferConfig;
  licensing?: LicensingConfig;
  live?: LiveConfig;
  playback?: PlaybackConfig;
  remoteControl?: RemoteControlConfig;
  style?: StyleConfig;
  tweaks?: TweaksConfig;
}

export interface SourceConfig {
  url: string;
  type: 'dash' | 'hls' | 'smooth' | 'progressive';
}

export interface BitmovinPlayerProps {
  config: PlayerConfig;
  source: SourceConfig;
}

export default requireNativeComponent<BitmovinPlayerProps>(
  'RCTBitmovinPlayerView'
);
