/*
 * Copyright 2008 Google Inc.
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
package com.google.gwt.dev.js.ast;

import com.google.gwt.dev.jjs.CorrelationFactory;
import com.google.gwt.dev.jjs.CorrelationFactory.DummyCorrelationFactory;
import com.google.gwt.dev.jjs.SourceInfo;
import com.google.gwt.dev.jjs.SourceOrigin;

import java.util.HashMap;
import java.util.Map;

/**
 * A JavaScript program.
 */
public final class JsProgram extends JsNode {

  private final CorrelationFactory correlator;

  private JsProgramFragment[] fragments;

  private final Map<String, JsFunction> indexedFunctions = new HashMap<String, JsFunction>();

  private final JsScope objectScope;

  private final JsScope topScope;

  public JsProgram() {
    this(DummyCorrelationFactory.INSTANCE);
  }

  /**
   * Constructs a JavaScript program object.
   * 
   * @param soycEnabled Controls whether or not SourceInfo nodes created via the
   *          JsProgram will record descendant information. Enabling this
   *          feature will collect extra data during the compilation cycle, but
   *          at a cost of memory and object allocations.
   */
  public JsProgram(CorrelationFactory correlator) {
    super(correlator.makeSourceInfo(SourceOrigin.create(0,
        JsProgram.class.getName())));

    this.correlator = correlator;

    topScope = new JsNormalScope(JsRootScope.INSTANCE, "Global");
    objectScope = new JsNormalScope(JsRootScope.INSTANCE, "Object");
    setFragmentCount(1);
  }

  public SourceInfo createSourceInfo(int startPos, int endPos, int startLine,
      String fileName) {
    return correlator.makeSourceInfo(SourceOrigin.create(startPos, endPos,
        startLine, fileName));
  }

  public SourceInfo createSourceInfo(int lineNumber, String location) {
    return correlator.makeSourceInfo(SourceOrigin.create(lineNumber, location));
  }

  public SourceInfo createSourceInfoSynthetic(Class<?> caller) {
    return createSourceInfo(0, caller.getName());
  }

  public JsBlock getFragmentBlock(int fragment) {
    if (fragment < 0 || fragment >= fragments.length) {
      throw new IllegalArgumentException("Invalid fragment: " + fragment);
    }
    return fragments[fragment].getGlobalBlock();
  }

  public int getFragmentCount() {
    return this.fragments.length;
  }

  /**
   * Gets the one and only global block.
   */
  public JsBlock getGlobalBlock() {
    return getFragmentBlock(0);
  }

  public JsFunction getIndexedFunction(String name) {
    return indexedFunctions.get(name);
  }

  public JsScope getObjectScope() {
    return objectScope;
  }

  /**
   * Gets the top level scope. This is the scope of all the statements in the
   * main program.
   */
  public JsScope getScope() {
    return topScope;
  }

  public void setFragmentCount(int fragments) {
    this.fragments = new JsProgramFragment[fragments];
    for (int i = 0; i < fragments; i++) {
      this.fragments[i] = new JsProgramFragment(
          createSourceInfoSynthetic(JsProgram.class));
    }
  }

  public void setIndexedFunctions(Map<String, JsFunction> indexedFunctions) {
    this.indexedFunctions.clear();
    this.indexedFunctions.putAll(indexedFunctions);
  }

  public void traverse(JsVisitor v, JsContext ctx) {
    if (v.visit(this, ctx)) {
      for (JsProgramFragment fragment : fragments) {
        v.accept(fragment);
      }
    }
    v.endVisit(this, ctx);
  }
}
