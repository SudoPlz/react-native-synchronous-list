#import <UIKit/UIKit.h>

#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#else
#import "RCTBridgeModule.h"
#endif

@interface RCCSyncRegistry : NSObject <RCTBridgeModule>

@property (nonatomic, strong) NSMutableDictionary *registry;
@property (nonatomic, strong) NSNumber *lastTag;

@end
