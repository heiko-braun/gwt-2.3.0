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

import com.google.gwt.requestfactory.server.impl.FindService;
import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

/**
 * Request selector interface for implementing a find method.
 *
 * <p><span style='color:red'>RequestFactory has moved to
 * <code>com.google.web.bindery.requestfactory</code>.  This package will be
 * removed in a future version of GWT.</span></p>
 */
@Deprecated
@Service(FindService.class)
public interface FindRequest extends RequestContext {
  /**
   * Use the implicit lookup in passing EntityProxy types to service methods.
   *
   * <p><span style='color:red'>RequestFactory has moved to
   * <code>com.google.web.bindery.requestfactory</code>.  This package will be
   * removed in a future version of GWT.</span></p>
   */
  Request<EntityProxy> find(EntityProxyId<?> proxy);
}
