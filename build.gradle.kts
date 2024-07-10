plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.detekt) apply true
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false

    id("com.cbcds.polishpal.detekt")
}
