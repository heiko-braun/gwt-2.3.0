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
package com.google.gwt.dev.jjs.impl;

import com.google.gwt.dev.jjs.InternalCompilerException;
import com.google.gwt.dev.jjs.SourceInfo;
import com.google.gwt.dev.jjs.SourceOrigin;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JBlock;
import com.google.gwt.dev.jjs.ast.JCaseStatement;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JDeclaredType;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JGwtCreate;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JMethodBody;
import com.google.gwt.dev.jjs.ast.JMethodCall;
import com.google.gwt.dev.jjs.ast.JModVisitor;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JReboundEntryPoint;
import com.google.gwt.dev.jjs.ast.JReferenceType;
import com.google.gwt.dev.jjs.ast.JReturnStatement;
import com.google.gwt.dev.jjs.ast.JSwitchStatement;
import com.google.gwt.dev.jjs.ast.JType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Replaces any "GWT.create()" calls with a new expression for the actual result
 * of the deferred binding decision.
 */
public class ResolveRebinds {

  private class RebindVisitor extends JModVisitor {
    @Override
    public void endVisit(JGwtCreate x, Context ctx) {

      if (isSoftRebind(x.getSourceType())) {
        JMethod method = rebindMethod(x.getSourceType(), x.getResultTypes(),
            x.getInstantiationExpressions());
        JMethodCall call = new JMethodCall(x.getSourceInfo(), null, method);
        ctx.replaceMe(call);
        return;
      }

      JClassType rebindResult = rebind(x.getSourceType());
      List<JClassType> rebindResults = x.getResultTypes();
      for (int i = 0; i < rebindResults.size(); ++i) {
        // Find the matching rebound type.
        if (rebindResult == rebindResults.get(i)) {
          // Replace with the associated instantiation expression.
          ctx.replaceMe(x.getInstantiationExpressions().get(i));
          return;
        }
      }
      throw new InternalCompilerException(
          "No matching rebind result in all rebind results!");
    }

    @Override
    public void endVisit(JReboundEntryPoint x, Context ctx) {

      if (isSoftRebind(x.getSourceType())) {
        JMethod method = rebindMethod(x.getSourceType(), x.getResultTypes(),
            x.getEntryCalls());
        JMethodCall call = new JMethodCall(x.getSourceInfo(), null, method);
        ctx.replaceMe(call.makeStatement());
        return;
      }

      JClassType rebindResult = rebind(x.getSourceType());
      List<JClassType> rebindResults = x.getResultTypes();
      for (int i = 0; i < rebindResults.size(); ++i) {
        // Find the matching rebound type.
        if (rebindResult == rebindResults.get(i)) {
          // Replace with the associated instantiation expression.
          ctx.replaceMe(x.getEntryCalls().get(i).makeStatement());
          return;
        }
      }
      throw new InternalCompilerException(
          "No matching rebind result in all rebind results!");
    }
  }

  public static boolean exec(JProgram program,
      Map<String, String>[] orderedRebindAnswers) {
    return new ResolveRebinds(program, orderedRebindAnswers).execImpl();
  }

  /**
   * Returns the rebind answers that do not vary across various maps of rebind
   * answers.
   */
  public static Map<String, String> getHardRebindAnswers(
      Map<String, String>[] rebindAnswers) {
    Iterator<Map<String, String>> it = Arrays.asList(rebindAnswers).iterator();

    // Start with an arbitrary copy of a rebind answer map
    Map<String, String> toReturn = new HashMap<String, String>(it.next());

    while (it.hasNext()) {
      Map<String, String> next = it.next();
      // Only keep key/value pairs present in the other rebind map
      toReturn.entrySet().retainAll(next.entrySet());
    }

    return toReturn;
  }

  private final Map<String, String> hardRebindAnswers;
  private final JClassType holderType;
  private final Map<String, String>[] orderedRebindAnswers;
  private final JMethod permutationIdMethod;
  private final JProgram program;
  private final Map<JReferenceType, JMethod> rebindMethods = new IdentityHashMap<JReferenceType, JMethod>();

  private ResolveRebinds(JProgram program,
      Map<String, String>[] orderedRebindAnswers) {
    this.program = program;
    this.orderedRebindAnswers = orderedRebindAnswers;

    this.hardRebindAnswers = getHardRebindAnswers(orderedRebindAnswers);
    this.holderType = (JClassType) program.getIndexedType("CollapsedPropertyHolder");
    this.permutationIdMethod = program.getIndexedMethod("CollapsedPropertyHolder.getPermutationId");
  }

