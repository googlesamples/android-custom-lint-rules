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

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue

class AvoidDateDetectorTest : LintDetectorTest() {
  override fun getDetector(): Detector = AvoidDateDetector()

  override fun getIssues(): List<Issue> = listOf(AvoidDateDetector.ISSUE)

  fun testDocumentationExample() {
    lint()
      .files(
        kotlin(
            """
              package test.pkg
              import java.util.Date
              fun test() {
                val date = Date()
              }
              """
          )
          .indented(),
        kotlin(
            """
              package test.pkg
              import java.util.Calendar

              fun test2() {
                  val calendar = Calendar.getInstance()
              }
              """
          )
          .indented(),
      )
      .allowMissingSdk()
      .run()
      .expect(
        """
        src/test/pkg/test.kt:4: Error: Don't use Date; use java.time.* instead [OldDate]
          val date = Date()
                     ~~~~~~
        src/test/pkg/test2.kt:5: Error: Don't use Calendar.getInstance; use java.time.* instead [OldDate]
            val calendar = Calendar.getInstance()
                           ~~~~~~~~~~~~~~~~~~~~~~
        2 errors, 0 warnings
        """
      )
      .expectFixDiffs(
        """
        Fix for src/test/pkg/test.kt line 4: Replace with java.time.LocalTime.now():
        @@ -2 +2
        + import java.time.LocalTime
        @@ -4 +5
        -   val date = Date()
        +   val date = LocalTime.now()
        Fix for src/test/pkg/test.kt line 4: Replace with java.time.LocalDate.now():
        @@ -2 +2
        + import java.time.LocalDate
        @@ -4 +5
        -   val date = Date()
        +   val date = LocalDate.now()
        Fix for src/test/pkg/test.kt line 4: Replace with java.time.LocalDateTime.now():
        @@ -2 +2
        + import java.time.LocalDateTime
        @@ -4 +5
        -   val date = Date()
        +   val date = LocalDateTime.now()
        """
      )
  }
}
