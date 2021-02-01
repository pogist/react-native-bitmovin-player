import { requireNativeComponent, NativeModules } from 'react-native';
import React from 'react';

type ReactNativeBitmovinPlayerType = {
  autoPlay: boolean;
  hasZoom: boolean;
  style?: any;
  onLoad?: (event: any) => void;
  onPlaying?: (event: any) => void;
  onPause?: (event: any) => void;
  onEvent?: (event: any) => void;
  onSeek?: (event: any) => void;
  onForward?: (event: any) => void;
  onRewind?: (event: any) => void;
  configuration: {
    url: string;
    poster: string;
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
  };
};

type ReactNativeBitmovinPlayerMethodsType = {
  ReactNativeBitmovinPlayer: {
    multiply(a: number, b: number): Promise<number>;
    play(): void;
  };
};

const {
  ReactNativeBitmovinPlayer: ReactNativeBitmovinPlayerIntance,
}: ReactNativeBitmovinPlayerMethodsType = NativeModules as ReactNativeBitmovinPlayerMethodsType;

const ReactNativeBitmovinPlayer = requireNativeComponent<ReactNativeBitmovinPlayerType>(
  'ReactNativeBitmovinPlayer'
);

export { ReactNativeBitmovinPlayerIntance };

export default ({
  autoPlay,
  hasZoom,
  style,
  onLoad,
  onPlaying,
  onPause,
  onEvent,
  onSeek,
  onForward,
  onRewind,
  configuration,
}: ReactNativeBitmovinPlayerType) => {
  const styles = { flex: 1, width: '100%', height: '100%' };
  return (
    <ReactNativeBitmovinPlayer
      {...{
        autoPlay,
        hasZoom,
        onLoad,
        onPlaying,
        onPause,
        onEvent,
        onSeek,
        onRewind,
        onForward,
        configuration,
      }}
      style={[styles, style]}
    />
  );
};
