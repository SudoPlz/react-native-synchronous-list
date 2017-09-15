//
//  RNTableViewChildren.m
//  example
//
//  Created by Tal Kol on 6/8/16.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "RCTSynchronousList.h"
#import "RCTConvert.h"
#import "RCTEventDispatcher.h"
#import "RCTUtils.h"
#import "UIView+React.h"

#define EMPTY_ROW_ID -1
#define LOOP_MODE_NONE @"no-loop"
#define LOOP_MODE_REPEAT_EMPTY @"repeat-empty"
#define LOOP_MODE_REPEAT_EDGE @"repeat-edge"
#define RECENTER_PERCENTAGE 0.25

@interface RCTSynchronousList()
@end

@implementation RCTSynchronousList
@synthesize data;

#pragma mark - init

RCTBridge *_bridge;
RCTEventDispatcher *_eventDispatcher;
NSMutableArray *_renderRows;
int _firstRenderRow;
float _firstRenderRowOffset;
int _firstRowIndex;
const int ROW_BUFFER = 2;
float _contentOffsetShift;
BOOL rowsAreCreated = NO;
int createdRowCnt = 0;
ScrollViewBindFactory* bindFactory;

- (instancetype)initWithBridge:(RCTBridge *)bridge {
  RCTAssertParam(bridge);
  //  NSLog(@"****** initWithBridge BEGAN");
  if ((self = [super initWithFrame:CGRectZero])) {
    _eventDispatcher = bridge.eventDispatcher;
    
    _bridge = bridge;
    while ([_bridge respondsToSelector:NSSelectorFromString(@"parentBridge")]
           && [_bridge valueForKey:@"parentBridge"]) {
      _bridge = [_bridge valueForKey:@"parentBridge"];
    }
    
    _renderRows = [NSMutableArray array];
    _firstRenderRow = 0;
    _firstRenderRowOffset = 0;
    _firstRowIndex = 0;
    _contentOffsetShift = 0;
    _initialPosition = 0;
    _horizontal = NO;
    _paging = NO;
    _templateName = @"RNSynchronousListRowTemplate";
//    self.pagingEnabled = _paging;
    

    //    emptyRowView = [[RCCSyncRootView alloc] initWithBridge:_bridge moduleName:@"RNSynchronousListRowTemplate" initialProperties:@{}];
    //    emptyRowView.isEmptyView = YES;
    
    self.delegate = self;
    self.backgroundColor = [UIColor whiteColor];
    self.showsVerticalScrollIndicator = YES; // TODO change that to NO in time
    self.showsHorizontalScrollIndicator = YES; // TODO change that to NO in time
    self.loopMode = LOOP_MODE_NONE;
    bindFactory = [[NoLoopBinder alloc] init];
    //    NSLog(@"****** initWithBridge ENDED");
  }
  
  return self;
}

RCT_NOT_IMPLEMENTED(-initWithFrame:(CGRect)frame)
RCT_NOT_IMPLEMENTED(-initWithCoder:(NSCoder *)aDecoder)

- (void)insertReactSubview:(UIView *)subview atIndex:(NSInteger)atIndex
{
  [super insertReactSubview: subview atIndex:atIndex];
  // we comment the following lines out bc we don't want to add any views from jsx
  
  //  [_renderRows addObject:subview];
  //  [self insertSubview:subview atIndex:atIndex];
  //  [self bind:subview atIndex:(int)atIndex toRowIndex:(int)atIndex];
  // instead all of that logic will take place in createRows
}

#pragma mark - inner methods


- (float) calculateViewSizeFromIndex: (int) fromIndex toIndex: (int) toIndex {
  if (fromIndex > toIndex) {
    RCTLogError(@"RNInfiniteScrollView: toIndex should be greater than fromIndex.");
  }
//  NSLog(@" About to find size between %d to %d", fromIndex, toIndex);
  float totalViewSize = 0;
  for (int i = fromIndex; i < toIndex; i++ ) {
    if (i >= 0 && i < data.count) {
      NSDictionary* curRowDt = data[i];
      if (_horizontal) { // horizontal mode
        totalViewSize += [curRowDt[@"width"] floatValue];
      } else { // vertical mode
        totalViewSize += [curRowDt[@"height"] floatValue];
      }
    } else {
      totalViewSize += _horizontal ? self.rowWidth : self.rowHeight;
    }
  }
  return totalViewSize;
}


