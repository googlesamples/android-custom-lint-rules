/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lint.checks

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

class AvoidDateDetector : Detector(), SourceCodeScanner {
  companion object Issues {
    private val IMPLEMENTATION =
      Implementation(AvoidDateDetector::class.java, Scope.JAVA_FILE_SCOPE)

    @JvmField
    val ISSUE =
      Issue.create(
        id = "OldDate",
        briefDescription = "Avoid Date and Calendar",
        explanation =
          """
          The `java.util.Date` and `java.util.Calendar` classes should not be used; instead \
          use the `java.time` package, such as `LocalDate` and `LocalTime`.
          """,
        category = Category.CORRECTNESS,
        priority = 6,
        severity = Severity.ERROR,
        androidSpecific = true,
        implementation = IMPLEMENTATION,
      )
  }

  // java.util.Date()
  override fun getApplicableConstructorTypes(): List<String> = listOf("java.util.Date")

  override fun visitConstructor(
    context: JavaContext,
    node: UCallExpression,
    constructor: PsiMethod,
  ) {
    context.report(
      ISSUE,
      node,
      context.getLocation(node),
      "Don't use `Date`; use `java.time.*` instead",
      fix()
        .alternatives(
          fix().replace().all().with("java.time.LocalTime.now()").shortenNames().build(),
          fix().replace().all().with("java.time.LocalDate.now()").shortenNames().build(),
          fix().replace().all().with("java.time.LocalDateTime.now()").shortenNames().build(),
        ),
    )
  }

  // java.util.Calendar.getInstance()
  override fun getApplicableMethodNames(): List<String> = listOf("getInstance")

  override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
    val evaluator = context.evaluator
    if (!evaluator.isMemberInClass(method, "java.util.Calendar")) {
      return
    }
    context.report(
      ISSUE,
      node,
      context.getLocation(node),
      "Don't use `Calendar.getInstance`; use `java.time.*` instead",
    )
  }
}
