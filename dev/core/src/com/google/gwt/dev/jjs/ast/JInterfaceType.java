/*
 * Copyright 2007 Google Inc.
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
package com.google.gwt.dev.jjs.ast;

import com.google.gwt.dev.jjs.SourceInfo;

/**
 * Java interface type definition.
 */
public class JInterfaceType extends JDeclaredType {

  public JInterfaceType(SourceInfo info, String name) {
    super(info, name);
  }

  @Override
  public String getClassLiteralFactoryMethod() {
    return "Class.createForInterface";
  }

  @Override
  public JClassType getSuperClass() {
    return null;
  }

  public boolean isAbstract() {
    return true;
  }

  public boolean isFinal() {
    return false;
  }

  public void traverse(JVisitor visitor, Context ctx) {
    if (visitor.visit(this, ctx)) {
      annotations = visitor.acceptImmutable(annotations);
      fields = visitor.acceptWithInsertRemoveImmutable(fields);
      methods = visitor.acceptWithInsertRemoveImmutable(methods);
    }
    visitor.endVisit(this, ctx);
  }
}
