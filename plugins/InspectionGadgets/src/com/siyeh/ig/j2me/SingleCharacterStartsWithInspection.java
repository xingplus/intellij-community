/*
 * Copyright 2003-2005 Dave Griffith
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
package com.siyeh.ig.j2me;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.psi.*;
import com.intellij.openapi.project.Project;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.util.IncorrectOperationException;
import com.siyeh.HardcodedMethodConstants;
import com.siyeh.InspectionGadgetsBundle;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.ExpressionInspection;
import com.siyeh.ig.InspectionGadgetsFix;
import com.siyeh.ig.psiutils.TypeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NonNls;

public class SingleCharacterStartsWithInspection extends ExpressionInspection {

    public String getDisplayName() {
        return InspectionGadgetsBundle.message(
                "single.character.startswith.display.name");
    }

    public String getGroupDisplayName() {
        return GroupNames.J2ME_GROUP_NAME;
    }

    @Nullable
    protected InspectionGadgetsFix buildFix(PsiElement location) {
        return new SingleCharacterStartsWithFix();
    }

    private static class SingleCharacterStartsWithFix
            extends InspectionGadgetsFix {

        public String getName() {
            return InspectionGadgetsBundle.message(
                    "single.character.startswith.quickfix");
        }

        protected void doFix(Project project, ProblemDescriptor descriptor)
                throws IncorrectOperationException {
            final PsiElement element = descriptor.getPsiElement();
            final PsiReferenceExpression methodExpression =
                    (PsiReferenceExpression)element.getParent();
            final PsiMethodCallExpression methodCall =
                    (PsiMethodCallExpression)methodExpression.getParent();
            final PsiElement qualifier = methodExpression.getQualifier();
            final PsiExpressionList argumentList = methodCall.getArgumentList();
            final PsiExpression[] expressions = argumentList.getExpressions();
            final PsiExpression expression = expressions[0];
            final String expressionText = expression.getText();
            final String character = expressionText.substring(1,
                    expressionText.length() - 1);
            if (qualifier == null) {
                return;
            }
            final String qualifierText = qualifier.getText();
            @NonNls final String newExpression;
            final String referenceName = methodExpression.getReferenceName();
            if (HardcodedMethodConstants.STARTS_WITH.equals(referenceName)) {
                newExpression = qualifierText + ".length() > 0 && " +
                        qualifierText + ".charAt(0) == '" + character + '\'';
            } else {
                newExpression = qualifierText + ".length() > 0 && " +
                        qualifierText + ".charAt(" + qualifierText +
                        ".length() - 1) == '" + character + '\'';
            }
            replaceExpression(methodCall, newExpression);
        }
    }

    public String buildErrorString(PsiElement location) {
        return InspectionGadgetsBundle.message(
                "single.character.startswith.problem.descriptor");
    }

    public BaseInspectionVisitor buildVisitor() {
        return new SingleCharacterStartsWithVisitor();
    }

    private static class SingleCharacterStartsWithVisitor
            extends BaseInspectionVisitor {

        public void visitMethodCallExpression(
                @NotNull PsiMethodCallExpression call) {
            super.visitMethodCallExpression(call);
            final PsiReferenceExpression methodExpression =
                    call.getMethodExpression();
            final String methodName = methodExpression.getReferenceName();
            if (!HardcodedMethodConstants.STARTS_WITH.equals(methodName) &&
                    !HardcodedMethodConstants.ENDS_WITH.equals(methodName)) {
                return;
            }
            final PsiExpressionList argumentList = call.getArgumentList();
            final PsiExpression[] args = argumentList.getExpressions();
            if (args.length != 1 && args.length != 2) {
                return;
            }
            if (!isSingleCharacterStringLiteral(args[0])) {
                return;
            }
            final PsiExpression qualifier =
                    methodExpression.getQualifierExpression();
            if (qualifier == null) {
                return;
            }
            final PsiType type = qualifier.getType();
            if (!TypeUtils.isJavaLangString(type)) {
                return;
            }
            registerMethodCallError(call);
        }

        private static boolean isSingleCharacterStringLiteral(
                PsiExpression arg) {
            final PsiType type = arg.getType();
            if (!TypeUtils.isJavaLangString(type)) {
                return false;
            }
            if (!(arg instanceof PsiLiteralExpression)) {
                return false;
            }
            final PsiLiteralExpression literal = (PsiLiteralExpression) arg;
            final String value = (String) literal.getValue();
            if (value == null) {
                return false;
            }
            return value.length() == 1;
        }
    }
}