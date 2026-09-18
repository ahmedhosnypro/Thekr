import com.android.build.api.dsl.ManagedVirtualDevice
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)

    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)

    // id("org.jetbrains.compose.hot-reload") version "1.0.0-dev-63"
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    // Per-module baseline: detektBaseline task overwrites the file, so a
    // shared path would clobber the other module's entries.
    baseline = file("$rootDir/config/detekt/baseline-composeApp.xml")
    source.setFrom(
        files(
            "$projectDir/src/commonMain/kotlin",
            "$projectDir/src/jvmMain/kotlin",
            "$projectDir/src/androidMain/kotlin",
        ),
    )
    parallel = true
}

ktlint {
    version.set("1.5.0")
    // Pre-existing findings live in the baseline; only new code must be clean.
    baseline.set(file("$rootDir/config/ktlint/baseline-composeApp.xml"))
}

val nameSpace = "com.thekr"

kotlin {
//    jvm("desktop")
    jvm()
    targets.all {
        compilations.all {
            compileTaskProvider {
                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }
    }

//    androidTarget {
//        @OptIn(ExperimentalKotlinGradlePluginApi::class)
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_21)
//        }
//    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.add("-Xjdk-release=${JavaVersion.VERSION_21}")
        }

        // https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
        // @OptIn(ExperimentalKotlinGradlePluginApi::class)
        // instrumentedTestVariant {
        //     sourceSetTree.set(KotlinSourceSetTree.test)
        //     dependencies {
        //        debugImplementation(libs.androidx.compose.ui.test.manifest)
        //         implementation(libs.androidx.compose.ui.test.junit4)
        //     }
        // }
    }

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

        androidMain.dependencies {
//            implementation(compose.preview)
            implementation(libs.compose.ui.tooling)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.bundles.libsu)
            implementation(libs.androidx.appcompat)

            implementation(libs.androidx.ui.tooling.preview)
        }
        commonMain.dependencies {
            implementation(libs.compose.ui)
            implementation(libs.compose.runtime)
            implementation(libs.compose.material)
            implementation(libs.compose.material3)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material.icons.extended)

            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)

            implementation(libs.voyager.navigator)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.composeIcons.featherIcons)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.multiplatformSettings)
            implementation(libs.koin.core)
            implementation(libs.kstore)
            implementation(libs.kstore.file)

            // okio was previously only on the android/jvm compile classpath as a
            // transitive of ktor-client-okhttp; both platform KSettingStore
            // siblings import okio.Path explicitly, so declare it directly.
            implementation(libs.okio)

            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.navigation.compose)

            implementation(libs.moko.mvvm)

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            implementation(libs.kotlinx.io.core)
            implementation(libs.appdirs)

            implementation(libs.korlibs.audio)
            implementation(libs.korlibs.time)
            implementation(libs.sdp.ssp.compose.multiplatform)

            implementation(libs.constraintlayout.compose.multiplatform)
            implementation(libs.bundles.vico)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = nameSpace
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res")
//        resources.srcDirs("src/commonMain/resources")
    }

    defaultConfig {
        applicationId = nameSpace
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()

        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        create("release") {
            // Release builds read the keystore path + credentials from env vars
            // (KEYSTORE_PATH, KEYSTORE_PASSWORD, KEY_ALIAS, KEY_PASSWORD); when no
            // keystore is provided, fall back to the git-ignored local ks.jks for
            // local dev, otherwise leave the release build unsigned.
            val envPath = System.getenv("KEYSTORE_PATH")?.takeIf { it.isNotBlank() }
            val localPath = file("${rootProject.projectDir}/ks.jks").takeIf { it.exists() }
            val keystorePath = envPath?.let { file(it) } ?: localPath
            if (keystorePath != null) {
                storeFile = keystorePath
                storePassword = System.getenv("KEYSTORE_PASSWORD")?.takeIf { it.isNotBlank() } ?: "123456"
                keyAlias = System.getenv("KEY_ALIAS")?.takeIf { it.isNotBlank() } ?: "key0"
                keyPassword = System.getenv("KEY_PASSWORD")?.takeIf { it.isNotBlank() } ?: "123456"
                if (envPath == null) {
                    // ks.jks is the weak development-only key: it must never
                    // silently sign a distributable release. CI is unaffected
                    // (it fails closed without the secret) and env-provided
                    // keystores skip this warning. No secret values printed.
                    logger.lifecycle(
                        "WARNING: the release signing config fell back to the weak " +
                            "local dev keystore 'ks.jks' instead of a release key. " +
                            "Set KEYSTORE_PATH, KEYSTORE_PASSWORD, KEY_ALIAS and " +
                            "KEY_PASSWORD to sign a real release (see " +
                            ".github/workflows/build-release.yml).",
                    )
                }
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            // R8 needs the Android optimize baseline plus our keeps (serializers/Room);
            // without proguardFiles, minification strips reflection-discovered classes.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            // Release ships device ABIs only; debug stays unfiltered for x86 emulators.
            ndk {
                abiFilters += listOf("arm64-v8a", "armeabi-v7a")
            }
            if (signingConfigs.getByName("release").storeFile != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        // enables a Compose tooling support in the AndroidStudio
        compose = true
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
    // https://developer.android.com/studio/test/gradle-managed-devices
    @Suppress("UnstableApiUsage")
    testOptions {
        managedDevices.allDevices {
            maybeCreate<ManagedVirtualDevice>("pixel5").apply {
                device = "Pixel 5"
                apiLevel = 34
                systemImageSource = "aosp"
            }
        }
    }
}

// compose {
//    tasks {
//        withType<AndroidLintAnalysisTask> {
//            enabled = false
//        }
//    }
// }

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.thekr"
            packageVersion = "1.0.0"
        }

        buildTypes.release.proguard {
            configurationFiles.from(project.file("proguard-rules.pro"))
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "$nameSpace.resources"
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":baselineprofile"))
    //    implementation(libs.androidx.room.ktx)

    // detekt-formatting gives detekt the ktlint-equivalent formatting rules (wrapping, spacing, imports).
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")

    with(libs.room.compiler) {
        add("kspAndroid", this)
        add("kspJvm", this)
        //        add("kspIosX64", this)
        //        add("kspIosArm64", this)
        //        add("kspIosSimulatorArm64", this)
    }
}

composeCompiler {
//    featureFlags.add(ComposeFeatureFlag.OptimizeNonSkippingGroups)
}
