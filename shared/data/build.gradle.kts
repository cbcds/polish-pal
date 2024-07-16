plugins {
    id("com.cbcds.polishpal.multiplatform.lib")
}

android {
    namespace = "com.cbcds.polishpal.data"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)

    commonMainImplementation(libs.kotlinx.datetime)
    commonMainImplementation(libs.koin.core)

    androidMainImplementation(libs.androidx.appcompat)
    androidMainImplementation(libs.koin.android)
}
