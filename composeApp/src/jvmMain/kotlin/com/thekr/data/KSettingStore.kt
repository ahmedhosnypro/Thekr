package com.thekr.data

import com.thekr.JvmApplication.userDataDir
import com.thekr.data.proto.Settings
import com.thekr.database.JsonParser.importDataFromJson
import com.thekr.di.appStorage
import com.thekr.di.settingsFile
import io.github.xxfast.kstore.Codec
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.FileCodec
import io.github.xxfast.kstore.storeOf
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.readByteArray
import okio.Path.Companion.toPath
import java.io.FileNotFoundException

actual val settingsStore: KStore<Settings> by lazy {
    // Runs once, before the store is first used anywhere (this lazy is
    // synchronized) — a corrupt settings file can never reach kstore's
    // decoder, which would otherwise throw on every launch. The pre-check's
    // single read+decode is handed to the codec below, so kstore's updates
    // flow — which re-reads settings.json on every collection, on the
    // collecting context (the AWT EDT for the first composition) — reuses the
    // shared decode instead of reading+decoding the same file a second time
    // pre-frame.
    val settingsPath = Path("$appStorage/$settingsFile".toPath().toString())
    val snapshot = quarantineCorruptSettingsFile()
    storeOf(SharedDecodeFileCodec(settingsPath, snapshot))
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

/**
 * A [Codec] that keeps kstore's file-backed semantics while eliminating the
 * duplicate settings.json read+decode before the first frame: kstore 1.1.0's
 * `updates` flow does a fresh `decode()` on every collection (not from
 * cache), and on desktop the first collection runs on the AWT EDT during
 * first composition. When the bytes on disk still match the quarantine
 * pre-check's read, its decoded value is returned as-is; writes delegate to
 * kstore's own [FileCodec], preserving the atomic temp-file-then-move
 * behavior. A file that goes bad between the pre-check and a decode is
 * quarantined here too (mirroring the pre-check) so no read can crash the
 * boot.
 */
private class SharedDecodeFileCodec(private val settingsPath: Path, seed: SettingsFileSnapshot?) : Codec<Settings> {
    private val delegate = FileCodec<Settings>(
        settingsPath,
        Path("$settingsPath.temp"),
        recoveryJson,
    )

    @Volatile
    private var snapshot: SettingsFileSnapshot? = seed

    override suspend fun decode(): Settings? {
        val bytes = runCatching {
            kotlinx.io.files.SystemFileSystem.source(settingsPath).buffered()
                .use { it.readByteArray() }
        }.getOrElse { failure ->
            // Mirrors kstore's FileCodec: a missing file is an empty store.
            if (failure is FileNotFoundException) return null
            quarantine(settingsPath, "unreadable (${failure.message})")
            return null
        }

        val memo = snapshot
        if (memo != null && memo.bytes contentEquals bytes) return memo.decoded

        val decoded = runCatching {
            recoveryJson.decodeFromString<Settings>(bytes.decodeToString())
        }.getOrElse { failure ->
            quarantine(settingsPath, "undecodable (${failure.message})")
            null
        }
        snapshot = SettingsFileSnapshot(bytes, decoded)
        return decoded
    }

    override suspend fun encode(value: Settings?) = delegate.encode(value)
}
