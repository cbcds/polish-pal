plugins {
    id("com.cbcds.polishpal.android.app")
    id("com.cbcds.polishpal.compose")
}

android {
    namespace = "com.cbcds.polishpal"

    defaultConfig {
        applicationId = "com.cbcds.polishpal"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    androidMainImplementation(project(":shared:app"))

    androidMainImplementation(libs.androidx.activity.compose)
    androidMainImplementation(compose.preview)
}
