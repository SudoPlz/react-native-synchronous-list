//
//  RepeatEdgeBinder.m
//  example
//
//  Created by John Kokkinidis on 12/09/2017.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import "RepeatEdgeBinder.h"

@implementation RepeatEdgeBinder
- (NSDictionary*) getValueForRow: (int)rowIndex andDatasource: (NSMutableArray*) data {
  if (data != nil) {
    //      NSLog(@"******* Binding childIndex %d to data row %d.", childIndex, rowIndex);
    
    if (rowIndex >= 0 && rowIndex < data.count) { // if the data index is within our data bounds
      return @{ @"item" : [data objectAtIndex:rowIndex], @"index": [NSNumber numberWithInt:rowIndex]};
    } else {
      NSNumber *newDataIndex;
      
      // find the (absolute) modulo of the row index
      int moduloRowIndex = (ABS(rowIndex) % data.count);
      if (rowIndex >= 0) { // if the row index is a positive number
        newDataIndex = [NSNumber numberWithInt:moduloRowIndex];
      } else { // else if the row index is a negative number
        // we'll need to do some more work to calculate the new index
        
        // calculate the new data index
        newDataIndex = moduloRowIndex == 0 ? // if the modulo is 0
        [NSNumber numberWithInt:moduloRowIndex] // just return the modulo
        :              // else
        [NSNumber numberWithInt:(int) data.count - moduloRowIndex];
        // we do that because we want the values to start again from the end of the array once the user reaches child 0 (a.k.a when rowIndex is negative)
      }
      //        NSLog(@"rowIndex %d was translated to %d because %d mod %lu", rowIndex, newDataIndex.intValue, rowIndex, (unsigned long)data.count);
      
      if (newDataIndex != nil) { // if we have a newDataIndex value
        // just set the view data to the value of that index
        return @{ @"item" : [data objectAtIndex:newDataIndex.intValue], @"index": [NSNumber numberWithInt:newDataIndex.intValue]};
      }
    }
  }
  return @{};
}

@end
