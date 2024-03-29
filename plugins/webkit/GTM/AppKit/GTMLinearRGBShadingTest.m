//
//  GTMLinearRGBShadingTest.m
//
//  Copyright 2006-2008 Google Inc.
//
//  Licensed under the Apache License, Version 2.0 (the "License"); you may not
//  use this file except in compliance with the License.  You may obtain a copy
//  of the License at
// 
//  http://www.apache.org/licenses/LICENSE-2.0
// 
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
//  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
//  License for the specific language governing permissions and limitations under
//  the License.
//

#import <SenTestingKit/SenTestingKit.h>
#import "GTMSenTestCase.h"
#import "GTMLinearRGBShading.h"

@interface GTMLinearRGBShadingTest : SenTestCase
@end

@implementation GTMLinearRGBShadingTest
- (void)testShadingFrom {
  // Create a shading from red to blue, and check if 50% is purple
  NSColor *red = [NSColor redColor];
  NSColor *blue = [NSColor blueColor];
  NSColor *purple = [NSColor purpleColor];
  GTMLinearRGBShading *theShading =
    [GTMLinearRGBShading shadingFromColor:red
                                  toColor:blue
                           fromSpaceNamed:NSCalibratedRGBColorSpace];
  STAssertNotNil(theShading,nil);
  STAssertEquals([theShading stopCount], (NSUInteger)2, nil);
  CGFloat *theColor = (CGFloat*)[theShading valueAtPosition: 0.5];
  STAssertTrue(theColor[0] == [purple redComponent] &&
               theColor[1] == [purple greenComponent] &&
               theColor[2] == [purple blueComponent] &&
               theColor[3] == [purple alphaComponent], nil);
}

- (void)testShadingWith {
  // Create a shading with kColorCount colors and make sure all the values are there.
  const NSUInteger kColorCount = 100; 
  NSColor *theColors[kColorCount];
  CGFloat thePositions[kColorCount];
  const CGFloat kColorIncrement = 1.0 / kColorCount;
  for (NSUInteger i = 0; i < kColorCount; i++) {
    thePositions[i] = kColorIncrement * i;
    theColors[i] = [NSColor colorWithCalibratedRed:kColorIncrement * i 
                                             green:kColorIncrement * i 
                                              blue:kColorIncrement * i 
                                             alpha:kColorIncrement * i];
  }
  GTMLinearRGBShading *theShading =
    [GTMLinearRGBShading shadingWithColors:theColors
                            fromSpaceNamed:NSCalibratedRGBColorSpace
                               atPositions:thePositions
                                     count:kColorCount];
  for (NSUInteger i = 0; i < kColorCount; i++) {
     CGFloat *theColor = (CGFloat*)[theShading valueAtPosition: kColorIncrement * i];
    STAssertTrue(theColor[0] == kColorIncrement * i &&
                 theColor[1] == kColorIncrement * i &&
                 theColor[2] == kColorIncrement * i &&
                 theColor[3] == kColorIncrement * i, nil);
  }
}

- (void)testShadeFunction {
  GTMLinearRGBShading *theShading =
    [GTMLinearRGBShading shadingWithColors:nil
                            fromSpaceNamed:NSCalibratedRGBColorSpace
                               atPositions:nil
                                     count:0];
  CGFunctionRef theFunction = [theShading shadeFunction];
  STAssertNotNULL(theFunction, nil);
  STAssertEquals(CFGetTypeID(theFunction), CGFunctionGetTypeID(), nil);  
}

- (void)testColorSpace {
  GTMLinearRGBShading *theShading =
    [GTMLinearRGBShading shadingWithColors:nil
                            fromSpaceNamed:NSCalibratedRGBColorSpace
                               atPositions:nil
                                     count:0];
  CGColorSpaceRef theColorSpace = [theShading colorSpace];
  STAssertNotNULL(theColorSpace, nil);
  STAssertEquals(CFGetTypeID(theColorSpace), CGColorSpaceGetTypeID(), nil);
}
@end
