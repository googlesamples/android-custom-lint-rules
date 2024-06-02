/*
 * Copyright (C) 2017 The Android Open Source Project
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

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class SampleCodeDetectorTest {
  @Test
  fun testBasic() {
    lint()
      .files(
        java(
            """
            package test.pkg;
            public class TestClass1 {
                // In a comment, mentioning "lint" has no effect
                private static String s1 = "Ignore non-word usages: linting";
                private static String s2 = "Let's say it: lint";
            }
            """
          )
          .indented()
      )
      .issues(SampleCodeDetector.ISSUE)
      .run()
      .expect(
        """
        src/test/pkg/TestClass1.java:5: Warning: This code mentions lint: Congratulations [SampleId]
            private static String s2 = "Let's say it: lint";
                                       ~~~~~~~~~~~~~~~~~~~~
        0 errors, 1 warnings
        """
      )
  }
}
