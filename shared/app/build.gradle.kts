plugins {
    id("com.cbcds.polishpal.multiplatform.lib")
    id("com.cbcds.polishpal.compose")
}

android {
    namespace = "com.cbcds.polishpal.shared.app"
}

dependencies {
    commonMainImplementation(project(":shared:core:ui"))

    commonMainImplementation(compose.foundation)
    commonMainImplementation(compose.components.uiToolingPreview)
}
