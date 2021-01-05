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

  // filename="https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"
  // url:
  //   'https://vod-hls-ntham-comm-live.bbccomm.s.llnwi.net/usp/auth/vod/piff_abr_full_sd/3c71d6-p04yhx89/vf_p04yhx89_b482950e-e543-41f6-bd25-5efd49756735.ism/mobile_wifi_main_sd_abr_v2_hls_master.m3u8?s=1608731574&e=1608774774&h=a263a7cc9007ce30655f86b31d9c9c9c',
  // 'https://vod-sub-ww-live.akamaized.net/iplayer/subtitles/s_od_p004/modav/p04yhx89_58915a94-38bc-4a02-b9a7-15afa95fb583.cc.xml?__gda__=1608774774_15b84f438bb50927c10fffa34045d734',
  return (
    <View style={styles.container}>
      <ReactNativeBitmovinPlayer
        autoPlay={false}
        configuration={{
          // url:
          //   'https://vod-hls-ntham-comm-live.bbccomm.s.llnwi.net/usp/auth/vod/piff_abr_full_sd/3c71d6-p04yhx89/vf_p04yhx89_b482950e-e543-41f6-bd25-5efd49756735.ism/mobile_wifi_main_sd_abr_v2_hls_master.m3u8?s=1608731574&e=1608774774&h=a263a7cc9007ce30655f86b31d9c9c9c',
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
