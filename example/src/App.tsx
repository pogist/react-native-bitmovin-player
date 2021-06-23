import * as React from 'react';

import { Platform, StyleSheet, useWindowDimensions, View } from 'react-native';
import ReactNativeBitmovinPlayer, {
  ReactNativeBitmovinPlayerIntance,
} from '@takeoffmedia/react-native-bitmovin-player';
import { useState } from 'react';

const videoUrl = Platform.select({
  ios: 'https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8',
  android: 'https://bitdash-a.akamaihd.net/content/sintel/sintel.mpd',
  default: 'https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8',
});

export default function App() {
  const [loading, setLoading] = useState(false);
  const { height } = useWindowDimensions();
  React.useEffect(() => {
    ReactNativeBitmovinPlayerIntance.multiply(3, 7).then((result) => {
      console.log({ result });
    });

    setTimeout(() => {
      setLoading(true);
    }, 2000);
    // ReactNativeBitmovinPlayerIntance.play();
  }, []);

  return (
    <View style={styles.container}>
      <ReactNativeBitmovinPlayer
        autoPlay={true}
        style={
          Platform.OS === 'android'
            ? { height: loading ? height : '100%', width: '100%', flex: 0 }
            : {}
        }
        // style={{ height: 300, width: 300, flex: 0 }}
        configuration={{
          title: 'The Brown',
          subtitle: 'S1 · E1',
          startOffset: 0,
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
            'https://staging-api.britbox.takeoffmedia.com/v1/thumbnail?qs=P19fZ2RhX189MTYyMzE4MDE0OF82M2ZlYzQxMjFkYTViOTMxOGIxMGJmNmUzNGM0MWIwMQ==&fn=L3RodW1ibmFpbF92MS83NDhhMzktcGlwcy1waWQtcDA5NzlseXIvdmZfcGlwcy1waWQtcDA5NzlseXJfdGh1bWJuYWlsX21hbmlmZXN0X2M2NmY3ZGFhLTNhNTgtNDk4ZC1iZGM3LWIyMDQ0YzkyNTBjYi54bWw=&ch=vod-thumb-ntham-comm-live.akamaized.net',
        }}
        analytics={{
          title: 'test',
        }}
        onLoad={() => {
          console.log({ event: 'load' });
        }}
        onPlaying={() => {
          console.log({ event: 'play' });
        }}
        onPause={() => {
          console.log({ event: 'pause' });
        }}
        onEvent={({ nativeEvent }) => {
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
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: '100%',
    height: '100%',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'black',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
