/*
 * Copyright 2000-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.psi.stubsHierarchy.impl;

import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.psi.CommonClassNames;
import com.intellij.psi.impl.java.stubs.hierarchy.IndexTree;
import gnu.trove.TIntArrayList;
import gnu.trove.TLongIntHashMap;

class NameEnvironment extends UserDataHolderBase {
  public static final int OBJECT_NAME = IndexTree.hashIdentifier("Object");
  public static final int NO_NAME = 0;
  @QNameId public final int java_lang;
  public final QualifiedName java_lang_Enum;
  public final QualifiedName java_lang_annotation_Annotation;

  private final TIntArrayList mySuffixes = new TIntArrayList();
  private final TIntArrayList myStems = new TIntArrayList();
  private final TLongIntHashMap myConcatenations = new TLongIntHashMap();

  NameEnvironment() {
    mySuffixes.add(0);
    myStems.add(0);

    java_lang = fromString("java.lang");
    java_lang_Enum = new QualifiedName(fromString(CommonClassNames.JAVA_LANG_ENUM));
    java_lang_annotation_Annotation = new QualifiedName(fromString(CommonClassNames.JAVA_LANG_ANNOTATION_ANNOTATION));
  }

  @QNameId
  int fromString(String s) {
    return internQualifiedName(IndexTree.hashQualifiedName(s));
  }

  @QNameId int prefixId(@QNameId int nameId) {
    return myStems.get(nameId);
  }

  @ShortName int shortName(@QNameId int id) {
    return mySuffixes.get(id);
  }

  @QNameId int findExistingName(@QNameId int stemId, @ShortName int suffix) {
    int existing = myConcatenations.get(pack(stemId, suffix));
    return existing > 0 ? existing : -1;
  }

  @QNameId int internQualifiedName(@ShortName int[] qname) {
    int id = 0;
    for (int shortName : qname) {
      id = qualifiedName(id, shortName);
    }
    return id;
  }

  @QNameId int qualifiedName(@QNameId int prefix, @ShortName int shortName) {
    int existing = findExistingName(prefix, shortName);
    return existing >= 0 ? existing : addName(prefix, shortName);
  }

  private int addName(@QNameId int stemId, @ShortName int suffix) {
    int newId = mySuffixes.size();
    mySuffixes.add(suffix);
    myStems.add(stemId);
    myConcatenations.put(pack(stemId, suffix), newId);
    return newId;
  }

  private static long pack(@QNameId int stemId, @ShortName int suffix) {
    return ((long)suffix << 32) + stemId;
  }
}
