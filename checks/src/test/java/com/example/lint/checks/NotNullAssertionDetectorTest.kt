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

class NotNullAssertionDetectorTest : LintDetectorTest() {
  override fun getDetector(): Detector {
    return NotNullAssertionDetector()
  }

  override fun getIssues(): List<Issue> {
    return listOf(NotNullAssertionDetector.ISSUE)
  }

  fun testDocumentationExample() {
    lint()
      .files(
        kotlin(
            """
            package test.pkg

            fun test(s: String?, t: String) {
              s?.plus(s)
              s!!.plus(s)
              t!!.plus(t)
            }
            """
          )
          .indented()
      )
      .run()
      .expect(
        """
        src/test/pkg/test.kt:5: Warning: Do not use !! [NotNullAssertion]
          s!!.plus(s)
          ~~~
        src/test/pkg/test.kt:6: Warning: Do not use !! -- it's not even needed here [NotNullAssertion]
          t!!.plus(t)
          ~~~
        0 errors, 2 warnings
        """
      )
  }
}
