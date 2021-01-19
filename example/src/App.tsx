import * as React from 'react';

import { StyleSheet, View } from 'react-native';
import ReactNativeBitmovinPlayer, {
  ReactNativeBitmovinPlayerIntance,
} from '@takeoffmedia/react-native-bitmovin-player';

export default function App() {
  React.useEffect(() => {
    ReactNativeBitmovinPlayerIntance.multiply(3, 7).then((result) => {
      console.log({ result });
    });
    // ReactNativeBitmovinPlayerIntance.play();
  }, []);

  return (
    <View style={styles.container}>
      <ReactNativeBitmovinPlayer
        autoPlay={false}
        configuration={{
          title: 'The Brown',
          subtitle: 'S1 · E1',
          startOffset: 1000,
          nextPlayback: 30,
          url:
            'https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8',
          poster:
            'https://bitmovin-a.akamaihd.net/content/MI201109210084_1/poster.jpg',
          subtitles:
            'https://bitdash-a.akamaihd.net/content/sintel/subtitles/subtitles_en.vtt',
          thumbnails:
            'https://staging-api.britbox.takeoffmedia.com/v1/thumbnail?qs=P19fZ2RhX189MTYwOTg3ODc4OV9kMzcwZjRjZjA3MWFiOTlkN2QxYWQ0ZjNjMjczOWQ1YQ==&fn=L3RodW1ibmFpbF92MS82YTBiMTYtcGlwcy1waWQtcDA4OTFmc3QvdmZfcGlwcy1waWQtcDA4OTFmc3RfdGh1bWJuYWlsX21hbmlmZXN0X2E2ZmMxNjNiLWIxNjctNDNkOS1iZjBkLWFjOWZkNTJlNjJkMC54bWw=&ch=vod-thumb-ntham-comm-live.akamaized.net',
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
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
