package com.thekr

import android.app.Application
import android.content.Context
import android.util.Log
import com.thekr.database.AppContainer
import com.thekr.database.AppDataContainer
import com.thekr.di.DatabaseProvider
import com.thekr.database.getDatabaseBuilder
import com.thekr.di.appStorage
import com.topjohnwu.superuser.Shell
import kotlin.io.path.Path

class ThekrApplication : Application() {
    /** AppContainer instance used by the rest of classes to get dependencies */
    lateinit var container: AppContainer

    companion object {
        lateinit var appContext: Context
    }

    init {
        // Initialize Shell only once
        if (Shell.isAppGrantedRoot() == false) {
            Shell.enableLegacyStderrRedirection = true
            Shell.setDefaultBuilder(
                Shell.Builder.create()
//                    .setFlags(Shell.FLAG_REDIRECT_STDERR)
                    .setTimeout(10)
            )
        }
        // No need to get the shell instance here
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        container = AppDataContainer(this)

        // Initialize database
        DatabaseProvider.initDatabase(getDatabaseBuilder(this))
        appStorage = filesDir.path

            // Set up global uncaught exception handler
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
            }
    }
}


