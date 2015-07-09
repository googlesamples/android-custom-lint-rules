Custom Lint Rules
============

The [Android `lint` tool](http://developer.android.com/tools/help/lint.html) is a static code
 analysis tool that checks your Android project source files for potential bugs and optimization
 improvements for correctness, security, performance, usability, accessibility, and
 internationalization. Lint comes with over 100 checks, however it can be extended with additional
 custom rules.

The Custom Lint Rules API is **not stable** and is subject to change in the future. Be ready to change
 your custom lint rule implementation when the API changes.

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

`git clone https://github.com/googlesamples/custom-lint-rules.git`

##### Build the validator

`./gradlew build`

##### Copy to the lint directory

`cp ./build/libs/custom-lint-rules.jar ~/.android/lint/`

##### Verify whether the issues are registered with lint

`lint --show MainActivityDetector`

##### Run lint

`lint`

> Note: If you can't run `lint` directly, you may want to include android tools `PATH` in your
 `~/.bash_profile`.
> (i.e. `PATH=$PATH:~/Library/Android/sdk/tools`)
>
> Then run `source ~/.bash_profile`.

Support
-------

- Google+ Community: https://plus.google.com/communities/android
- Stack Overflow: http://stackoverflow.com/questions/tagged/android

If you've found an error in this sample, please file an issue:
https://github.com/googlesamples/custom-lint-rules/issues

Patches are encouraged, and may be submitted by forking this project and
submitting a pull request through GitHub.

License
-------
Licensed under the Apache 2.0 license. See the LICENSE file for details.

How to make contributions?
--------------------------
Please read and follow the steps in the CONTRIBUTING.md
