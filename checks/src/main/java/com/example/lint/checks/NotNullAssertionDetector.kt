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

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import org.jetbrains.kotlin.analysis.api.analyze
import org.jetbrains.kotlin.codegen.optimization.common.analyze
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UPostfixExpression

class NotNullAssertionDetector : Detector(), SourceCodeScanner {
  companion object Issues {
    private val IMPLEMENTATION =
      Implementation(NotNullAssertionDetector::class.java, Scope.JAVA_FILE_SCOPE)

    @JvmField
    val ISSUE =
      Issue.create(
        id = "NotNullAssertion",
        briefDescription = "Avoid `!!`",
        explanation =
          """
          Do not use the `!!` operator. It can lead to null pointer exceptions. \
          Please use the `?` operator instead, or assign to a local variable with \
          `?:` initialization if necessary.
          """,
        category = Category.CORRECTNESS,
        priority = 6,
        severity = Severity.WARNING,
        implementation = IMPLEMENTATION,
      )
  }

  override fun getApplicableUastTypes(): List<Class<out UElement>>? {
    return listOf(UPostfixExpression::class.java)
  }

  override fun createUastHandler(context: JavaContext): UElementHandler {
    return object : UElementHandler() {
      override fun visitPostfixExpression(node: UPostfixExpression) {
        if (node.operator.text == "!!") {
          var message = "Do not use `!!`"

          // Kotlin Analysis API example
          val sourcePsi = node.operand.sourcePsi
          if (sourcePsi is KtExpression) {
            analyze(sourcePsi) {
              val type = sourcePsi.getKtType()
              if (type != null && !type.canBeNull) {
                message += " -- it's not even needed here"
              }
            }
          }

          val incident = Incident(ISSUE, node, context.getLocation(node), message)
          context.report(incident)
        }
      }
    }
  }
}
