package com.thekr

import ca.gosyer.appdirs.AppDirs
import com.thekr.data.proto.Settings
import com.thekr.data.settingsStore
import com.thekr.database.AppContainer
import com.thekr.database.AppDataContainer
import com.thekr.database.JsonParser.importDataFromJson
import com.thekr.database.getDatabaseBuilder
import com.thekr.di.DatabaseProvider
import com.thekr.di.appStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object JvmApplication {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    var container: AppContainer = AppDataContainer()
    private val appCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        // Initialize database
        DatabaseProvider.initDatabase(getDatabaseBuilder())

        // Initialize settingsDataStore
        val userDataDir = AppDirs("thekr", "thekr").getUserDataDir()
        appStorage = userDataDir

        appCoroutineScope.launch {
            val settings = settingsStore.get()
            if (settings == null) {
                kotlinx.io.files.SystemFileSystem.run {
                    createDirectories(
                        path = kotlinx.io.files.Path(userDataDir)
                    )
                }


                settingsStore.set(Settings(initialized = true))
                importDataFromJson()
            } else if (settings.dbInitialized.not()) {
                importDataFromJson()
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
}


