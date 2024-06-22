package com.thekr

import android.app.Application
import com.thekr.data.settings.SettingsDetails
import com.thekr.database.AppContainer
import com.thekr.database.AppDataContainer
import com.thekr.database.JsonParser.importDataFromJson
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull

class ZekrApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    private lateinit var container: AppContainer

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


        // Initialize settingsDataStore
        appCoroutineScope.launch {
//            val settingsDataStore = applicationContext.settingsDataStore
//            val settings = settingsDataStore.data.firstOrNull() ?: return@launch
//            if (settings.initialized.not() && settings.dbInitialized.not()) {
            importDataFromJson()
//                settingsDataStore.updateData {
//                    SettingsDetails().toSettings()
//                }
//            } else {
//                // Start fingerprint logging in the background
//                // todo: save this as a job to be able to cancel it
//                if (settings.fingerPrintControl) {
//                    FingerPrintLogcatProcessor.startMonitoring()
//                }
//            }
        }
    }
}


