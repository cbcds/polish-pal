plugins {
    id("com.cbcds.polishpal.multiplatform.lib")
    id("com.cbcds.polishpal.compose")
}

android {
    namespace = "com.cbcds.polishpal.core.navigation"
}

dependencies {
    commonMainApi(libs.voyager.tab.navigator)
    commonMainImplementation(compose.foundation)
    commonMainImplementation(compose.components.resources)
    commonMainImplementation(libs.voyager.screenmodel)
}
