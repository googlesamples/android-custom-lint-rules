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
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

import static com.android.SdkConstants.ANDROID_MANIFEST_XML;
import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.TAG_ACTIVITY;
import static com.android.SdkConstants.TAG_APPLICATION;
import static com.android.SdkConstants.TAG_INTENT_FILTER;
import static com.android.xml.AndroidManifest.NODE_ACTION;
import static com.android.xml.AndroidManifest.NODE_CATEGORY;
import static com.example.google.lint.ManifestConstants.ACTION_NAME_MAIN;
import static com.example.google.lint.ManifestConstants.CATEGORY_NAME_LAUNCHER;

/**
 * Checks for an activity with a launcher intent in <code>AndroidManifest.xml</code>.
 * <p/>
 * <b>NOTE: This is not a final API; if you rely on this be prepared
 * to adjust your code for the next tools release.</b>
 */
public class MainActivityDetector extends ResourceXmlDetector implements Detector.XmlScanner {
    public static final Issue ISSUE = Issue.create(
            "MainActivityDetector",
            "Missing Launcher Activities",
            "This app should have an activity with a launcher intent.",
            Category.CORRECTNESS,
            8,
            Severity.ERROR,
            new Implementation(
                    MainActivityDetector.class,
                    EnumSet.of(Scope.MANIFEST)));

    /**
     * This will be <code>true</code> if the current file we're checking has at least one activity.
     */
    private boolean mHasActivity;
    /**
     * This will be <code>true</code> if the file has an activity with a launcher intent.
     */
    private boolean mHasLauncherActivity;
    /**
     * The manifest file location for the main project, <code>null</code> if there is no manifest.
     */
    private Location mManifestLocation;

    /**
     * No-args constructor used by the lint framework to instantiate the detector.
     */
    public MainActivityDetector() {
    }

    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singleton(TAG_ACTIVITY);
    }

    @Override
    public void beforeCheckProject(@NonNull Context context) {
        mHasActivity = false;
        mHasLauncherActivity = false;
        mManifestLocation = null;
    }

    @Override
    public void afterCheckProject(@NonNull Context context) {
        // Don't report issues on libraries that may not have a launcher activity
        if (context.getProject() == context.getMainProject()
                && !context.getMainProject().isLibrary()
                && mManifestLocation != null) {
            if (!mHasActivity) {
                context.report(ISSUE, mManifestLocation,
                        "Expecting " + ANDROID_MANIFEST_XML + " to have an <" + TAG_APPLICATION +
                                "> tag.");
            } else if (!mHasLauncherActivity) {
                // Report the issue if the manifest file has no activity with a launcher intent.
                context.report(ISSUE, mManifestLocation,
                        "Expecting " + ANDROID_MANIFEST_XML +
                                " to have an activity with a launcher intent.");
            }
        }
    }

    @Override
    public void afterCheckFile(@NonNull Context context) {
        // Store a reference to the manifest file in case we need to report it's location.
        if (context.getProject() == context.getMainProject()) {
            mManifestLocation = Location.create(context.file);
        }
    }

    @Override
    public void visitElement(XmlContext context, Element activityElement) {
        // Checks every activity and reports an error if there is no activity with a launcher
        // intent.
        mHasActivity = true;
        if (isMainActivity(activityElement)) {
            mHasLauncherActivity = true;
        }
    }

    /**
     * Returns true if the XML node is an activity with a launcher intent.
     *
     * @param activityNode The node to check.
     * @return <code>true</code> if the node is an activity with a launcher intent.
     */
    private boolean isMainActivity(Node activityNode) {
        if (TAG_ACTIVITY.equals(activityNode.getNodeName())) {
            // Loop through all <intent-filter> tags
            for (Element activityChild : LintUtils.getChildren(activityNode)) {
                if (TAG_INTENT_FILTER.equals(activityChild.getNodeName())) {
                    // Check for these children nodes:
                    //
                    // <category android:name="android.intent.category.LAUNCHER" />
                    // <action android:name="android.intent.action.MAIN" />
                    boolean hasLauncherCategory = false;
                    boolean hasMainAction = false;

                    for (Element intentFilterChild : LintUtils.getChildren(activityChild)) {
                        // Check for category tag)
                        if (NODE_CATEGORY.equals(intentFilterChild.getNodeName())
                                && CATEGORY_NAME_LAUNCHER.equals(
                                intentFilterChild.getAttributeNS(ANDROID_URI, ATTR_NAME))) {
                            hasLauncherCategory = true;
                        }
                        // Check for action tag
                        if (NODE_ACTION.equals(intentFilterChild.getNodeName())
                                && ACTION_NAME_MAIN.equals(
                                intentFilterChild.getAttributeNS(ANDROID_URI, ATTR_NAME))) {
                            hasMainAction = true;
                        }
                    }

                    if (hasLauncherCategory && hasMainAction) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
