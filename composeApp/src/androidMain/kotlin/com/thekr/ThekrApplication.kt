package com.thekr

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.thekr.database.AppContainer
import com.thekr.database.AppDataContainer
import com.thekr.database.initDatabaseIfNeeded
import com.thekr.di.appStorage
import com.topjohnwu.superuser.Shell
import kotlin.io.path.Path
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThekrApplication : Application() {
    /** AppContainer instance used by the rest of classes to get dependencies */
    lateinit var container: AppContainer

    companion object {
        lateinit var appContext: Context
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
                    .setTimeout(10)
            )
        }
        // No need to get the shell instance here
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        container = AppDataContainer(this)

        // Build the Room database off the main thread; first access is gated by
        // initDatabaseIfNeeded, so this only warms the build up.
        CoroutineScope(Dispatchers.Default).launch {
            initDatabaseIfNeeded(applicationContext)
        }
        appStorage = filesDir.path

            // Set up global uncaught exception handler
            val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
                Log.e("ThekrApp", "Uncaught exception in thread: ${thread.name}", throwable)
                runCatching {
                    val crashFilePath = filesDir.resolve("crash_log.txt").absolutePath
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
    }
}


