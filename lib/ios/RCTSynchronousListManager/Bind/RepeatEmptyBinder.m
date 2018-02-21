//
//  RepeatEmptyBinder.m
//  example
//
//  Created by John Kokkinidis on 12/09/2017.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import "RepeatEmptyBinder.h"

@implementation RepeatEmptyBinder

- (NSDictionary*) getValueForRow: (int)rowIndex andDatasource: (NSMutableArray*) data {
    if (data != nil) {
        //      NSLog(@"******* Binding childIndex %d to data row %d.", childIndex, rowIndex);
        
        if (rowIndex >= 0 && rowIndex < data.count) { // if the data index is within our data bounds
            NSMutableDictionary* rowData = [[data objectAtIndex:rowIndex] mutableCopy];
            rowData[@"index"] = [NSNumber numberWithInt:rowIndex];
            return rowData;
        }
    }
    return nil;
}

@end