- (float) calculateViewSizeForRowCnt: (int) rowCnt {
  if (_horizontal) { // horizontal mode
    return self.rowWidth * rowCnt;
  } else { // vertical mode
    return self.rowHeight * rowCnt;
    
  }
  return 0;
}


              

- (void)recenterIfNecessary
{
  CGPoint currentOffset = [self contentOffset]; // cur scroll values
  if (_horizontal) { // horizontal mode
    CGFloat contentWidth = [self contentSize].width;
    CGFloat centerOffsetX = (contentWidth - [self bounds].size.width) / 2.0; // find the center Y point
    CGFloat distanceFromCenter = fabs(currentOffset.x - centerOffsetX); // find the distance of the center Y
    if (rowsAreCreated == YES // if the rows have been created
        && _renderRows.count > 0 // and we got renderRows
        && distanceFromCenter > (contentWidth * RECENTER_PERCENTAGE)) // and we have scrolled more than 25% ahead
    {
      //      NSLog(@" Must recenter because distance from center (%f) is more than 25% (%f), contentWidth=%f", distanceFromCenter, contentWidth * RECENTER_PERCENTAGE, contentWidth);
      [self recenterTo: CGPointMake(centerOffsetX, 0)];
    }
  } else { // vertical mode
    CGFloat contentHeight = [self contentSize].height;
    CGFloat centerOffsetY = (contentHeight - [self bounds].size.height) / 2.0; // find the center Y point
    CGFloat distanceFromCenter = fabs(currentOffset.y - centerOffsetY); // find the distance of the center Y
    if (rowsAreCreated == YES // if the rows have been created
        && _renderRows.count > 0 // and we got renderRows
        && distanceFromCenter > (contentHeight * RECENTER_PERCENTAGE)) // and we have scrolled more than 25% ahead
    {
      [self recenterTo: CGPointMake(0, centerOffsetY)];
    }
  }
}

- (void) recenterTo: (CGPoint) recenterPoint withNewBindingsStartingFrom: (NSNumber*) bindStart {
  CGPoint currentOffset = [self contentOffset]; // cur scroll values
  if (_horizontal) { // horizontal mode
//    NSLog(@" Now recentering to x: %f, y: %f", recenterPoint.x, currentOffset.y);
    
    // setting the Y value to be equal to the center Y point
    self.contentOffset = CGPointMake(recenterPoint.x, currentOffset.y);

    // move content by the same amount so it appears to stay still
    int i = 0;
    for (RCCSyncRootView *view in _renderRows) {
      CGPoint center = view.center;
      center.x += (recenterPoint.x - currentOffset.x);
      view.center = center;
      if (bindStart != nil) {
        [self bindView:view toRowIndex:(int)(bindStart.intValue + i)];
      }
      i++;
    }
    _contentOffsetShift += (recenterPoint.x - currentOffset.x);
  } else { // vertical mode
//    NSLog(@" Now recentering to x: %f, y: %f", currentOffset.x, recenterPoint.y);
    
    // setting the Y value to be equal to the center Y point
    self.contentOffset = CGPointMake(currentOffset.x, recenterPoint.y);

    // move content by the same amount so it appears to stay still
    int i = 0;
    for (RCCSyncRootView *view in _renderRows) {
      CGPoint center = view.center;
      center.y += (recenterPoint.y - currentOffset.y);
      view.center = center;
      if (bindStart != nil) {
        [self bindView:view toRowIndex:(int)(bindStart.intValue + i)];
      }
      i++;
    }
    _contentOffsetShift += (recenterPoint.y - currentOffset.y);
  }
}

- (void) recenterTo: (CGPoint) recenterPoint {
  [self recenterTo: recenterPoint withNewBindingsStartingFrom:nil];
}


