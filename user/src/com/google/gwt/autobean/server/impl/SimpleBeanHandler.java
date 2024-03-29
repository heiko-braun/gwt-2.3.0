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
package com.google.gwt.autobean.server.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Dynamic implementation of an AutoBean's simple peer object.
 *
 * <p><span style='color:red'>AutoBeans has moved to
 * <code>com.google.web.bindery.autobeans</code>.  This package will be
 * removed in a future version of GWT.</span></p>
 *
 * @param <T> the type of interface the shim allows access to
 */
@Deprecated
class SimpleBeanHandler<T> implements InvocationHandler {
  private final ProxyAutoBean<T> bean;

  public SimpleBeanHandler(ProxyAutoBean<T> bean) {
    this.bean = bean;
  }

  public ProxyAutoBean<T> getBean() {
    return bean;
  }

  /**
   * Delegates most work to {@link BeanMethod}.
   */
  public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
    for (BeanMethod type : BeanMethod.values()) {
      if (type.matches(this, method)) {
        Object toReturn = type.invoke(this, method, args);
        return toReturn;
      }
    }
    throw new RuntimeException("Unhandled invocation " + method.getName());
  }

  /**
   * For debugging use only.
   */
  @Override
  public String toString() {
    return bean.getPropertyMap().toString();
  }
}
