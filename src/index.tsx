import React from 'react';

import {
  findNodeHandle,
  UIManager,
  NativeModules,
  requireNativeComponent,
  Platform,
  View,
} from 'react-native';

const ReactNativeBitmovinPlayerModule = NativeModules.ReactNativeBitmovinPlayer;
const EMPTY_FN = () => {};

const DEFAULT_CONFIGURATION = {
  style: {
    uiEnabled: true,
    fullscreenIcon: true,
  },
};

export type ReactNativeBitmovinPlayerMethodsType = {
  play(): void;
  pause(): void;
  destroy(): void;
  seekBackwardCommand(): void;
  seekForwardCommand(): void;
};

type ReactNativeBitmovinPlayerType = {
  autoPlay: boolean;
  hasZoom: boolean;
  style?: any;
  color?: string;
  onReady?: (event: any) => void;
  onPlay?: (event: any) => void;
  onPause?: (event: any) => void;
  onEvent?: (event: any) => void;
  onError?: (event: any) => void;
  onSeek?: (event: any) => void;
  onForward?: (event: any) => void;
  onRewind?: (event: any) => void;
  onTimeChanged?: (event: any) => void;
  onStallStarted?: (event: any) => void;
  onStallEnded?: (event: any) => void;
  onPlaybackFinished?: (event: any) => void;
  onRenderFirstFrame?: (event: any) => void;
  onPlayerError?: (event: any) => void;
  onMuted?: (event: any) => void;
  onUnmuted?: (event: any) => void;
  onSeeked?: (event: any) => void;
  onFullscreenEnter?: (event: any) => void;
  onFullscreenExit?: (event: any) => void;
  onControlsShow?: (event: any) => void;
  onControlsHide?: (event: any) => void;
  configuration: {
    url: string;
    poster?: string;
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
  analytics?: {
    videoId: string;
    title?: string;
    userId?: string;
    cdnProvider: string;
    customData1: string;
    customData2: string;
    customData3: string;
  };
};

class BitmovinPlayer extends React.Component<
  ReactNativeBitmovinPlayerType,
  {
    maxHeight: any;
  }
> {
  private _player: any = React.createRef<ReactNativeBitmovinPlayerMethodsType>();

  static defaultProps = {
    style: null,
    onReady: EMPTY_FN,
    onEvent: EMPTY_FN,
    onPlay: EMPTY_FN,
    onPause: EMPTY_FN,
    onTimeChanged: EMPTY_FN,
    onStallStarted: EMPTY_FN,
    onStallEnded: EMPTY_FN,
    onPlaybackFinished: EMPTY_FN,
    onRenderFirstFrame: EMPTY_FN,
    onPlayerError: EMPTY_FN,
    onMuted: EMPTY_FN,
    onUnmuted: EMPTY_FN,
    onSeek: EMPTY_FN,
    onSeeked: EMPTY_FN,
    onFullscreenEnter: EMPTY_FN,
    onFullscreenExit: EMPTY_FN,
    onControlsShow: EMPTY_FN,
    onControlsHide: EMPTY_FN,
    onForward: EMPTY_FN,
    onRewind: EMPTY_FN,
  };

  state = {
    maxHeight: null,
  };

  fixVideoAndroid = () => {
    // this need because video view stretched on initial render (RN 0.55.4)
    // TODO: check in future releases of RN
    UIManager.measure(findNodeHandle(this._player) as any, (_, __, ___, h) => {
      // trigger resize
      this.setState(
        {
          maxHeight: h - 1,
        },
        () => {
          requestAnimationFrame(() => {
            this.setState({
              maxHeight: h,
            });
          });
        }
      );
    });
  };

  _onReady = (event: any) => {
    const { onReady, hasZoom, autoPlay } = this.props;

    if (Platform.OS === 'android') {
      this.fixVideoAndroid();
    }

    if (hasZoom && Platform.OS === 'android') {
      this.setZoom();
    }

    if (autoPlay) {
      requestAnimationFrame(() => {
        this.play();
      });
    }

    if (onReady) {
      onReady(event);
    }
  };

  _onPlay = (event: any) => {
    const { onPlay } = this.props;

    if (Platform.OS === 'android') {
      this.fixVideoAndroid();
    }

    if (onPlay) {
      onPlay(event);
    }
  };

  play = () => {
    if (Platform.OS === 'android') {
      ReactNativeBitmovinPlayerModule.play(findNodeHandle(this._player));
    } else {
      ReactNativeBitmovinPlayerModule.play();
    }
  };

  seekBackwardCommand = () => {
    ReactNativeBitmovinPlayerModule.seekBackwardCommand();
  };

  seekForwardCommand = () => {
    ReactNativeBitmovinPlayerModule.seekForwardCommand();
  };

  destroy = () => {
    if (Platform.OS === 'android') {
      ReactNativeBitmovinPlayerModule.destroy(findNodeHandle(this._player));
    } else {
      ReactNativeBitmovinPlayerModule.destroy();
    }
  };

  setZoom = () => {
    ReactNativeBitmovinPlayerModule.setZoom(findNodeHandle(this._player));
  };

  setFit = () => {
    ReactNativeBitmovinPlayerModule.setFit(findNodeHandle(this._player));
  };

  pause = () => {
    if (Platform.OS === 'android') {
      ReactNativeBitmovinPlayerModule.pause(findNodeHandle(this._player));
    } else {
      ReactNativeBitmovinPlayerModule.pause();
    }
  };

  seek = (time = 0) => {
    const seekTime = parseFloat(time.toString());

    if (seekTime) {
      ReactNativeBitmovinPlayerModule.seek(
        findNodeHandle(this._player),
        seekTime
      );
    }
  };

  mute = () => {
    ReactNativeBitmovinPlayerModule.mute(findNodeHandle(this._player));
  };

  unmute = () => {
    ReactNativeBitmovinPlayerModule.unmute(findNodeHandle(this._player));
  };

  enterFullscreen = () => {
    ReactNativeBitmovinPlayerModule.enterFullscreen(
      findNodeHandle(this._player)
    );
  };

  exitFullscreen = () => {
    ReactNativeBitmovinPlayerModule.exitFullscreen(
      findNodeHandle(this._player)
    );
  };

  getCurrentTime = () =>
    ReactNativeBitmovinPlayerModule.getCurrentTime(
      findNodeHandle(this._player)
    );

  getDuration = () =>
    ReactNativeBitmovinPlayerModule.getDuration(findNodeHandle(this._player));

  getVolume = () =>
    ReactNativeBitmovinPlayerModule.getVolume(findNodeHandle(this._player));

  setVolume = (volume = 100) => {
    ReactNativeBitmovinPlayerModule.setVolume(
      findNodeHandle(this._player),
      volume
    );
  };

  isMuted = () =>
    ReactNativeBitmovinPlayerModule.isMuted(findNodeHandle(this._player));

  isPaused = () =>
    ReactNativeBitmovinPlayerModule.isPaused(findNodeHandle(this._player));

  isStalled = () =>
    ReactNativeBitmovinPlayerModule.isStalled(findNodeHandle(this._player));

  isPlaying = () =>
    ReactNativeBitmovinPlayerModule.isPlaying(findNodeHandle(this._player));

  _setRef = (ref: any) => {
    this._player = ref;
  };

  render() {
    const { style, configuration } = this.props;

    const { maxHeight } = this.state;

    return (
      <View style={{ flex: 1, backgroundColor: 'black' }}>
        <ReactNativeBitmovinPlayer
          {...this.props}
          ref={this._setRef}
          onReady={this._onReady}
          onPlay={this._onPlay}
          configuration={{
            ...DEFAULT_CONFIGURATION,
            ...configuration,
          }}
          style={[
            maxHeight
              ? {
                  maxHeight,
                }
              : null,
            style,
          ]}
        />
      </View>
    );
  }
}

const ReactNativeBitmovinPlayer = requireNativeComponent<ReactNativeBitmovinPlayerType>(
  'ReactNativeBitmovinPlayer'
);

export default BitmovinPlayer;
