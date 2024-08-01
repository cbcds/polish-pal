plugins {
    id("com.cbcds.polishpal.multiplatform.lib")
    id("com.cbcds.polishpal.compose")
}

android {
    namespace = "com.cbcds.polishpal.core.grammar"
}

dependencies {
    commonMainImplementation(project(":shared:data"))
    commonMainImplementation(project(":shared:core:ui"))

    commonMainImplementation(compose.components.resources)
}

compose.resources {
    packageOfResClass = "com.cbcds.polishpal.shared.core.grammar"
    publicResClass = true
}
