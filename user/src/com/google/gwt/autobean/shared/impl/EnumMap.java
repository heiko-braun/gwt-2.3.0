/*
 * Copyright 2010 Google Inc.
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
package com.google.gwt.autobean.shared.impl;

/**
 * This interface is implemented by our generated AutoBeanFactory types to
 * convert enum values to strings.
 *
 * <p><span style='color:red'>AutoBeans has moved to
 * <code>com.google.web.bindery.autobeans</code>.  This package will be
 * removed in a future version of GWT.</span></p>
 */
@Deprecated
public interface EnumMap {
  /**
   * Extra enums that should be included in the AutoBeanFactory.
   *
   * <p><span style='color:red'>AutoBeans has moved to
   * <code>com.google.web.bindery.autobeans</code>.  This package will be
   * removed in a future version of GWT.</span></p>
   */
  @Deprecated
  public @interface ExtraEnums {
    Class<? extends Enum<?>>[] value();
  }

  <E extends Enum<E>> E getEnum(Class<E> clazz, String token);

  String getToken(Enum<?> e);
}
