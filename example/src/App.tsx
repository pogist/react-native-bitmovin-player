import * as React from 'react';

import { Platform, StyleSheet, View } from 'react-native';
import ReactNativeBitmovinPlayer, {
  ReactNativeBitmovinPlayerIntance,
} from '@takeoffmedia/react-native-bitmovin-player';

const videoUrl = Platform.select({
  ios: 'https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8',
  android: 'https://bitdash-a.akamaihd.net/content/sintel/sintel.mpd',
  default: 'https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8',
});

export default function App() {
  React.useEffect(() => {
    ReactNativeBitmovinPlayerIntance.multiply(3, 7).then((result) => {
      console.log({ result });
    });
  }, []);

  return (
    <View style={styles.container}>
      <ReactNativeBitmovinPlayer
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
          // subtitles:
          //   'https://bitdash-a.akamaihd.net/content/sintel/subtitles/subtitles_en.vtt',
          subtitles:
            'https://staging-api.britbox.takeoffmedia.com/v1/subtitles.vtt?qs=P19fZ2RhX189MTYyNDQ5MjE4Ml8xNmM4OTIwYjI3MDNkNDcwNzBhZjk4MzgyZGMyODQzZg==&fn=L2lwbGF5ZXIvc3VidGl0bGVzL3Nfb2RfcDAwNC9tb2Rhdi9wMDd4dDdjeV9waXBzLXBpZC1wMDd4dDdjeV9jZTI1MWE4OS05ODY0LTQ3NzctODk0Yi00YTA4MWM3YjJmMDAuY2MueG1s&ch=vod-sub-ww-live.akamaized.net',
          thumbnails:
            'https://staging-api.britbox.takeoffmedia.com/v1/thumbnail?qs=P19fZ2RhX189MTYyMzE4MDE0OF82M2ZlYzQxMjFkYTViOTMxOGIxMGJmNmUzNGM0MWIwMQ==&fn=L3RodW1ibmFpbF92MS83NDhhMzktcGlwcy1waWQtcDA5NzlseXIvdmZfcGlwcy1waWQtcDA5NzlseXJfdGh1bWJuYWlsX21hbmlmZXN0X2M2NmY3ZGFhLTNhNTgtNDk4ZC1iZGM3LWIyMDQ0YzkyNTBjYi54bWw=&ch=vod-thumb-ntham-comm-live.akamaized.net',
        }}
        onLoad={() => {
          console.log({ event: 'load' });
          setTimeout(() => {
            ReactNativeBitmovinPlayerIntance.play();
          }, 500);
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