- (void) swapViewsIfNecessary {
  CGPoint currentOffset = [self contentOffset];
  
  if (_horizontal) { // horizontal mode
    double curXValue = currentOffset.x - _contentOffsetShift;
    double furthestPointRight = _firstRenderRowOffset
    + (self.dynamicViewSizes ?
       [self calculateViewSizeFromIndex:_firstRowIndex toIndex:(_firstRowIndex + (int) self.numRenderRows - ROW_BUFFER)]
       :
       [self calculateViewSizeForRowCnt:((int) self.numRenderRows - ROW_BUFFER)]);
    /* the furthest point right is
     _firstRenderRowOffset (where we started rendering)
     + the width of all the rows minus the ROW_BUFFER (rows outside the screen)
     */
    if (curXValue + self.frame.size.width > furthestPointRight) {
//      NSLog(@"curXValue: %f plus frame width %f > furthestPointRight %f", curXValue, self.frame.size.width, furthestPointRight);
      [self moveFirstRenderRowToEnd];
    }
    
    double furthestPointLeft = _firstRenderRowOffset
    + (self.dynamicViewSizes ?
       [self calculateViewSizeFromIndex:_firstRowIndex toIndex:(_firstRowIndex + ROW_BUFFER)]
       :
       [self calculateViewSizeForRowCnt:ROW_BUFFER]);
//    NSLog(@"furthestPointLeft: %f plus 2 more rows (%f) ", furthestPointLeft, (self.rowWidth * ROW_BUFFER));
    /* the furthest point left is
     _firstRenderRowOffset (where we started rendering)
     + the height (or width) of the ROW_BUFFER rows (rows outside the screen)
     */
    
    if (curXValue < furthestPointLeft) {
//      NSLog(@"cur X: %f, < furthestPointLeft %f ? ", curXValue, furthestPointLeft);
      [self moveLastRenderRowToBeginning];
    }
  } else { // vertical mode
    double curYValue = currentOffset.y - _contentOffsetShift;
    double furthestPointBottom = _firstRenderRowOffset
    + (self.dynamicViewSizes ?
       [self calculateViewSizeFromIndex:_firstRowIndex toIndex:(_firstRowIndex + (int) self.numRenderRows - ROW_BUFFER)]
       :
       [self calculateViewSizeForRowCnt:((int) self.numRenderRows - ROW_BUFFER)]);
    /* the furthest point bottom is
     _firstRenderRowOffset (where we started rendering)
     + the height of all the rows minus the ROW_BUFFER (rows outside the screen)
     */
    if (curYValue + self.frame.size.height > furthestPointBottom) {
      [self moveFirstRenderRowToEnd];
    }
    
    double furthestPointTop = _firstRenderRowOffset
    + (self.dynamicViewSizes ?
       [self calculateViewSizeFromIndex:_firstRowIndex toIndex:(_firstRowIndex + ROW_BUFFER)]
       :
       [self calculateViewSizeForRowCnt:ROW_BUFFER]);
    /* the furthest point ahead is
     _firstRenderRowOffset (where we started rendering)
     + the height of the ROW_BUFFER rows (rows outside the screen)
     */
    
    if (curYValue < furthestPointTop) {
      [self moveLastRenderRowToBeginning];
    }
  }
}

