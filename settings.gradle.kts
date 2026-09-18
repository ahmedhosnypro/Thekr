@file:Suppress("UnstableApiUsage")

rootProject.name = "Thekr"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()

        // Load-bearing for the libsu fingerprint feature: com.github.topjohnwu.libsu
        // resolves from jitpack.io only (probed 2026-09-18; not on Maven Central
        // under these coordinates — Central publishes it as com.github.topjohnwu:libsu).
        maven("https://jitpack.io")
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("de.fayard.refreshVersions") version "0.60.6"
}


include(":composeApp")
include(":baselineprofile")
