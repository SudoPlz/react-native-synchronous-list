//
//  RNTableViewChildren.h
//  example
//
//  Created by Tal Kol on 6/8/16.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RCCSyncRootView.h"
#import "ScrollViewBindFactory.h"
#import "NoLoopBinder.h"
#import "RepeatEdgeBinder.h"
#import "RepeatEmptyBinder.h"

@class RCTBridge;

@interface RCTSynchronousList : UIScrollView <UIScrollViewDelegate>

- (instancetype)initWithBridge:(RCTBridge *)bridge NS_DESIGNATED_INITIALIZER;
- (void) createRows;
- (void) appendDataToDataSource: (NSArray*) newData;
- (void) prependDataToDataSource: (NSArray*) newData;
- (void) updateDataAtIndex: (int) rowIndex withNewData: (id) newData;
- (void) setScrollerZoom: (float) zoomScale animated: (BOOL) animated;
    
@property (nonatomic) float rowHeight;
@property (nonatomic) float rowWidth;
@property (nonatomic) float yeep;
@property (nonatomic) int initialPosition;
@property (nonatomic) NSInteger numRenderRows;
@property (nonatomic) NSString *loopMode;
@property (nonatomic) NSString *templateName;
@property (nonatomic) NSMutableArray *data;
@property (nonatomic) BOOL horizontal;
@property (nonatomic) BOOL paging;
@property (nonatomic) BOOL dynamicViewSizes;


@end
