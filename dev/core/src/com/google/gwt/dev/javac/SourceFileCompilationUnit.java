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
package com.google.gwt.dev.javac;

import com.google.gwt.dev.jjs.ast.JDeclaredType;
import com.google.gwt.dev.resource.Resource;

import org.eclipse.jdt.core.compiler.CategorizedProblem;

import java.util.Collection;
import java.util.List;

/**
 * A compilation unit that was generated.
 */
class SourceFileCompilationUnit extends CompilationUnitImpl {

  /**
   * A token to retrieve this object's bytes from the disk cache. It's generally
   * much faster to read from the disk cache than to reread individual
   * resources.
   */
  private long sourceToken = -1;

  private final Resource sourceFile;

  private final ContentId contentId;

  public SourceFileCompilationUnit(Resource sourceFile, ContentId contentId,
      List<CompiledClass> compiledClasses, List<JDeclaredType> types,
      Dependencies dependencies, Collection<? extends JsniMethod> jsniMethods,
      MethodArgNamesLookup methodArgs, CategorizedProblem[] problems) {
    super(compiledClasses, types, dependencies, jsniMethods, methodArgs,
        problems);
    this.sourceFile = sourceFile;
    this.contentId = contentId;
  }

  @Override
  public String getDisplayLocation() {
    return sourceFile.getLocation();
  }

  @Override
  public long getLastModified() {
    return sourceFile.getLastModified();
  }

  @Deprecated
  @Override
  public String getSource() {
    if (sourceToken < 0) {
      String sourceCode = Shared.readSource(sourceFile);
      sourceToken = diskCache.writeString(sourceCode);
      return sourceCode;
    } else {
      return diskCache.readString(sourceToken);
    }
  }

  public Resource getSourceFile() {
    return sourceFile;
  }

  @Override
  public String getTypeName() {
    return Shared.getTypeName(sourceFile);
  }

  @Deprecated
  @Override
  public boolean isGenerated() {
    return false;
  }

  @Deprecated
  @Override
  public boolean isSuperSource() {
    return sourceFile.wasRerooted();
  }

  @Override
  protected Object writeReplace() {
    if (sourceToken < 0) {
      sourceToken = diskCache.transferFromStream(sourceFile.openContents());
    }
    return new CachedCompilationUnit(this, sourceToken);
  }

  @Override
  ContentId getContentId() {
    return contentId;
  }
}
