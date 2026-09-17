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
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.io.files.Path as KxPath

actual val settingsStore: KStore<Settings> by lazy {
    // Runs once, before the store is first used anywhere (this lazy is
    // synchronized) — a corrupt settings file can never reach kstore's
    // decoder, which would otherwise throw on every launch.
    quarantineCorruptSettingsFile()
//    storeOf("$appStorage/$settingsFile".toPath())
    storeOf(KxPath("$appStorage/$settingsFile".toPath().toString()))
}

actual suspend fun initAppData() {
    // Belt-and-suspenders for a file corrupted between the quarantine check
    // and this read: treat a throwing read like a missing record so the
    // null-branch below re-initializes and the app boots.
    val settings = runCatching { settingsStore.get() }.getOrNull()

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
