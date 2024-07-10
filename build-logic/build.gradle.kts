plugins {
    `kotlin-dsl`
}

group = "com.cbcds.polishpal.gradle"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.detekt.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("android-app") {
            id = "com.cbcds.polishpal.android.app"
            implementationClass = "AndroidAppConventionPlugin"
        }

        register("android-library") {
            id = "com.cbcds.polishpal.android.lib"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("multiplatform-library") {
            id = "com.cbcds.polishpal.multiplatform.lib"
            implementationClass = "MultiplatformLibraryConventionPlugin"
        }

        register("compose") {
            id = "com.cbcds.polishpal.compose"
            implementationClass = "ComposeConventionPlugin"
        }

        register("detekt") {
            id = "com.cbcds.polishpal.detekt"
            implementationClass = "DetektConventionPlugin"
        }
    }
}
