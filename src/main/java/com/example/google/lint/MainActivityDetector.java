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
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import static com.android.SdkConstants.ANDROID_MANIFEST_XML;
import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.TAG_ACTIVITY;
import static com.android.SdkConstants.TAG_APPLICATION;
import static com.android.SdkConstants.TAG_INTENT_FILTER;
import static com.android.xml.AndroidManifest.NODE_ACTION;
import static com.android.xml.AndroidManifest.NODE_CATEGORY;

/**
 * Checks for a main activity in <code>AndroidManifest.xml</code>.
 * <p/>
 * <b>NOTE: This is not a final API; if you rely on this be prepared
 * to adjust your code for the next tools release.</b>
 */
public class MainActivityDetector extends ResourceXmlDetector implements Detector.XmlScanner {
    public static final Issue ISSUE = Issue.create(
            "MainActivityDetector",
            "Checks for a main activity",
            "This app should have a main launcher activity.",
            Category.CORRECTNESS,
            8,
            Severity.ERROR,
            new Implementation(
                    MainActivityDetector.class,
                    EnumSet.of(Scope.MANIFEST)));

    /**
     * Will be <code>true</code> if the current file we're checking has at least one activity.
     */
    private boolean mHasActivity;
    /**
     * Will be <code>true</code> if the file has a main activity.
     */
    private boolean mHasMainActivity;

    @Override
    public Collection<String> getApplicableElements() {
        return Arrays.asList(
                TAG_ACTIVITY
        );
    }

    @Override
    public void beforeCheckFile(@NonNull Context context) {
        mHasActivity = false;
        mHasMainActivity = false;
    }

    @Override
    public void afterCheckFile(@NonNull Context context) {
        // Report an error if there are no <application> tags to check.
        // We assume the application tag is in the right place (i.e. have correct parent elements).
        Location location = Location.create(context.file);
        if (!mHasActivity) {
            context.report(ISSUE, location,
                    "Expecting " + ANDROID_MANIFEST_XML + " to have an <" + TAG_APPLICATION +
                            "> tag.");
        } else if (!mHasMainActivity) {
            // Report the issue if the manifest file has no main activity.
            context.report(ISSUE, location,
                    "Expecting " + ANDROID_MANIFEST_XML + " to have a main activity.");
        }
    }

    @Override
    public void visitElement(XmlContext context, Element activityElement) {
        // Checks every activity under the <application> element and reports an error if there is
        // no main activity.
        mHasActivity = true;
        if (isMainActivity(activityElement)) {
            mHasMainActivity = true;
        }
    }

    /**
     * Returns true if the XML node is a main activity.
     * <p/>
     * A main activity is an <code>&lt;activity&gt;</code> node with an <code>&lt;
     * intent-filter&gt;</code> that contains the following tags:
     * <p/>
     * <pre>
     *   <category android:name="android.intent.category.LAUNCHER" />
     *   <action android:name="android.intent.action.MAIN" />
     * </pre>
     *
     * @param activityNode The node to check.
     * @return <code>true</code> if the node is a main activity.
     */
    private boolean isMainActivity(Node activityNode) {
        if (TAG_ACTIVITY.equals(activityNode.getNodeName())) {
            // Loop through all <intent-filter> tags
            NodeList activityChildren = activityNode.getChildNodes();
            for (int i = 0; i < activityChildren.getLength(); ++i) {
                Node activityChild = activityChildren.item(i);
                if (TAG_INTENT_FILTER.equals(activityChild.getNodeName())) {
                    NodeList intentFilterChildren = activityChild.getChildNodes();

                    // Check for these children nodes:
                    //
                    // <category android:name="android.intent.category.LAUNCHER" />
                    // <action android:name="android.intent.action.MAIN" />
                    boolean hasLauncherCategory = false;
                    boolean hasMainAction = false;

                    for (int j = 0; j < intentFilterChildren.getLength(); ++j) {
                        Node intentFilterChild = intentFilterChildren.item(j);
                        // Check for category tag
                        if (NODE_CATEGORY.equals(intentFilterChild.getNodeName())) {
                            Node categoryName = intentFilterChild.getAttributes()
                                    .getNamedItemNS(ANDROID_URI, ATTR_NAME);
                            if (categoryName != null && categoryName.getNodeValue().equals(
                                    ManifestConstants.CATEGORY_NAME_LAUNCHER)) {
                                hasLauncherCategory = true;
                            }
                        }
                        // Check for action tag
                        if (NODE_ACTION.equals(intentFilterChild.getNodeName())) {
                            Node actionName = intentFilterChild.getAttributes()
                                    .getNamedItemNS(ANDROID_URI, ATTR_NAME);
                            if (actionName != null && actionName.getNodeValue().equals(
                                    ManifestConstants.ACTION_NAME_MAIN)) {
                                hasMainAction = true;
                            }
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
