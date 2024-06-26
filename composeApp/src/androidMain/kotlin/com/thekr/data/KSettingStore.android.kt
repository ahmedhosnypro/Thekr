package com.thekr.data

import com.thekr.data.proto.Settings
import com.thekr.database.JsonParser.importDataFromJson
import com.thekr.di.appStorage
import com.thekr.di.settingsFile
import com.thekr.fingerprint.FingerPrintLogcatProcessor
import com.thekr.fingerprint.startMonitoring
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    CoroutineScope(Dispatchers.Default).launch {
        settingsStore.updates.collectLatest {
            if (it?.fingerPrintControl == true) {
                FingerPrintLogcatProcessor.startMonitoring()
            } else {
                // todo: save this as a job to be able to cancel it
//                FingerPrintLogcatProcessor.stopMonitoring()
            }
        }
    }
}