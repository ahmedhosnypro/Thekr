import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import com.android.build.api.dsl.ManagedVirtualDevice
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.apollo)
}

kotlin {
    targets.all {
        compilations.all {
            compileTaskProvider {
                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }

        }
    }
    androidTarget {
        compilations.all {
            compileTaskProvider {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                    freeCompilerArgs.add("-Xjdk-release=${JavaVersion.VERSION_1_8}")
                }
            }
        }
        //https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant {
            sourceSetTree.set(KotlinSourceSetTree.test)
            dependencies {
                debugImplementation(libs.androidx.testManifest)
                implementation(libs.androidx.junit4)
            }
        }
    }

    jvm()

//    wasmJs {
//        browser()
//        binaries.executable()
//    }

//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach {
//        it.binaries.framework {
//            baseName = "ComposeApp"
//            isStatic = true
    // Required when using NativeSQLiteDriver
//    linkerOpts.add("-lsqlite3")
//        }
//    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.voyager.navigator)
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)
            implementation(libs.napier)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.core)
            implementation(libs.composeIcons.featherIcons)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.multiplatformSettings)
            implementation(libs.koin.core)
            implementation(libs.kstore)
            implementation(libs.apollo.runtime)

            implementation(libs.moko.mvvm)

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.bundles.libsu)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
        }

//        iosMain.dependencies {
//            implementation(libs.ktor.client.darwin)
//        }

    }
}

android {
    namespace = "com.thekr"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34

        applicationId = "com.thekr.androidApp"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res")
    }
    //https://developer.android.com/studio/test/gradle-managed-devices
    @Suppress("UnstableApiUsage")
    testOptions {
        managedDevices.devices {
            maybeCreate<ManagedVirtualDevice>("pixel5").apply {
                device = "Pixel 5"
                apiLevel = 34
                systemImageSource = "aosp"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        //enables a Compose tooling support in the AndroidStudio
        compose = true
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.thekr.desktopApp"
            packageVersion = "1.0.0"
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

buildConfig {
    // BuildConfig configuration here.
    // https://github.com/gmazzo/gradle-buildconfig-plugin#usage-in-kts
}

room {
    schemaDirectory("$projectDir/schemas")
}

apollo {
    service("api") {
        // GraphQL configuration here.
        // https://www.apollographql.com/docs/kotlin/advanced/plugin-configuration/
        packageName.set("com.thekr.graphql")
    }
}

dependencies {
    implementation(libs.androidx.room.ktx)
    ksp(libs.room.compiler)

//    with(libs.room.compiler) {
//        add("kspAndroid", this)
//        add("kspJvm", this)
//        add("kspIosX64", this)
//        add("kspIosArm64", this)
//        add("kspIosSimulatorArm64", this)
//    }
}
