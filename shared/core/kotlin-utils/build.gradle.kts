plugins {
    id("com.cbcds.polishpal.multiplatform.lib")
}

android {
    namespace = "com.cbcds.polishpal.core.kotlin"
}

dependencies {
    commonMainImplementation(libs.kotlinx.coroutines)
    commonMainImplementation(libs.koin.core)
}
