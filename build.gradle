buildscript {
    ext {
        kotlinVersion = '1.4.31'

        // Current lint target: Studio 4.2 / AGP 7
        //gradlePluginVersion = '4.2.0-beta05'
        //lintVersion = '27.2.0-beta05'

        // Upcoming lint target: Arctic Fox / AGP 7
        gradlePluginVersion = '7.0.0-alpha10'
        lintVersion = '30.0.0-alpha10' // if gradle plugin was 4.1.2, you'd use 27.1.2 here
    }

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:$gradlePluginVersion"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
