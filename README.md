Custom Lint Rules
=================

The lint source code contains a lot of documentation on how to write
custom checks; this git repository contains a snapshot of this
documentation which you can read here:

* [Full API Guide](https://googlesamples.github.io/android-custom-lint-rules/api-guide.html)
* [Other docs](https://googlesamples.github.io/android-custom-lint-rules/index.html)

Lint
----

The [Android `lint` tool](http://developer.android.com/tools/help/lint.html) is a static code
analysis tool that checks your project source files for potential bugs and optimization
improvements for correctness, security, performance, usability, accessibility, and
internationalization. Lint comes with around 400 built-in checks, but it can be extended with 
additional custom checks. This sample project shows how those sample checks can be built
and packaged.

Note that while Android Lint has the name "Android" in it, it is no longer an Android-specific
static analysis tool; it's a general static analysis tool, and inside Google for example it is
run to analyze server-side Java and Kotlin code.

**NOTE: The lint API is not a final API; if you rely on this be prepared
 to adjust your code for the next tools release.**

Introduction
------------

The Android Lint API allows users to create custom lint checks. For example, if you are the author of
an Android library project, and your library project has certain usage requirements, you can write
additional lint rules to check that your library is used correctly, and then you can distribute
those extra lint rules for users of the library. Similarly, you may have company-local rules you'd
like to enforce.

This sample demonstrates how to create a custom lint checks and corresponding tests for those rules.


# Sample Lint Checks

This project shows how Android Studio as well as the Android Gradle plugin handles packaging of lint
rules.

## Lint Check Jar Library

First, there's the lint check implementation itself. That's done in the
"checks" project, which just applies the Gradle "java" or "kotlin" plugins, and
that project produces a jar. Note that the dependencies for the lint
check project (other than its testing dependencies) must all be "compileOnly":

    dependencies {
        compileOnly "com.android.tools.lint:lint-api:$lintVersion"
        compileOnly "com.android.tools.lint:lint-checks:$lintVersion"
		...

## Lint Check AAR Library

Next, there's a separate Android library project, called "library". This
library doesn't have any code on its own (though it could). However,
in its build.gradle, it specifies this:

    dependencies {
        lintPublish project(':checks')
    }

This tells the Gradle plugin to take the output from the "checks" project
and package that as a "lint.jar" payload inside this library's AAR file.
When that's done, any other projects that depends on this library will
automatically be using the lint checks.

## App Modules

Note that you don't have to go through the extra "library indirection"
if you have a lint check that you only want to apply to one or more
app modules. You can simply include the `lintChecks` dependency as shown
above there as well, and then lint will include these rules when analyzing
the project.

## Lint Version

The lint version of the libraries (specified in this project as the
`lintVersion` variable in build.gradle) should be the same version
that is used by the Gradle plugin.

If the Gradle plugin version is *X*.*Y*.*Z*, then the Lint library
version is *X+23*.*Y*.*Z*.

For example, for AGP 7.0.0-alpha08, the lint API versions are 30.0.0-alpha08.

Getting Started
---------------

##### Fetch code

```
git clone https://github.com/googlesamples/android-custom-lint-rules.git
cd android-custom-lint-rules
```

##### Run The Sample

Run the :app:lint target to have first the custom lint checks in checks/
compiled, then wrapped into the library, and finally run lint on a
sample app module which has violations of the check enforced by sample
check in this project:
```
$ ./gradlew :app:lint

> Task :app:lintDebug

Scanning app: ...
Wrote HTML report to file:///demo/android-custom-lint-rules/app/build/reports/lint-results-debug.html
Wrote SARIF report to file:///demo/android-custom-lint-rules/app/build/reports/lint-results-debug.sarif

/demo/android-custom-lint-rules/app/src/main/java/com/android/example/Test.kt:8: Warning: This code mentions lint: Congratulations [ShortUniqueId]
    val s = "lint"
             ~~~~

   Explanation for issues of type "ShortUniqueId":
   This check highlights string literals in code which mentions the word lint.
   Blah blah blah.

   Another paragraph here.

   Vendor: Android Open Source Project
   Contact: https://github.com/googlesamples/android-custom-lint-rules
   Feedback: https://github.com/googlesamples/android-custom-lint-rules/issues

0 errors, 1 warnings

BUILD SUCCESSFUL in 1s
```

##### Lint Dependencies

When building your own rules, you will likely want to know which dependencies you should 
bring into your own project. The below descriptions of the dependencies included within
this project serve to help you make that decision:

Source Dependencies

- **com.android.tools.lint:lint-api**: The most important one; it contains things 
  like `LintClient`, the `Detector` base class, the `Issue` class, and everything else 
  that Lint checks rely on in the Lint framework.
- **com.android.tools.lint:lint-checks**: Contains the built-in checks that are developed 
  internally. Also contains utilities that are sometimes useful for other lint checks, 
  such as the `VersionChecks` class (which figures out whether a given UAST element is 
  known to only be called at a given API level, either by surrounding `if >= SDK-version`
  checks or `if < SDK-version` early returns in the method).

Test Dependencies

- **com.android.tools.lint:lint-tests**: Contains useful utilities for writing unit tests 
  for Lint checks, including the `LintDetectorTest` base class.
- **com.android.tools.lint:lint**: Lint checks don't need to depend on this. It's a 
  separate artifact used by tools that want to integrate lint with the command line, 
  such as the Gradle integration of lint. This is where things like terminal output, HTML 
  reporting, command line parsing etc is handled.

The APIs in all but the lint-api artifact are more likely to change incompatibly than
the lint-api artifact.

Support
-------

- The "lint-dev" Google group: https://groups.google.com/forum/#!forum/lint-dev
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
