package com.thekr

import android.app.Application
import com.thekr.data.proto.Settings
import com.thekr.data.settingsStore
import com.thekr.database.AppContainer
import com.thekr.database.AppDataContainer
import com.thekr.di.DatabaseProvider
import com.thekr.database.JsonParser.importDataFromJson
import com.thekr.database.getDatabaseBuilder
import com.thekr.di.appStorage
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ThekrApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer

    private val appCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val shellInitiated = false

    init {
        // Initialize Shell only once
        if (shellInitiated.not() && Shell.isAppGrantedRoot() == false) {

            Shell.setDefaultBuilder(
                Shell.Builder.create()
                    .setFlags(Shell.FLAG_REDIRECT_STDERR)
                    .setTimeout(10)
            )
        }
        // No need to get the shell instance here
    }

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)

        // Initialize database
        DatabaseProvider.initDatabase(getDatabaseBuilder(this))

        // Initialize settingsDataStore
        appCoroutineScope.launch {
            appStorage = filesDir.path

            val settings = settingsStore.get()
//            val settings = settingsDataStore.data.firstOrNull() ?: return@launch
            if (settings != null && settings.initialized.not() && settings.dbInitialized.not()) {
                importDataFromJson()
                settingsStore.update {
                    Settings(
                        initialized = true,
                    )
                }
//            } else {
//                // Start fingerprint logging in the background
//                // todo: save this as a job to be able to cancel it
//                if (settings.fingerPrintControl) {
//                    FingerPrintLogcatProcessor.startMonitoring()
//                }
//            }
            } else if (settings != null && settings.dbInitialized.not()) {
                importDataFromJson()
            }
        }
    }
}


