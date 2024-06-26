package com.thekr

import android.app.Application
import android.content.Context
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

    companion object {
        lateinit var appContext: Context
    }

    init {
        // Initialize Shell only once
        if (Shell.isAppGrantedRoot() == false) {

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

        appContext = applicationContext

        container = AppDataContainer(this)

        // Initialize database
        DatabaseProvider.initDatabase(getDatabaseBuilder(this))
        appStorage = filesDir.path
    }
}


