plugins {
    id("com.cbcds.polishpal.multiplatform.lib")
    id("com.cbcds.polishpal.compose")
}

android {
    namespace = "com.cbcds.polishpal.feature.settings"
}

dependencies {
    commonMainImplementation(project(":shared:core:navigation"))
    commonMainImplementation(project(":shared:core:ui"))
    commonMainImplementation(project(":shared:data"))

    commonMainImplementation(compose.components.resources)
    commonMainImplementation(libs.androidx.viewmodel)
    commonMainImplementation(libs.kotlinx.datetime)
    commonMainImplementation(libs.koin.core)
    commonMainImplementation(libs.koin.compose)

    androidMainImplementation(libs.accompanist.permissions)
}

compose.resources {
    packageOfResClass = "com.cbcds.polishpal.shared.feature.settings"
    publicResClass = false
}
