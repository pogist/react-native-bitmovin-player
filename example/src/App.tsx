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
        filename="http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
        style={{ flex: 1, width: '100%', height: '100%' }}
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
