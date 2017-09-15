//
//  ScrollViewBindFactory.h
//  example
//
//  Created by John Kokkinidis on 12/09/2017.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ScrollViewBindFactory : NSObject

- (NSDictionary*) getValueForRow: (int)rowIndex andDatasource: (NSMutableArray*) data;

@end
