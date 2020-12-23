# @takeoffmedia/react-native-bitmovin-player

React native bitmovin player

## Installation

```sh
npm install @takeoffmedia/react-native-bitmovin-player
```

## Usage

```js
import ReactNativeBitmovinPlayer, {
  ReactNativeBitmovinPlayerIntance,
} from '@takeoffmedia/react-native-bitmovin-player';

// ...

<ReactNativeBitmovinPlayer
  autoPlay={false}
  filename="http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
/>


React.useEffect(() => {
  ReactNativeBitmovinPlayerIntance.multiply(3, 7).then((result) => {
    console.log({ result });
  });
}, []);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
