
#import <UIKit/UIKit.h>

#if __has_include(<React/RCTRootView.h>)
#import <React/RCTRootView.h>
#else
#import "RCTRootView.h"
#endif

@interface RCCSyncRootView : RCTRootView

- (void)updateProps:(NSDictionary *)newProps;

@property (nonatomic) int boundToIndex;

@end