- (void)moveFirstRenderRowToEnd {
  //  NSLog(@" abt to moveFirstRenderRowToEnd");
  if (rowsAreCreated == YES && self.numRenderRows > 0 && [_renderRows count] > 0) {
        NSLog(@"************* moveFirstRenderRowToEnd");
    RCCSyncRootView *view = _renderRows[_firstRenderRow];
    CGPoint center = view.center;
    
    float newViewOffset = 0;
    if (self.dynamicViewSizes) { // if the sizing calculation is dynamic
      // calculate and add all the view heights
      newViewOffset = [self calculateViewSizeFromIndex:_firstRowIndex toIndex:(_firstRowIndex + (int) self.numRenderRows)];
      
      // and add 1 row height to the _firstRenderRowOffset
      _firstRenderRowOffset += [self calculateViewSizeFromIndex:_firstRowIndex toIndex:(_firstRowIndex + 1)];
      
    } else { // else if the sizing is static
      
      // calculate and add all the view heights
      newViewOffset = [self calculateViewSizeForRowCnt: (int) self.numRenderRows];
      
      // and add 1 row height to the _firstRenderRowOffset
      _firstRenderRowOffset += [self calculateViewSizeForRowCnt:1];
    }

    if (_horizontal) { // horizontal mode
      center.x += newViewOffset;
    } else {
      center.y += newViewOffset;
    }
    view.center = center;
    
    _firstRenderRow = (_firstRenderRow + 1) % self.numRenderRows;
    _firstRowIndex += 1;
    [self bindView:view toRowIndex:(int)(_firstRowIndex + self.numRenderRows)];
  }
}

- (void)moveLastRenderRowToBeginning {
  //  NSLog(@" abt to moveLastRenderRowToBeginning");
  if (rowsAreCreated == YES && self.numRenderRows > 0 && [_renderRows count] > 0) {
        NSLog(@"******* moveLastRenderRowToBeginning");
    int _lastRenderRow = (_firstRenderRow + self.numRenderRows - 1) % (int)self.numRenderRows;
    RCCSyncRootView *view = _renderRows[_lastRenderRow];
    CGPoint center = view.center;
    

    float newViewOffset = 0;
    if (self.dynamicViewSizes) { // if the sizing calculation is dynamic
      // calculate and add all the view heights
      newViewOffset = [self calculateViewSizeFromIndex:_firstRowIndex - 1 toIndex:(_firstRowIndex + (int) self.numRenderRows - 1)];
      
      // and add 1 row height to the _firstRenderRowOffset
      _firstRenderRowOffset -= [self calculateViewSizeFromIndex:_firstRowIndex - 1 toIndex:_firstRowIndex];
      
    } else { // else if the sizing is static
      
      // calculate and add all the view heights
      newViewOffset = [self calculateViewSizeForRowCnt: (int) self.numRenderRows];
      
      // and add 1 row height to the _firstRenderRowOffset
      _firstRenderRowOffset -= [self calculateViewSizeForRowCnt:1];
    }
    
    if (_horizontal) { // horizontal mode
      center.x -= newViewOffset;
    } else {
      center.y -= newViewOffset;
    }
    
    view.center = center;
    [self bindView:view toRowIndex:(_firstRowIndex - 1)];
    _firstRenderRow = _lastRenderRow;
    _firstRowIndex -= 1;
    NSLog(@"Moved LAST child to  beggining, on %f (%f), so the first render row is now %d", center.x, _firstRenderRowOffset, _firstRowIndex);
  }
}

//- (void)bindViewAtIndex:(int)childIndex toRowIndex:(int)rowIndex
//{
//  RCCSyncRootView *curRowView = _renderRows[childIndex];
//  NSDictionary* newDt = [bindFactory getValueForRow:rowIndex andDatasource:data];
//  if (newDt) {
//curRowView.boundToIndex = rowIndex;
//    [curRowView updateProps:newDt];
//  }
//}

- (void)bindView:(RCCSyncRootView *)child toRowIndex:(int)rowIndex
{
  if (child.boundToIndex != rowIndex || rowIndex == 0) {
//    NSLog(@"Now requesting to bind row index %d", rowIndex);
    NSDictionary* newDt = [bindFactory getValueForRow:rowIndex andDatasource:data];
    if (newDt) {
      NSLog(@"updating child %d w/  DATA %@", rowIndex, newDt);
      child.boundToIndex = rowIndex;
      [child updateProps:newDt];
    }
  }
}

#pragma mark - exposed methods


