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

        resourceConfigurations += listOf("en", "pl", "be")
    }
}

dependencies {
    implementation(project(":shared:app"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.process)
    implementation(libs.koin.android)

    debugImplementation(compose.preview)
    debugImplementation(libs.compose.tracing)
}
