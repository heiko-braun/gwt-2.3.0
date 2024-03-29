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
package com.google.gwt.requestfactory.shared.impl;

import static com.google.gwt.requestfactory.shared.impl.Constants.REQUEST_CONTEXT;
import static com.google.gwt.requestfactory.shared.impl.Constants.STABLE_ID;

import com.google.gwt.autobean.shared.AutoBean;
import com.google.gwt.autobean.shared.AutoBeanUtils;
import com.google.gwt.requestfactory.shared.BaseProxy;

/**
 * Contains behaviors common to all proxy instances.
 *
 * <p><span style='color:red'>RequestFactory has moved to
 * <code>com.google.web.bindery.requestfactory</code>.  This package will be
 * removed in a future version of GWT.</span></p>
 */
@Deprecated
public class BaseProxyCategory {
  /**
   * Sniff all return values and ensure that if the current bean is a mutable
   * EntityProxy, that its return values are mutable.
   */
  // CHECKSTYLE_OFF
  public static <T> T __intercept(AutoBean<?> bean, T returnValue) {
    // CHECKSTYLE_ON

    AbstractRequestContext context = requestContext(bean);

    /*
     * The context will be null if the bean is immutable. If the context is
     * locked, don't try to edit.
     */
    if (context == null || context.isLocked()) {
      return returnValue;
    }

    /*
     * EntityProxies need to be recorded specially by the RequestContext, so
     * delegate to the edit() method for wiring up the context.
     */
    if (returnValue instanceof BaseProxy) {
      @SuppressWarnings("unchecked")
      T toReturn = (T) context.editProxy((BaseProxy) returnValue);
      return toReturn;
    }

    if (returnValue instanceof Poser) {
      ((Poser<?>) returnValue).setFrozen(false);
    }

    /*
     * We're returning some object that's not an EntityProxy, most likely a
     * Collection type. At the very least, propagate the current RequestContext
     * so that editable chains can be constructed.
     */
    AutoBean<T> otherBean = AutoBeanUtils.getAutoBean(returnValue);
    if (otherBean != null) {
      otherBean.setTag(REQUEST_CONTEXT, bean.getTag(REQUEST_CONTEXT));
    }
    return returnValue;
  }

  public static AbstractRequestContext requestContext(AutoBean<?> bean) {
    return bean.getTag(REQUEST_CONTEXT);
  }

  public static <T extends BaseProxy> SimpleProxyId<T> stableId(
      AutoBean<? extends T> bean) {
    return bean.getTag(STABLE_ID);
  }
}
