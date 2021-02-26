apply plugin: 'com.android.library'

android {
    compileSdkVersion 30
    defaultConfig {
        minSdkVersion 15
        targetSdkVersion 30
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

/** Package the given lint checks library into this AAR  */
dependencies {
    lintPublish project(':checks')
}
