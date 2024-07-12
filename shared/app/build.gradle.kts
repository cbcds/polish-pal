plugins {
    id("com.cbcds.polishpal.multiplatform.lib")
    id("com.cbcds.polishpal.compose")
}

android {
    namespace = "com.cbcds.polishpal.shared.app"
}

dependencies {
    commonMainImplementation(project(":shared:core:ui"))

    commonMainImplementation(compose.components.resources)
    commonMainImplementation(libs.voyager.navigator)
    commonMainImplementation(libs.voyager.screenmodel)
    commonMainImplementation(libs.voyager.tab.navigator)
}

compose.resources {
    packageOfResClass = "com.cbcds.polishpal.shared.app"
    publicResClass = false
}
