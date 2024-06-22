rootProject.name = "Thekr"
include(":composeApp")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")

        maven("https://jitpack.io")
        maven("https://maven.pkg.github.com/bumble-org/appyx")
        maven("https://androidx.dev/storage/compose-compiler/repository/")
    }
}
