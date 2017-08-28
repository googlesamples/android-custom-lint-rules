/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.example.google.lint;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import java.util.Collections;
import java.util.List;

import static com.android.SdkConstants.FN_ANDROID_MANIFEST_XML;

/**
 * <b>NOTE: This is not a final API; if you rely on this be prepared
 * to adjust your code for the next tools release.</b>
 */
public class MainActivityDetectorTest extends LintDetectorTest {
    @Override
    protected Detector getDetector() {
        return new MainActivityDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Collections.singletonList(MainActivityDetector.ISSUE);
    }

    /**
     * Test that a manifest with an activity with a launcher intent has no warnings.
     */
    public void testHasMainActivity() {
        lint().files(
                xml(FN_ANDROID_MANIFEST_XML, "" +
                        "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<manifest package=\"com.example.android.custom-lint-rules\"\n" +
                        "          xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                        "    <application>\n" +
                        "        <activity android:name=\"com.example.android.custom-lint-rules" +
                        ".OtherActivity\">\n" +
                        "        </activity>\n" +
                        "\n" +
                        "        <activity android:name=\"com.example.android.custom-lint-rules" +
                        ".MainActivity\">\n" +
                        "            <intent-filter>\n" +
                        "                <action android:name=\"android.intent.action.MAIN\"/>\n" +
                        "                <category android:name=\"android.intent.category.LAUNCHER\"/>\n" +
                        "            </intent-filter>\n" +
                        "        </activity>\n" +
                        "    </application>\n" +
                        "</manifest>"))
                .run()
                .expectClean();
    }

    /**
     * Test that a manifest <em>without</em> an activity with a launcher intent reports an error.
     */
    public void testMissingMainActivity() {
        String expected = "AndroidManifest.xml: Error: Expecting AndroidManifest.xml to have an " +
                "activity with a launcher intent. [MainActivityDetector]\n" +
                "1 errors, 0 warnings\n";
        lint().files(
                xml(FN_ANDROID_MANIFEST_XML, "" +
                        "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<manifest package=\"com.example.android.custom-lint-rules\"\n" +
                        "          xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                        "    <application>\n" +
                        "        <activity android:name=\"com.example.android.custom-lint-rules" +
                        ".Activity1\">\n" +
                        "            <intent-filter>\n" +
                        "                <action android:name=\"android.intent.action.VIEW\" />\n" +
                        "\n" +
                        "                <category android:name=\"android.intent.category.HOME\" />\n" +
                        "                <category android:name=\"android.intent.category.LAUNCHER\" />\n" +
                        "                <category android:name=\"android.intent.category.DEFAULT\" />\n" +
                        "                <category android:name=\"android.intent.category.BROWSABLE\" " +
                        "/>\n" +
                        "            </intent-filter>\n" +
                        "        </activity>\n" +
                        "\n" +
                        "        <activity android:name=\"com.example.android.custom-lint-rules" +
                        ".Activity2\">\n" +
                        "        </activity>\n" +
                        "\n" +
                        "        <activity android:name=\"com.example.android.custom-lint-rules" +
                        ".Activity3\">\n" +
                        "            <intent-filter>\n" +
                        "                <action android:name=\"android.intent.action.SEND\"/>\n" +
                        "                <category android:name=\"android.intent.category.DEFAULT\"/>\n" +
                        "                <data android:mimeType=\"text/plain\"/>\n" +
                        "            </intent-filter>\n" +
                        "        </activity>\n" +
                        "    </application>\n" +
                        "</manifest>"))
                .run()
                .expect(expected);
    }

    /**
     * Test that a manifest without an <code>&lt;application&gt;</code> tag reports an error.
     */
    public void testMissingApplication() {
        String expected = "AndroidManifest.xml: Error: Expecting AndroidManifest.xml to have an " +
                "<activity> tag. [MainActivityDetector]\n" +
                "1 errors, 0 warnings\n";
        lint().files(
                xml(FN_ANDROID_MANIFEST_XML, "" +
                        "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<manifest package=\"com.example.android.custom-lint-rules\"\n" +
                        "          xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                        "</manifest>"))
                .run()
                .expect(expected);
    }
}
