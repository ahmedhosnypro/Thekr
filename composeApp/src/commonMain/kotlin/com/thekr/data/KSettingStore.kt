@file:JvmName("KSettingStoreRecoveryKt")

package com.thekr.data

import com.thekr.data.proto.Settings
import com.thekr.di.appStorage
import com.thekr.di.settingsFile
import com.thekr.util.TimeHelper.now
import io.github.xxfast.kstore.KStore
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.serialization.json.Json

expect val settingsStore: KStore<Settings>

expect suspend fun initAppData()

// Mirrors kstore 1.1.0's FileCodec default Json (ignoreUnknownKeys +
// encodeDefaults) so this pre-check only rejects what kstore itself would
// fail to decode — no false-positive quarantines on benign schema drift.
private val recoveryJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

/**
 * Quarantine files (settings.json.corrupt-<epochMillis>) are pruned to this
 * many newest copies on startup. Object-scoped so ktlint (screaming snake)
 * and detekt (top-level PascalCase) naming rules are both satisfied.
 */
private object QuarantineFiles {
    const val KEEP_COUNT = 5
}

/**
 * kstore 1.1.0's file codec maps only FileNotFoundException to null; a corrupt
 * or partially-written settings.json (e.g. disk-full during a non-atomic
 * moveOrCopy fallback, or external tampering) makes every get()/updates read
 * throw — crashing the launch path before any error gate, since CounterApp
 * collects `updates` during first composition. One corrupt file used to
 * brick every launch until the user cleared app data (wiping Room counts too).
 *
 * Called from each platform's synchronized `settingsStore` lazy initializer,
 * so recovery always happens exactly once, before the store is first used:
 * on a read or decode failure the bad file is quarantined next to itself
 * (settings.json.corrupt-<epochMillis> — nothing is silently destroyed);
 * the next get() then returns null, which makes initAppData() re-initialize
 * the record with defaults so the app boots normally.
 */
internal fun quarantineCorruptSettingsFile() {
    val settingsPath = Path("$appStorage/$settingsFile")
    if (!SystemFileSystem.exists(settingsPath)) {
        pruneQuarantineFiles()
        return
    }

    val bytes = runCatching {
        SystemFileSystem.source(settingsPath).buffered().use { it.readByteArray() }
    }.getOrElse { failure ->
        quarantine(settingsPath, "unreadable (${failure.message})")
        return
    }

    runCatching { recoveryJson.decodeFromString<Settings>(bytes.decodeToString()) }
        .onFailure { quarantine(settingsPath, "undecodable (${it.message})") }

    pruneQuarantineFiles()
}

/** Keeps only the newest [QuarantineFiles.KEEP_COUNT] settings.json.corrupt-* files. */
private fun pruneQuarantineFiles() {
    runCatching {
        val prefix = "$settingsFile.corrupt-"
        SystemFileSystem
            .list(Path(appStorage))
            .filter { it.name.startsWith(prefix) }
            .sortedByDescending { it.name.removePrefix(prefix).toLongOrNull() ?: 0L }
            .drop(QuarantineFiles.KEEP_COUNT)
            .forEach { stale ->
                // A single unremovable stale file must never break the boot.
                runCatching { SystemFileSystem.delete(stale) }
            }
    }
}

private fun quarantine(settingsPath: Path, reason: String?) {
    val quarantinePath = Path("$appStorage/$settingsFile.corrupt-${now()}")
    runCatching { SystemFileSystem.atomicMove(settingsPath, quarantinePath) }
        .onFailure {
            // Last resort so the app still boots: drop the bad blob; the
            // quarantined copy survives if the move half-completed.
            runCatching { SystemFileSystem.delete(settingsPath) }
        }
    println(
        "KSettingStore: settings file was $reason; quarantined at " +
            "$quarantinePath and re-initializing with defaults",
    )
}
