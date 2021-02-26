plugins {
    id 'com.android.application'
    id 'kotlin-android'
}

android {
    compileSdkVersion 30

    defaultConfig {
        applicationId "com.android.example.lint_usage"
        minSdkVersion 21
        targetSdkVersion 30
        versionCode 1
        versionName "1.0"
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
    lintOptions {
        textReport true

        // Produce report for CI:
        // https://docs.github.com/en/github/finding-security-vulnerabilities-and-errors-in-your-code/sarif-support-for-code-scanning
        sarifOutput file("../lint-results.sarif")
    }
}

dependencies {
    implementation project(':library')
}

