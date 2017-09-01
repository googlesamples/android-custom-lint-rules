# Sample Lint Checks

This project shows how Android Studio 3 (beta 5 and later) handles packaging
of lint rules.

## Lint Check Jar Library

First, there's the lint check implementation itself. That's done in the
"checks" project, which just applies the Gradle "java" plugin, and
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
        lintChecks project(':checks')
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
