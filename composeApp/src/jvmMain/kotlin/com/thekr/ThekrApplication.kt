package com.thekr

import com.thekr.database.AppContainer
import com.thekr.database.AppDataContainer
import com.thekr.database.JsonParser.importDataFromJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object ThekrApplication {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer

    private val appCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    init {
        container = AppDataContainer()
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


