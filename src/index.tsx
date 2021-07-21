import React from 'react';
import PropTypes from 'prop-types';

import {
  findNodeHandle,
  UIManager,
  NativeModules,
  ViewPropTypes,
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

class BitmovinPlayer extends React.Component {
  static propTypes = {
    style: ViewPropTypes.style,
    hasZoom: PropTypes.bool,
    autoPlay: PropTypes.bool,
    configuration: PropTypes.shape({
      title: PropTypes.string,
      subtitle: PropTypes.string,
      url: PropTypes.string.isRequired,
      startOffset: PropTypes.number,
      nextPlayback: PropTypes.number,
      hasNextEpisode: PropTypes.bool,
      advisory: PropTypes.shape({
        classification: PropTypes.string,
        description: PropTypes.string,
      }),
      hearbeat: PropTypes.number,
      poster: PropTypes.string,
      subtitles: PropTypes.string,
      thumbnails: PropTypes.string,
      style: PropTypes.shape({
        uiEnabled: PropTypes.bool,
        systemUI: PropTypes.bool,
        uiCss: PropTypes.string,
        supplementalUiCss: PropTypes.string,
        uiJs: PropTypes.string,
        fullscreenIcon: PropTypes.bool,
      }),
    }).isRequired,
    analytics: PropTypes.shape({
      videoId: PropTypes.string,
      title: PropTypes.string || undefined,
      userId: PropTypes.string || undefined,
      cdnProvider: PropTypes.string,
      customData1: PropTypes.string,
      customData2: PropTypes.string,
      customData3: PropTypes.string,
      licenseKey: PropTypes.string,
    }),
    onReady: PropTypes.func,
    onEvent: PropTypes.func,
    onPlay: PropTypes.func,
    onPause: PropTypes.func,
    onTimeChanged: PropTypes.func,
    onStallStarted: PropTypes.func,
    onStallEnded: PropTypes.func,
    onPlaybackFinished: PropTypes.func,
    onRenderFirstFrame: PropTypes.func,
    onPlayerError: PropTypes.func,
    onMuted: PropTypes.func,
    onUnmuted: PropTypes.func,
    onSeek: PropTypes.func,
    onSeeked: PropTypes.func,
    onFullscreenEnter: PropTypes.func,
    onFullscreenExit: PropTypes.func,
    onControlsShow: PropTypes.func,
    onControlsHide: PropTypes.func,
    onForward: PropTypes.func,
    onRewind: PropTypes.func,
  };

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

  _onReady = () => {
    const { onReady, hasZoom, autoPlay } = this.props;

    // this need because video view stretched on initial render (RN 0.55.4)
    // TODO: check in future releases of RN
    if (Platform.OS === 'android') {
      UIManager.measure(findNodeHandle(this._player), (x, y, w, h) => {
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
    }

    if (hasZoom) {
      this.setZoom();
    }

    if (autoPlay) {
      requestAnimationFrame(() => {
        this.play();
      });
    }

    onReady();
  };

  play = () => {
    ReactNativeBitmovinPlayerModule.play(findNodeHandle(this._player));
  };

  setZoom = () => {
    ReactNativeBitmovinPlayerModule.setZoom(findNodeHandle(this._player));
  };

  pause = () => {
    ReactNativeBitmovinPlayerModule.pause(findNodeHandle(this._player));
  };

  seek = (time = 0) => {
    const seekTime = parseFloat(time);

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

  _setRef = (ref) => {
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

const ReactNativeBitmovinPlayer = requireNativeComponent(
  'ReactNativeBitmovinPlayer'
);

export default BitmovinPlayer;
