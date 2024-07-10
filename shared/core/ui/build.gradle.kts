plugins {
    id("com.cbcds.polishpal.multiplatform.lib")
    id("com.cbcds.polishpal.compose")
}

android {
    namespace = "com.cbcds.polishpal.shared.core.ui"
}

dependencies {
    commonMainImplementation(compose.material3)
    commonMainImplementation(compose.components.resources)
    commonMainImplementation(compose.components.uiToolingPreview)
}