  public JClassType rebind(JType type) {
    // Rebinds are always on a source type name.
    String reqType = type.getName().replace('$', '.');
    String reboundClassName = hardRebindAnswers.get(reqType);
    if (reboundClassName == null) {
      // The fact that we already compute every rebind permutation before
      // compiling should prevent this case from ever happening in real life.
      //
      throw new InternalCompilerException("Unexpected failure to rebind '"
          + reqType + "'");
    }
    JDeclaredType result = program.getFromTypeMap(reboundClassName);
    assert (result != null);
    return (JClassType) result;
  }

  private boolean execImpl() {
    RebindVisitor rebinder = new RebindVisitor();
    rebinder.accept(program);
    return rebinder.didChange();
  }

  private boolean isSoftRebind(JType type) {
    String reqType = type.getName().replace('$', '.');
    return !hardRebindAnswers.containsKey(reqType);
  }

  private JMethod rebindMethod(JReferenceType requestType,
      List<JClassType> resultTypes, List<JExpression> instantiationExpressions) {
    assert resultTypes.size() == instantiationExpressions.size();

    JMethod toReturn = rebindMethods.get(requestType);
    if (toReturn != null) {
      return toReturn;
    }

    SourceInfo info = requestType.getSourceInfo().makeChild(
        SourceOrigin.UNKNOWN);

    // Maps the result types to the various virtual permutation ids
    Map<JClassType, List<Integer>> resultsToPermutations = new LinkedHashMap<JClassType, List<Integer>>();

    for (int i = 0, j = orderedRebindAnswers.length; i < j; i++) {
      Map<String, String> answerMap = orderedRebindAnswers[i];
      String answerTypeName = answerMap.get(requestType.getName().replace('$',
          '.'));
      // We take an answer class, e.g. DOMImplSafari ...
      JClassType answerType = (JClassType) program.getFromTypeMap(answerTypeName);

      List<Integer> list = resultsToPermutations.get(answerType);
      if (list == null) {
        list = new ArrayList<Integer>();
        resultsToPermutations.put(answerType, list);
      }
      // and map it to the permutation ID for a particular set of values
      list.add(i);
    }

    // Pick the most-used result type to emit less code
    JClassType mostUsed = null;
    {
      int max = 0;
      for (Map.Entry<JClassType, List<Integer>> entry : resultsToPermutations.entrySet()) {
        int size = entry.getValue().size();
        if (size > max) {
          max = size;
          mostUsed = entry.getKey();
        }
      }
    }
    assert mostUsed != null;

    // c_g_g_d_c_i_DOMImpl
    toReturn = program.createMethod(info,
        requestType.getName().replace("_", "_1").replace('.', '_'), holderType,
        program.getTypeJavaLangObject().getNonNull(), false, true, true, false,
        false);
    toReturn.freezeParamTypes();
    info.addCorrelation(info.getCorrelator().by(toReturn));
    rebindMethods.put(requestType, toReturn);

    // Used in the return statement at the end
    JExpression mostUsedExpression = null;

    JBlock switchBody = new JBlock(info);
    for (int i = 0, j = resultTypes.size(); i < j; i++) {
      JClassType resultType = resultTypes.get(i);
      JExpression instantiation = instantiationExpressions.get(i);

      List<Integer> permutations = resultsToPermutations.get(resultType);
      if (permutations == null) {
        // This rebind result is unused in this permutation
        continue;
      } else if (resultType == mostUsed) {
        // Save off the fallback expression and go onto the next type
        mostUsedExpression = instantiation;
        continue;
      }

      for (int permutationId : permutations) {
        // case 33:
        switchBody.addStmt(new JCaseStatement(info,
            program.getLiteralInt(permutationId)));
      }

      // return new FooImpl();
      JReturnStatement ret = new JReturnStatement(info, instantiation);
      switchBody.addStmt(ret);
    }

    assert switchBody.getStatements().size() > 0 : "No case statement emitted "
        + "for supposedly soft-rebind type " + requestType.getName();

    // switch (CollapsedPropertyHolder.getPermutationId()) { ... }
    JSwitchStatement sw = new JSwitchStatement(info, new JMethodCall(info,
        null, permutationIdMethod), switchBody);

    // return new FallbackImpl(); at the very end.
    assert mostUsedExpression != null : "No most-used expression";
    JReturnStatement fallbackReturn = new JReturnStatement(info,
        mostUsedExpression);

    JMethodBody body = (JMethodBody) toReturn.getBody();
    body.getBlock().addStmt(sw);
    body.getBlock().addStmt(fallbackReturn);

    return toReturn;
  }
}
