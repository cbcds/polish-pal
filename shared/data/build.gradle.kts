plugins {
    id("com.cbcds.polishpal.multiplatform.lib")
    id("com.cbcds.polishpal.compose")
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.cbcds.polishpal.data"
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.datastore.preferences)

    commonMainImplementation(compose.runtime)
    commonMainImplementation(libs.kotlinx.datetime)
    commonMainImplementation(libs.koin.core)
    commonMainImplementation(libs.room.runtime)
    kspCommonMainMetadata(libs.room.compiler)

    androidMainImplementation(libs.androidx.appcompat)
    androidMainImplementation(libs.koin.android)
    kspAndroid(libs.room.compiler)
}
