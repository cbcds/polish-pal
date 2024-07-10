package utils

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

internal object Java {

    val sourceCompatibility = JavaVersion.VERSION_11
    val targetCompatibility = JavaVersion.VERSION_11

    val jvmTarget = JvmTarget.JVM_11
}
