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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okio.Path.Companion.toPath
import kotlinx.io.files.Path as KxPath
import java.util.concurrent.atomic.AtomicBoolean

actual val settingsStore: KStore<Settings> by lazy {
//    storeOf("$appStorage/$settingsFile".toPath())
    storeOf(KxPath("$appStorage/$settingsFile".toPath().toString()))
}

actual suspend fun initAppData() {
    val settings = settingsStore.get()

    if (settings == null) {
        settingsStore.set(Settings(initialized = true))
        importDataFromJson()
    } else if (settings.dbInitialized.not()) {
        importDataFromJson()
    }

    // React only to transitions of fingerPrintControl: every unrelated settings
    // update must not spawn yet another logcat shell (startMonitoring never
    // cancels the previous one). initAppData runs once per ViewModel
    // recreation, so guard the collector to start at most once per process —
    // otherwise each recreation piles up another never-cancelled collector.
    if (fingerprintCollectorStarted.compareAndSet(false, true)) {
        CoroutineScope(Dispatchers.Default).launch {
            settingsStore.updates
                .map { it?.fingerPrintControl == true }
                .distinctUntilChanged()
                .collect { fingerPrintEnabled ->
                    if (fingerPrintEnabled) {
                        FingerPrintLogcatProcessor.startMonitoring()
                    }
                }
        }
    }
}

private val fingerprintCollectorStarted = AtomicBoolean(false)