- (void) createRows {
  //  NSLog(@"**** NO of rows: %ld", self.numRenderRows);
  //  NSLog(@" loop? %@", self.loopMode);
  rowsAreCreated = NO;
  createdRowCnt = 0;
  
  for (int i = 0; i < self.numRenderRows; i++)
  {
    //    NSLog(@"%d", i);
    dispatch_async(dispatch_get_main_queue(), ^
                   {
                     id curRowValue;
                     if (data != nil && [data count] > i) {
                       curRowValue = [data objectAtIndex:i];
                     }
                     
                     RCCSyncRootView *rootView = [[RCCSyncRootView alloc] initWithBridge:_bridge moduleName:_templateName initialProperties:curRowValue ? @{ @"item" : curRowValue, @"index": [NSNumber numberWithInt:i] } : @{ @"index": [NSNumber numberWithInt:i]}];
                     rootView.boundToIndex = i;
                     CGPoint center = rootView.center;
                     
                     float newViewOffset = 0;
                     if (self.dynamicViewSizes) { // if the sizing calculation is dynamic
                       // calculate and add all the view heights
                       newViewOffset = [self calculateViewSizeFromIndex:0 toIndex:i];
                     } else { // else if the sizing is static
                       
                       // calculate and add all the view heights
                       newViewOffset = (i * [self calculateViewSizeForRowCnt: 1]);
                     }

                     if (_horizontal) { // horizontal mode
                       center.x = newViewOffset;
                     } else { // vertical mode
                       center.y = newViewOffset;
                     }
//                     NSLog(@"******* ITEM AT %d, will be placed that at x: %f, y: %f", i, center.x, center.y);
                     
                     rootView.center = center;
                     rootView.backgroundColor = [UIColor yellowColor];
                     [_renderRows addObject:rootView];
                     [self insertSubview:rootView atIndex:i];
                     createdRowCnt ++;
                     //                       NSLog(@" Created row %d out of %ld", createdRowCnt, (long)self.numRenderRows);
                     if (createdRowCnt == self.numRenderRows) {
                       NSLog(@" @@@@@@ ROWS CREATED");
                       rowsAreCreated = YES;
                       if (_horizontal) { // horizontal mode
                         if (self.rowWidth <= 0) {
                           RCTLogError(@"RNInfiniteScrollView: We need a rowWidth greater than zero for horizontal mode. Cur value: %f", self.rowWidth);
                           
                         }
                         if (self.rowHeight > 0) {
                           NSString *warnMsg = @"RNInfiniteScrollView: You don't have to specify the rowHeight on horizontal mode";
                           RCTLogWarn(@"%@", warnMsg);
                           NSLog(@"%@", warnMsg);
                         }
                         float totalContentWidth = self.dynamicViewSizes ?
                            [self calculateViewSizeFromIndex:0 toIndex:((int) data.count - 1)]
                            :
                            [self calculateViewSizeForRowCnt:((int) data.count - 1)];

                         self.contentSize = CGSizeMake(totalContentWidth, self.frame.size.height);
                       } else { // vertical mode
                         if (self.rowHeight <= 0) {
                           RCTLogError(@"RNInfiniteScrollView: We need a rowHeight greater than zero for horizontal={false}. Cur value: %f", self.rowHeight);
                           
                         }
                         if (self.rowWidth > 0) {
                           NSString *warnMsg = @"RNInfiniteScrollView: You don't really have to specify the rowWidth on horizontal={false} mode";
                           RCTLogWarn(@"%@", warnMsg);
                           NSLog(@"%@", warnMsg);
                         }
                         float totalContentHeight = self.dynamicViewSizes ?
                            [self calculateViewSizeFromIndex:0 toIndex:((int) data.count - 1)]
                            :
                            [self calculateViewSizeForRowCnt:((int) data.count - 1)];
                         self.contentSize = CGSizeMake(self.frame.size.width, totalContentHeight);
                       }
                       
                       if (_initialPosition != 0) {
                         [self scrollToItemWithIndex:_initialPosition animated:NO];
                       }
                     }
                   });
  }
}

