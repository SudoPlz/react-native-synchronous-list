#import "RCTSynchronousListManager.h"

@implementation RCTSynchronousListManager

RCT_EXPORT_MODULE();

- (UIView *)view
{
    _scrollView = [[RCTSynchronousList alloc] initWithBridge:self.bridge];
    //  [_scrollView setFrame:CGRectMake(0, 0, 960, 1132)];
    return _scrollView;
}

RCT_EXPORT_VIEW_PROPERTY(templateName, NSString)
RCT_EXPORT_VIEW_PROPERTY(rowHeight, float)
RCT_EXPORT_VIEW_PROPERTY(rowWidth, float)
RCT_EXPORT_VIEW_PROPERTY(numRenderRows, NSInteger)
RCT_EXPORT_VIEW_PROPERTY(loopMode, NSString)
RCT_EXPORT_VIEW_PROPERTY(data, NSArray)
RCT_EXPORT_VIEW_PROPERTY(defaultChildData, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(initialPosition, int)
RCT_EXPORT_VIEW_PROPERTY(horizontal, BOOL)
RCT_EXPORT_VIEW_PROPERTY(paging, BOOL)
RCT_EXPORT_VIEW_PROPERTY(dynamicViewSizes, BOOL)

RCT_EXPORT_METHOD(prepareRows)
{
    [_scrollView createRows];
}

RCT_EXPORT_METHOD(appendDataToDataSource: (NSArray *) newData) {
    [_scrollView appendDataToDataSource:newData];
}

RCT_EXPORT_METHOD(prependDataToDataSource: (NSArray *) newData) {
    [_scrollView prependDataToDataSource:newData];
}

RCT_EXPORT_METHOD(updateDataAtIndex: (int) rowIndex withNewData: (id) newData) {
    [_scrollView updateDataAtIndex:rowIndex withNewData:newData];
}

RCT_EXPORT_METHOD(setScrollerZoom: (float) zoomScale animated: (BOOL) animated) {
    [_scrollView setScrollerZoom:zoomScale animated:animated];
}

RCT_EXPORT_METHOD(getCurrentViewIndex:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    resolve([NSNumber numberWithInt:[_scrollView getCurrentViewIndex]]);
}



@end
