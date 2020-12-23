#import <Foundation/Foundation.h>
#import "React/RCTViewManager.h"
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ReactNativeBitmovinPlayer, RCTViewManager)
  RCT_EXPORT_VIEW_PROPERTY(autoPlay, BOOL)
  RCT_EXPORT_VIEW_PROPERTY(filename, NSString)
//  RCT_EXPORT_VIEW_PROPERTY(width, CGFloat)
//  RCT_EXPORT_VIEW_PROPERTY(height, CGFloat)


RCT_EXTERN_METHOD(multiply:(float)a withB:(float)b
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(play)

@end
