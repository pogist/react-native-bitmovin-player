/* eslint-disable react-hooks/exhaustive-deps */
import {
  requireNativeComponent,
  NativeModules,
  NativeEventEmitter,
  Platform,
} from 'react-native';
import React, { useEffect } from 'react';

type ReactNativeBitmovinPlayerType = {
  autoPlay: boolean;
  hasZoom: boolean;
  deviceZoom: boolean;
  style?: any;
  onLoad?: (event: any) => void;
  onAirPlay?: (event: any) => void;
  onPlaying?: (event: any) => void;
  onPause?: (event: any) => void;
  onEvent?: (event: any) => void;
  onError?: (event: any) => void;
  onSeek?: (event: any) => void;
  onForward?: (event: any) => void;
  onRewind?: (event: any) => void;
  configuration: {
    url: string;
    poster?: string;
    startOffset: number;
    hasNextEpisode: boolean;
    subtitles?: string;
    thumbnails?: string;
    title?: string;
    subtitle?: string;
    nextPlayback?: number;
    hearbeat?: number;
    advisory?: {
      classification: string;
      description: string;
    };
    css?: string;
    js?: string;
  };
  analytics?: {
    videoId: string;
    title: string | undefined;
    userId: string | undefined;
    cdnProvider: string;
    customData1: string;
    customData2: string;
    customData3: string;
  };
};

type ReactNativeBitmovinPlayerMethodsType = {
  ReactNativeBitmovinPlayer: {
    multiply(a: number, b: number): Promise<number>;
    play(): void;
    pause(): void;
    destroy(): void;
    seekBackwardCommand(): void;
    seekForwardCommand(): void;
  };
};

const {
  ReactNativeBitmovinPlayer: ReactNativeBitmovinPlayerIntance,
}: ReactNativeBitmovinPlayerMethodsType = NativeModules as ReactNativeBitmovinPlayerMethodsType;

const ReactNativeBitmovinPlayer = requireNativeComponent<ReactNativeBitmovinPlayerType>(
  'ReactNativeBitmovinPlayer'
);

export { ReactNativeBitmovinPlayerIntance };

const eventEmitter = new NativeEventEmitter(
  NativeModules.ReactNativeBitmovinPlayer
);

export default ({
  autoPlay,
  hasZoom,
  deviceZoom,
  style,
  onLoad,
  onAirPlay,
  onPlaying,
  onPause,
  onEvent,
  onError,
  onSeek,
  onForward,
  onRewind,
  configuration,
  analytics,
}: ReactNativeBitmovinPlayerType) => {
  const styles = { flex: 1, width: '100%', height: '100%' };

  useEffect(() => {
    if (Platform.OS === 'android') {
      eventEmitter.addListener(
        'onEvent',
        (event: any) => !!onEvent && onEvent({ nativeEvent: event })
      );
      eventEmitter.addListener(
        'onLoad',
        (event: any) => !!onLoad && onLoad({ nativeEvent: event })
      );
      eventEmitter.addListener(
        'onPlay',
        (event: any) => !!onPlaying && onPlaying({ nativeEvent: event })
      );
      eventEmitter.addListener(
        'onPause',
        (event: any) => !!onPause && onPause({ nativeEvent: event })
      );
      eventEmitter.addListener(
        'onSeek',
        (event: any) => !!onSeek && onSeek({ nativeEvent: event })
      );
      eventEmitter.addListener(
        'onForward',
        (event: any) => !!onForward && onForward({ nativeEvent: event })
      );
      eventEmitter.addListener(
        'onRewind',
        (event: any) => !!onRewind && onRewind({ nativeEvent: event })
      );
    }
    return () => {
      if (Platform.OS === 'android') {
        eventEmitter.removeAllListeners('onEvent');
        eventEmitter.removeAllListeners('onLoad');
        eventEmitter.removeAllListeners('onPlay');
        eventEmitter.removeAllListeners('onPause');
        eventEmitter.removeAllListeners('onSeek');
        eventEmitter.removeAllListeners('onForward');
        eventEmitter.removeAllListeners('onRewind');
      }
    };
  }, []);

  return (
    <ReactNativeBitmovinPlayer
      {...{
        autoPlay,
        hasZoom,
        deviceZoom,
        onLoad,
        onAirPlay,
        onPlaying,
        onPause,
        onEvent,
        onError,
        onSeek,
        onRewind,
        onForward,
        configuration,
        analytics,
      }}
      style={[styles, style]}
    />
  );
};
