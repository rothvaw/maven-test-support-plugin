/*
 * Copyright 2014 Dawid Pytel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dpytel.intellij.plugin.maventest.model;

import com.intellij.execution.Location;
import com.intellij.execution.junit2.info.MethodLocation;
import com.intellij.execution.junit2.info.PsiClassLocator;
import com.intellij.execution.junit2.info.TestInfo;
import com.intellij.execution.junit2.segments.ObjectReader;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.MethodSignature;
import com.intellij.psi.util.MethodSignatureUtil;

/**
 *
 */
public class TestMethodInfo extends TestInfo {

    private final PsiClassLocator myClass;
    private final String methodName;

    public TestMethodInfo(String classQualifiedName, String methodName) {
        this.methodName = methodName;
        myClass = PsiClassLocator.fromQualifiedName(classQualifiedName);
    }

    @Override
    public void readFrom(ObjectReader reader) {
        // do nothing
    }

    @Override
    public String getComment() {
        return "";
    }

    @Override
    public String getName() {
        return methodName;
    }

    @Override
    public Location getLocation(Project project, GlobalSearchScope searchScope) {
        Location<PsiClass> classLocation = myClass.getLocation(project, searchScope);
        if (classLocation == null) {
            return null;
        }
        MethodSignature patternMethod = MethodSignatureUtil.createMethodSignature(methodName, PsiType.EMPTY_ARRAY,
            PsiTypeParameter.EMPTY_ARRAY, PsiSubstitutor.EMPTY);
        PsiMethod method = MethodSignatureUtil
            .findMethodBySignature(classLocation.getPsiElement(), patternMethod, true);
        if (method == null) {
            return null;
        }
        return new MethodLocation(project, method, classLocation);
    }

    @Override
    public boolean shouldRun() {
        return true;
    }

    @Override
    public int getTestsCount() {
        return 1;
    }
}