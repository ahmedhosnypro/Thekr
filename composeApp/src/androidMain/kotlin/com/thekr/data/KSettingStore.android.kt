package com.thekr.data

import com.thekr.data.proto.Settings
import com.thekr.database.JsonParser.importDataFromJson
import com.thekr.di.appStorage
import com.thekr.di.settingsFile
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import okio.Path.Companion.toPath

actual val settingsStore: KStore<Settings> by lazy {
    storeOf("$appStorage/$settingsFile".toPath())
}

actual suspend fun initAppData() {
    val settings = settingsStore.get()

    if (settings == null) {
        settingsStore.set(Settings(initialized = true))
        importDataFromJson()
    } else if (settings.dbInitialized.not()) {
        importDataFromJson()
    }
}