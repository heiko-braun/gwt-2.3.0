/*
 * Copyright 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.sample.dynatablerf.shared;

import com.google.gwt.requestfactory.shared.ProxyFor;
import com.google.gwt.requestfactory.shared.ValueProxy;
import com.google.gwt.sample.dynatablerf.domain.TimeSlot;

/**
 * TimeSlot DTO.
 */
@ProxyFor(TimeSlot.class)
public interface TimeSlotProxy extends ValueProxy {
  int getDayOfWeek();
  
  int getEndMinutes();
  
  int getStartMinutes();

  void setDayOfWeek(int zeroBasedDayOfWeek);

  void setEndMinutes(int endMinutes);
  
  void setStartMinutes(int startMinutes);
}
