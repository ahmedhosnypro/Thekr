package com.thekr.data

import com.thekr.JvmApplication.userDataDir
import com.thekr.data.proto.Settings
import com.thekr.database.JsonParser.importDataFromJson
import com.thekr.di.appStorage
import com.thekr.di.settingsFile
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import okio.Path.Companion.toPath

actual val settingsStore: KStore<Settings> by lazy {
    // Runs once, before the store is first used anywhere (this lazy is
    // synchronized) — a corrupt settings file can never reach kstore's
    // decoder, which would otherwise throw on every launch.
    quarantineCorruptSettingsFile()
    storeOf(Path("$appStorage/$settingsFile".toPath().toString()))
}

actual suspend fun initAppData() {
    // Initialize settingsDataStore
    // Belt-and-suspenders for a file corrupted between the quarantine check
    // and this read: treat a throwing read like a missing record so the
    // null-branch below re-initializes and the app boots.
    val settings = runCatching { settingsStore.get() }.getOrNull()
    if (settings == null) {
        kotlinx.io.files.SystemFileSystem.run {
            createDirectories(
                path = kotlinx.io.files.Path(userDataDir),
            )
        }
        settingsStore.set(Settings(initialized = true))
        importDataFromJson()
    } else if (settings.dbInitialized.not()) {
        importDataFromJson()
    }
}
