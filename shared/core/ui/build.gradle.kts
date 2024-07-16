plugins {
    id("com.cbcds.polishpal.multiplatform.lib")
    id("com.cbcds.polishpal.compose")
}

android {
    namespace = "com.cbcds.polishpal.core.ui"
}

dependencies {
    commonMainApi(compose.foundation)
    commonMainApi(compose.material3)
    commonMainApi(compose.components.uiToolingPreview)

    commonMainImplementation(compose.components.resources)
}

compose.resources {
    packageOfResClass = "com.cbcds.polishpal.shared.core.ui"
    publicResClass = true
}