- (void) appendDataToDataSource: (NSArray*) newData {
  if (_firstRowIndex + self.numRenderRows > data.count) { // if we have children rendered above the data count limit
    // we have to update the data props for those children
    for (RCCSyncRootView *view in _renderRows) {
      int viewBindIndex = view.boundToIndex;
      int rowsAfterViewBindIndex = viewBindIndex - (int) data.count;
      if (rowsAfterViewBindIndex >= 0
          && rowsAfterViewBindIndex < newData.count) {
        [view updateProps:@{ @"item": newData[rowsAfterViewBindIndex], @"index": [NSNumber numberWithInt:viewBindIndex]}];
      }
    }
  }
  
  // then insert the new data in the end of our datasource
  [data addObjectsFromArray:newData];
}


- (void) prependDataToDataSource: (NSArray*) newData {
  if (_firstRowIndex < 0) { // if we have children rendered below the current data count
    // we have to update the data props for those children
    for (RCCSyncRootView *view in _renderRows) {
      int viewBindIndex = view.boundToIndex;
      if (viewBindIndex < 0 && fabs(viewBindIndex) <= newData.count) {
//        NSLog(@"Now translating data index: %d to newData index: %d", viewBindIndex, (int) newData.count + viewBindIndex);
        [view updateProps:@{ @"item": newData[newData.count + viewBindIndex ], @"index": [NSNumber numberWithInt:viewBindIndex] }];
      }
    }
    _firstRowIndex += newData.count;
  }
  
  // then insert the new data in the beggining of our datasource
  NSIndexSet *indexes = [NSIndexSet indexSetWithIndexesInRange:
                         NSMakeRange(0,[newData count])];
  [data insertObjects:newData atIndexes:indexes];
  
//  NSLog(@"### The datasource is now: %@", data);
}

- (void) updateDataAtIndex: (int) rowIndex withNewData: (id) newData {
  if (rowIndex > 0 && rowIndex < data.count) { // if the rowIndex is within our data range
    [data replaceObjectAtIndex:rowIndex withObject:newData];
  }
  
   // if the row index is
  if (rowIndex > _firstRowIndex // above the first rendered index
      && rowIndex < _firstRowIndex + self.numRenderRows) { // and below the last rendered index
    // that means we have to update the view containing that data as well

    for (RCCSyncRootView *view in _renderRows) {
      int viewBindIndex = view.boundToIndex;
      if (viewBindIndex == rowIndex) {
//        NSLog(@"Now replacing data at index: %d w/ newData %@", viewBindIndex, newData);
        [view updateProps:@{ @"item": newData, @"index": [NSNumber numberWithInt:viewBindIndex] }];
      }
    }
  }
}

- (void) setScrollerZoom: (float) zoomScale animated: (BOOL) animated {
//  NSLog(@" Now zooming to %f", zoomScale);
//  [self setZoomScale:zoomScale animated:animated];
//  dispatch_async(dispatch_get_main_queue(), ^ {
//    RCCSyncRootView* curView = [self getCurrentView];
//    curView.backgroundColor = [UIColor greenColor];
//    NSLog(@"cur view bound to %d", curView.boundToIndex);
//  });
}

- (void) scrollToItemWithIndex: (int) itemIndex animated: (BOOL) animated {
  float newOffset;
  
  if (self.dynamicViewSizes) {
    if (itemIndex > 0) { // if item index positive
      newOffset = [self calculateViewSizeFromIndex:0 toIndex:itemIndex];
    } else {
      newOffset = [self calculateViewSizeFromIndex:itemIndex toIndex:0];
    }
  } else {
    newOffset = [self calculateViewSizeForRowCnt:itemIndex];
  }
  
  
  
  if (_horizontal) { // horizontal mode
    
    if (animated == NO) {
      CGFloat contentWidth = [self contentSize].width;
      CGFloat centerOffsetX = (contentWidth - [self bounds].size.width) / 2.0; // find the center Y point
      
      [self recenterTo: CGPointMake(centerOffsetX, 0) withNewBindingsStartingFrom:[NSNumber numberWithInt:itemIndex]];
      _firstRenderRowOffset = 0;
      _firstRenderRow = 0;
      _firstRowIndex = itemIndex;
    } else {
      [self setContentOffset: CGPointMake(newOffset, 0) animated:YES];
    }
  } else { // vertical mode
    
    if (animated == NO) {
      CGFloat contentHeight = [self contentSize].height;
      CGFloat centerOffsetY = (contentHeight - [self bounds].size.height) / 2.0; // find the center Y point
      
      [self recenterTo: CGPointMake(0, centerOffsetY) withNewBindingsStartingFrom:[NSNumber numberWithInt:itemIndex]];
      _firstRenderRowOffset = 0;
      _firstRenderRow = 0;
      _firstRowIndex = itemIndex;
    } else {
      [self setContentOffset: CGPointMake(0, newOffset) animated:YES];
    }
  }
}

