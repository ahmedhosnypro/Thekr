package com.thekr.data

import ca.gosyer.appdirs.AppDirs
import com.thekr.JvmApplication.userDataDir
import com.thekr.data.proto.Settings
import com.thekr.database.JsonParser.importDataFromJson
import com.thekr.di.appStorage
import com.thekr.di.settingsFile
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okio.Path.Companion.toPath

actual val settingsStore: KStore<Settings> by lazy {
    storeOf("$appStorage/$settingsFile".toPath())
}

actual suspend fun initAppData() {
    // Initialize settingsDataStore
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
    }
}