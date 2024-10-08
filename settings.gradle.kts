rootProject.name = "polish-pal"

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":android-app")
include(":shared:app")
include(":shared:core:kotlin-utils")
include(":shared:core:navigation")
include(":shared:core:ui")
include(":shared:core:ui:grammar")
include(":shared:data")
include(":shared:data:preferences")
include(":shared:feature")
include(":shared:feature:exercises")
include(":shared:feature:settings")
include(":shared:feature:vocabulary")
