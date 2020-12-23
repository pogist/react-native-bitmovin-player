import { requireNativeComponent, NativeModules } from 'react-native';

type ReactNativeBitmovinPlayerType = {
  style: any;
  autoPlay: boolean;
  filename: string;
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

// export default ReactNativeBitmovinPlayer as ReactNativeBitmovinPlayerType;

const ReactNativeBitmovinPlayer = requireNativeComponent<ReactNativeBitmovinPlayerType>(
  'ReactNativeBitmovinPlayer'
);

export { ReactNativeBitmovinPlayerIntance };

export default ReactNativeBitmovinPlayer;
