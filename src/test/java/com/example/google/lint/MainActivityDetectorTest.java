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

import com.android.annotations.NonNull;
import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Project;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.android.SdkConstants.FN_ANDROID_MANIFEST_XML;

/**
 * <b>NOTE: This is not a final API; if you rely on this be prepared
 * to adjust your code for the next tools release.</b>
 */
public class MainActivityDetectorTest extends LintDetectorTest {
    /**
     * The set of enabled issues for a given test.
     */
    private Set<Issue> mEnabled = new HashSet<Issue>();

    @Override
    protected Detector getDetector() {
        return new MainActivityDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Arrays.asList(
                MainActivityDetector.ISSUE
        );
    }

    /**
     * Gets the configuration for the test.
     * Each test can have a set of enabled issues by assigning the member field {@link #mEnabled}.
     */
    @Override
    protected TestConfiguration getConfiguration(LintClient client, Project project) {
        return new TestConfiguration(client, project, null) {
            @Override
            public boolean isEnabled(@NonNull Issue issue) {
                return super.isEnabled(issue) && mEnabled.contains(issue);
            }
        };
    }

    /**
     * Test that a manifest with a main activity has no warnings.
     */
    public void testHasMainActivity() throws Exception {
        mEnabled = Collections.singleton(MainActivityDetector.ISSUE);
        String expected = "No warnings.";
        String result = lintProject(xml(FN_ANDROID_MANIFEST_XML, "" +
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
                "</manifest>"));
        assertEquals(expected, result);
    }

    /**
     * Test that a manifest <em>without</em> a main activity reports an error.
     */
    public void testMissingMainActivity() throws Exception {
        mEnabled = Collections.singleton(MainActivityDetector.ISSUE);
        String expected = "AndroidManifest.xml: Error: Expecting AndroidManifest.xml to have a " +
                "main activity. [MainActivityDetector]\n" +
                "1 errors, 0 warnings\n";
        String result = lintProject(xml(FN_ANDROID_MANIFEST_XML, "" +
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
                "</manifest>"));
        assertEquals(expected, result);
    }

    /**
     * Test that a manifest without an <code>&lt;application&gt;</code> tag reports an error.
     */
    public void testMissingApplication() throws Exception {
        mEnabled = Collections.singleton(MainActivityDetector.ISSUE);
        String expected = "AndroidManifest.xml: Error: Expecting AndroidManifest.xml to have an " +
                "<application> tag. [MainActivityDetector]\n" +
                "1 errors, 0 warnings\n";
        String result = lintProject(xml(FN_ANDROID_MANIFEST_XML, "" +
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<manifest package=\"com.example.android.custom-lint-rules\"\n" +
                "          xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "</manifest>"));
        assertEquals(expected, result);
    }
}