- (int) getCurrentViewIndex {
  CGPoint currentOffset = [self contentOffset];
  float scrollPerc;
  if (_horizontal == NO) { // vertical mode
    scrollPerc = currentOffset.x / self.contentSize.width;
  } else { // horizontal mode
    scrollPerc = currentOffset.y / self.contentSize.height;
  }
  
  return (int) scrollPerc * (int) data.count;
  
}

- (int) getCurrentViewExpressedInRenderViewIndex {
  int curViewIndex = [self getCurrentViewIndex];
  int viewIndexDiffFromFirst = curViewIndex - _firstRowIndex;
  int renderedViewIndex = (_firstRenderRow + viewIndexDiffFromFirst) % self.numRenderRows;
//  NSLog(@"renderedViewIndex %d", renderedViewIndex);
  return renderedViewIndex;
}

- (RCCSyncRootView*) getCurrentView {
  int renderedViewIndex = [self getCurrentViewExpressedInRenderViewIndex];
  if (renderedViewIndex >= 0 && renderedViewIndex < _renderRows.count) {
    return _renderRows[renderedViewIndex];
  }
  return nil;
}




#pragma mark - UIScrollViewDelegate callbacks

- (UIView*) viewForZoomingInScrollView:(UIScrollView *)scrollView {
  return nil;
}

- (void)layoutSubviews {
  [super layoutSubviews];
//  NSLog(@"_firstRenderRowOffset: %f, _firstRenderRow: %d, _firstRowIndex: %d", _firstRenderRowOffset, _firstRenderRow, _firstRowIndex);
  if (![self.loopMode isEqualToString:LOOP_MODE_NONE]) {
    [self recenterIfNecessary];
  }
  [self swapViewsIfNecessary];
}


//-(void) scrollViewDidScroll:(UIScrollView *)scrollView {
//  NSLog(@"scrollViewDidScroll");
//}

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView {
  //  NSLog(@"scrollViewWillBeginDragging");
}

-(void)scrollViewWillEndDragging:(UIScrollView *)scrollView withVelocity:(CGPoint)velocity targetContentOffset:(inout CGPoint *)targetContentOffset {
  //  NSLog(@"scrollViewWillEndDragging");
}


#pragma mark - Setter functions

//- (void) setRowHeight:(float)rowHeight{
//  _rowHeight = rowHeight;
//  NSLog(@"####################### rowHeight was SET");
//}

- (void) setLoopMode:(NSString *)loopMode {
  _loopMode = loopMode;
  if ([loopMode isEqualToString:LOOP_MODE_REPEAT_EMPTY]) {
    bindFactory = [[RepeatEmptyBinder alloc] init];
  } else if ([loopMode isEqualToString:LOOP_MODE_REPEAT_EDGE]) {
    bindFactory = [[RepeatEdgeBinder alloc] init];
  } else {
    bindFactory = [[NoLoopBinder alloc] init];
  }
}

- (void) setData:(NSArray *) newData {
  data = [newData mutableCopy];
}

- (void) setHorizontal:(BOOL)horizontal {
  _horizontal = horizontal;
}

- (void) setPaging:(BOOL)paging {
  _paging = paging;
//  self.pagingEnabled = _paging;
}

@end
       
/*
 _firstRenderRowOffset: -5460
 _firstRenderRow: 6,
 _firstRowIndex: -42
 */
