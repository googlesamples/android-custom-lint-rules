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

##### Lint Dependencies

When building your own rules, you will likely want to know which dependencies you should bring into your own project. 
The below descriptions of the dependencies included within this project serve to help you make that decision:

Source Dependencies

- **com.android.tools.lint:lint-api**: The most important one; it contains things like `LintClient`, the `Detector` 
base class, the `Issue` class, and everything else that Lint checks rely on in the Lint framework.
- **com.android.tools.lint:lint-checks**: Contains the built-in checks that are developed internally. Also contains 
utilities that are sometimes useful for other lint checks, such as the `VersionChecks` class (which figures out whether 
a given UAST element is known to only be called at a given API level, either by surrounding `if >= SDK-version` checks or 
`if < SDK-version` early returns in the method).

Test Dependencies

- **com.android.tools.lint:lint-tests**: Contains useful utilities for writing unit tests for Lint checks, 
including the `LintDetectorTest` base class.
- **com.android.tools:testutils**: It's unlikely that you need to depend on this directly. The test infrastructure 
depends on it indirectly though (the methods we use there were mostly for the older lint test infrastructure, 
not the newer one).  
- **com.android.tools.lint:lint**: Lint checks don't need to depend on this. It's a separate artifact used by tools 
that want to integrate lint with the command line, such as the Gradle integration of lint. This is where things like 
terminal output, HTML reporting, command line parsing etc is handled.


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
