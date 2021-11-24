package com.example.takeoffmediareactnativebitmovinplayer;

import android.app.Activity;
import android.content.res.Configuration;

import androidx.annotation.Nullable;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class MainActivity extends ReactActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "ReactNativeBitmovinPlayerExample";
  }

  private void sendEvent(ReactContext reactContext,
                         String eventName,
                         @Nullable WritableMap params) {
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, params);
  }

  @Override
  public void onPictureInPictureModeChanged ( boolean isInPictureInPictureMode, Configuration newConfig) {
    Activity activity = MainActivity.this;
    MainApplication application = (MainApplication) activity.getApplication();
    ReactNativeHost reactNativeHost = application.getReactNativeHost();
    ReactInstanceManager reactInstanceManager = reactNativeHost.getReactInstanceManager();
    ReactApplicationContext reactContext = (ReactApplicationContext) reactInstanceManager.getCurrentReactContext();
    WritableMap params = Arguments.createMap();

    if (isInPictureInPictureMode) {
      params.putString("PiP_event", "EnterPiP");
      sendEvent(reactContext, "onPictureInPictureModeChanged",params);
    } else {
      params.putString("PiP_event", "ExitPiP");
      sendEvent(reactContext, "onPictureInPictureModeChanged",params);
    }
  }

}
