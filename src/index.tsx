import { requireNativeComponent, NativeModules } from 'react-native';
import React from 'react';

type ReactNativeBitmovinPlayerType = {
  autoPlay: boolean;
  style?: any;
  onLoad?: (event: any) => void;
  onPlaying?: (event: any) => void;
  onPause?: (event: any) => void;
  configuration: {
    url: string;
    poster: string;
    subtitles?: string;
    thumbnails?: string;
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
  style,
  onLoad,
  onPlaying,
  onPause,
  configuration,
}: ReactNativeBitmovinPlayerType) => {
  const styles = { flex: 1, width: '100%', height: '100%' };
  return (
    <ReactNativeBitmovinPlayer
      {...{ autoPlay, onLoad, onPlaying, onPause, configuration }}
      style={[styles, style]}
    />
  );
};
