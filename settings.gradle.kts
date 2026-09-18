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

        // Load-bearing for the libsu fingerprint feature: jitpack.io is the ONLY
        // distribution channel for libsu (per the upstream topjohnwu/libsu README;
        // probed 2026-09-18 — Maven Central has no libsu under any group). The
        // only path off jitpack would be vendoring the artifacts locally or
        // switching libraries; see the follow-up findings before acting.
        maven("https://jitpack.io")
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("de.fayard.refreshVersions") version "0.60.6"
}


include(":composeApp")
include(":baselineprofile")
