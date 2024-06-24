rootProject.name = "Thekr"
include(":composeApp", ":resources")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()

        maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
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
