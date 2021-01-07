#import <Foundation/Foundation.h>
#import "React/RCTViewManager.h"
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ReactNativeBitmovinPlayer, RCTViewManager)

RCT_EXTERN_METHOD(multiply:(float)a withB:(float)b
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(play)

RCT_EXPORT_VIEW_PROPERTY(autoPlay, BOOL)
RCT_EXPORT_VIEW_PROPERTY(onLoad, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onEvent, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onPlaying, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onPause, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(configuration, NSDictionary);

@end
