plugins {
    id("com.cbcds.polishpal.multiplatform.lib")
    id("com.cbcds.polishpal.compose")
}

android {
    namespace = "com.cbcds.polishpal.app"
}

dependencies {
    commonMainApi(project(":shared:feature:settings"))
    commonMainImplementation(project(":shared:feature:vocabulary"))
    commonMainImplementation(project(":shared:core:grammar"))
    commonMainImplementation(project(":shared:core:navigation"))
    commonMainImplementation(project(":shared:core:ui"))
    commonMainImplementation(project(":shared:data"))

    commonMainImplementation(compose.components.resources)
    commonMainImplementation(libs.koin.core)
    commonMainImplementation(libs.koin.compose)
}

compose.resources {
    packageOfResClass = "com.cbcds.polishpal.shared.app"
    publicResClass = false
}
