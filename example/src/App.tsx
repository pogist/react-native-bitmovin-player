import * as React from 'react';
import { useEffect, useState } from 'react';
import {
  NativeEventEmitter,
  NativeModules,
  Platform,
  StyleSheet,
} from 'react-native';
import ReactNativeBitmovinPlayer, {
  ReactNativeBitmovinPlayerMethodsType,
} from '@takeoffmedia/react-native-bitmovin-player';

const videoUrl = Platform.select({
  ios: 'https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8',
  android: 'https://bitdash-a.akamaihd.net/content/sintel/sintel.mpd',
  default: 'https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8',
});

export default function App() {
  const playerRef = React.useRef<ReactNativeBitmovinPlayerMethodsType>();
  const [isInPipMode, setIsInPipMode] = useState(false);

  useEffect(() => {
    const eventEmitter = new NativeEventEmitter(NativeModules.ToastExample);
    eventEmitter.addListener('onPictureInPictureModeChanged', (event) => {
      if (event.PiP_event === 'EnterPiP') {
        setIsInPipMode(true);
        console.log('EnterPiP');
      } else if (event.PiP_event === 'ExitPiP') {
        setIsInPipMode(false);
        console.log('ExitPiP');
      }
    });

    return () => {
      eventEmitter.removeAllListeners('onPictureInPictureModeChanged');
    }
  }, []);

  return (
    <ReactNativeBitmovinPlayer
      ref={playerRef as any}
      style={styles.container}
      autoPlay
      hasZoom={false}
      inPiPMode={isInPipMode}
      configuration={{
        title: 'It works',
        subtitle: 'S1 · E1',
        startOffset: 10,
        nextPlayback: 30,
        hasNextEpisode: false,
        advisory: {
          classification: 'TV-PG',
          description: 'All Drama',
        },
        hearbeat: 10,
        url: videoUrl,
        poster:
          'https://bitmovin-a.akamaihd.net/content/MI201109210084_1/poster.jpg',
        subtitles:
          'https://bitdash-a.akamaihd.net/content/sintel/subtitles/subtitles_en.vtt',
        thumbnails:
          'https://bitdash-a.akamaihd.net/content/sintel/sprite/sprite.vtt',
      }}
      onReady={({ nativeEvent }) => {
        console.log({ nativeEvent });
      }}
      onEvent={({ nativeEvent }) => {
        console.log({ nativeEvent });
      }}
      onPause={({ nativeEvent }) => {
        console.log({ nativeEvent });
      }}
      onPlay={({ nativeEvent }) => {
        console.log({ nativeEvent });
      }}
      onSeek={({ nativeEvent }) => {
        console.log({ nativeEvent });
      }}
      onForward={({ nativeEvent }) => {
        console.log({ nativeEvent });
      }}
      onRewind={({ nativeEvent }) => {
        console.log({ nativeEvent });
      }}
    />
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'black',
  },
});
