plugins {
    id("com.cbcds.polishpal.multiplatform.lib")
}

android {
    namespace = "com.cbcds.polishpal.core.kotlin"
}

dependencies {
    commonMainApi(libs.kotlinx.collections.immutable)

    commonMainImplementation(libs.kotlinx.coroutines)
    commonMainImplementation(libs.koin.core)
}
