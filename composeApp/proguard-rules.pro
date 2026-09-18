# Compose Desktop ProGuard Rules
# These rules suppress warnings for optional dependencies that are not included in desktop builds

# Reactor BlockHound - Optional debugging tool for detecting blocking calls
-dontwarn reactor.blockhound.**

# JUnit 4 & JUnit 5 - Testing frameworks (not needed in production)
-dontwarn org.junit.**
-dontwarn org.junit.jupiter.**
-dontwarn org.junit.platform.**
-dontwarn org.junit.rules.**
-dontwarn org.junit.runner.**
-dontwarn org.junit.runners.**

# GraalVM Native Image - Optional native compilation support
-dontwarn org.graalvm.**
-dontwarn com.oracle.svm.**

# FindBugs annotations - Optional static analysis annotations
-dontwarn edu.umd.cs.findbugs.annotations.**

# Kotlin concurrent atomics - Optional multiplatform atomic operations
-dontwarn kotlin.concurrent.atomics.**

# Kotlin internal enhanced nullability annotations
-dontwarn kotlin.jvm.internal.EnhancedNullability

# Keep main entry point
-keep class MainKt {
    public static void main(java.lang.String[]);
}

# Keep all classes in your app package (but allow ProGuard to process them)
-keep,allowoptimization,allowobfuscation class com.thekr.** { *; }

# Keep Kotlin metadata for reflection
-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*

# Keep kotlinx.serialization — serializer discovery rules from the official
# kotlinx.serialization R8/ProGuard rules (Kotlin/kotlinx.serialization README,
# "Android" section): plugin-generated $serializer classes, the Companion field
# holding serializer(), and the serializer() accessor itself. Generalized from
# com.thekr.** to ** so library-serializable classes survive too.
-dontnote kotlinx.serialization.AnnotationsKt
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
-keep,includedescriptorclasses class **$$serializer { *; }
-keepclassmembers class ** {
    *** Companion;
}
-keepclasseswithmembers class ** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep all library classes that are referenced by your app classes
-keep class kotlinx.datetime.** { *; }
-keep interface kotlinx.datetime.** { *; }
-keep class org.jetbrains.compose.resources.** { *; }
-keep class korlibs.audio.sound.** { *; }
-keep class com.patrykandpatrick.vico.** { *; }

# Keep Room database classes — per Room's official shrinking guidance
# (developer.android.com — "Shrink, obfuscate, and optimize your app" / Room docs,
# entities and RoomDatabase subclasses are looked up reflectively). Room's AAR
# ships consumer rules; these are an explicit safety net for KMP-generated code.
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }

# Suppress warnings about missing classes in reflection usage
-dontwarn java.lang.invoke.StringConcatFactory

# Suppress specific method reference warnings
-dontwarn korlibs.ffi.FFILib_jvmKt

# Keep JNA classes used by korlibs
-keep class com.sun.jna.** { *; }

# Ignore warnings about missing optional methods/fields - these are safe to ignore
# as they are checked at runtime by the libraries
-ignorewarnings
