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
package com.google.gwt.requestfactory.client;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.requestfactory.shared.RequestContext;

/**
 * Editors used with {@link RequestFactoryEditorDriver} that implement this
 * interface will be provided with the {@link RequestContext} associated with
 * the current editing session.
 *
 * <p><span style='color:red'>RequestFactory has moved to
 * <code>com.google.web.bindery.requestfactory</code>.  This package will be
 * removed in a future version of GWT.</span></p>
 *
 * @param <T> the type of data being edited
 */
@Deprecated
public interface HasRequestContext<T> extends Editor<T> {
  /**
   * Called by {@link RequestFactoryEditorDriver} with the
   * {@link RequestContext} passed into
   * {@link RequestFactoryEditorDriver#edit(Object, RequestContext) edit()} or
   * {@code null} if {@link RequestFactoryEditorDriver#display(Object)
   * display()} is called.
   *
   * @param the RequestContext associated with the current editing session which
   *          may be {@code null}
   */
  void setRequestContext(RequestContext ctx);
}
