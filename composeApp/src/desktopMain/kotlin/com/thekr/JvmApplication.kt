package com.thekr

import ca.gosyer.appdirs.AppDirs
import com.thekr.database.AppContainer
import com.thekr.database.AppDataContainer
import com.thekr.database.getDatabaseBuilder
import com.thekr.di.DatabaseProvider
import com.thekr.di.appStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object JvmApplication {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    var container: AppContainer = AppDataContainer()
    private val appCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val userDataDir = AppDirs("thekr", "thekr").getUserDataDir()

    init {
        // Initialize database
        DatabaseProvider.initDatabase(getDatabaseBuilder())

        appStorage = userDataDir

            //            } else {
//                // Start fingerprint logging in the background
//                // todo: save this as a job to be able to cancel it
//                if (settings.fingerPrintControl) {
//                    FingerPrintLogcatProcessor.startMonitoring()
//                }
//            }
    }
}


