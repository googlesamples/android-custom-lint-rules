Custom Lint Rules
=================

The [Android `lint` tool](http://developer.android.com/tools/help/lint.html) is a static code
 analysis tool that checks your Android project source files for potential bugs and optimization
 improvements for correctness, security, performance, usability, accessibility, and
 internationalization. Lint comes with over 200 checks, however it can be extended with additional
 custom rules.

**NOTE: The lint API is not a final API; if you rely on this be prepared
 to adjust your code for the next tools release.**

Introduction
------------

The Android Lint API allows users to create custom lint rules. For example, if you are the author of
 a library project, and your library project has certain usage requirements, you can write
 additional lint rules to check that your library is used correctly, and then you can distribute
 those extra lint rules for users of the library. Similarly, you may have company-local rules you'd
 like to enforce.

This sample demonstrates how to create a custom lint checks and corresponding tests for those rules.

Getting Started
---------------

##### Fetch code

```
git clone https://github.com/googlesamples/android-custom-lint-rules.git
cd android-custom-lint-rules
```

##### Build the code

For Android Studio 3.x and above, use the sample in `android-studio-3`.
If you are targeting Android Studio 2.x and older, use the sample in `android-studio-2`.

Support
-------

- The "lint-dev" Google group: https://groups.google.com/forum/#!forum/lint-dev
- Google+ Community: https://plus.google.com/communities/105153134372062985968
- Stack Overflow: http://stackoverflow.com/questions/tagged/android

If you've found an error in this sample, please file an issue:
https://github.com/googlesamples/android-custom-lint-rules/issues

Patches are encouraged, and may be submitted by forking this project and
submitting a pull request through GitHub.

License
-------
Licensed under the Apache 2.0 license. See the LICENSE file for details.

How to make contributions?
--------------------------
Please read and follow the steps in the CONTRIBUTING.md
