package utils

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureKotlinAndroid() {
    extensions.configure<KotlinMultiplatformExtension> {
        androidTarget {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            compilerOptions {
                jvmTarget.set(Java.jvmTarget)
            }
        }
    }
}

internal fun Project.configureKotlinIos() {
    extensions.configure<KotlinMultiplatformExtension> {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }
}

internal fun Project.addKotlinUtilsDependencyIfNeeded() {
    if (name != "kotlin-utils") {
        dependencies {
            add("implementation", project(":shared:core:kotlin-utils"))
        }
    }
}
