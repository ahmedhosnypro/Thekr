package com.thekr

import android.app.Application
import android.content.Context
import android.util.Log
import com.thekr.database.AppContainer
import com.thekr.database.AppDataContainer
import com.thekr.database.initDatabaseIfNeeded
import com.thekr.di.appStorage
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.io.path.Path

class ThekrApplication : Application() {
    /** AppContainer instance used by the rest of classes to get dependencies */
    lateinit var container: AppContainer

    companion object {
        lateinit var appContext: Context

        /** crash_log.txt is append-only and cloud-backed-up: keep it bounded. */
        private const val CRASH_LOG_MAX_BYTES = 256L * 1024

        /** When the cap is exceeded, keep only this much of the newest content. */
        private const val CRASH_LOG_KEEP_BYTES = 128 * 1024
    }

    init {
        // Initialize Shell only once. Per libsu-recommended practice, configure
        // the builder before any shell is created. isAppGrantedRoot() is FALSE
        // on devices with no executable su on PATH, null (unknown) when su
        // exists, and true for uid-0 — so "!= true" applies this config to
        // rooted and non-rooted/unknown devices alike.
        if (Shell.isAppGrantedRoot() != true) {
//            Shell.enableLegacyStderrRedirection = true
            Shell.setDefaultBuilder(
                Shell.Builder.create()
                    .setFlags(Shell.FLAG_REDIRECT_STDERR)
                    .setTimeout(10),
            )
        }
        // No need to get the shell instance here
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        container = AppDataContainer(this)

        // Set up global uncaught exception handler (before any background
        // work starts, so a warm-up failure is still logged here)
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("ThekrApp", "Uncaught exception in thread: ${thread.name}", throwable)
            runCatching {
                val crashFile = filesDir.resolve("crash_log.txt")
                // Bound the log before appending: a crash-looping device
                // grows it on every launch, and the file is included in
                // cloud backups. Keep only the newest tail when over cap.
                if (crashFile.exists() && crashFile.length() > CRASH_LOG_MAX_BYTES) {
                    val tail =
                        crashFile.readText()
                            .takeLast(CRASH_LOG_KEEP_BYTES)
                            .dropWhile { it != '\n' }
                            .drop(1)
                    crashFile.writeText(tail)
                }
                val crashFilePath = crashFile.absolutePath
                val logEntry = "\n---\n${java.util.Date()}\nThread: ${thread.name}\n${throwable.stackTraceToString()}"
                Path(crashFilePath).toFile().appendText(logEntry)
                Log.i("ThekrApp", "Crash log written to: $crashFilePath")
            }.onFailure {
                Log.e("ThekrApp", "Failed to write crash log", it)
            }
            // Delegate to the previous handler so the process still dies
            // normally instead of being left running in a broken state
            defaultHandler?.uncaughtException(thread, throwable)
        }

        // Build the Room database off the main thread; first access is gated by
        // initDatabaseIfNeeded, so this only warms the build up. A build
        // failure must not kill the process — the first real access retries.
        CoroutineScope(Dispatchers.Default).launch {
            runCatching { initDatabaseIfNeeded(applicationContext) }
                .onFailure { Log.e("ThekrApp", "Database warm-up failed", it) }
        }
        appStorage = filesDir.path
    }
}
