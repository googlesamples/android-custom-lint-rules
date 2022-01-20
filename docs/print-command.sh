#!/bin/sh

# Set the following flags
export STUDIO=your source tree
export ANDROIDX_REPO=your androidx checkout
export OUTPUT=this git repo + /docs/checks
export LINT_URI_PREFIX=https://cs.android.com/android-studio/platform/tools/base/+/mirror-goog-studio-main:lint/
export ANDROID_URI_PREFIX=https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:

# If running the documentation driver directly, omit the
# --generate-docs flag

echo lint \
    --generate-docs \
    --output $OUTPUT \
    --builtins \
    --source-url $LINT_URI_PREFIX $STUDIO/tools/base/lint \
    --test-url $LINT_URI_PREFIX $STUDIO/tools/base/lint \
    --lint-jars $ANDROIDX_REPO/out \
    --source-url $ANDROID_URI_PREFIX $ANDROIDX_REPO/frameworks/support \
    --test-url $ANDROID_URI_PREFIX $ANDROIDX_REPO/frameworks/support

