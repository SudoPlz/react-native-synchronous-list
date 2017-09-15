#if __has_include(<React/RCTViewManager.h>)
#import <React/RCTViewManager.h>
#else
#import "RCTViewManager.h"
#endif

#import "RCTSynchronousList.h"

@interface RCTSynchronousListManager : RCTViewManager

@property (nonatomic, strong) RCTSynchronousList * _Nullable scrollView;

@end
