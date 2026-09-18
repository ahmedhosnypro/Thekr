@file:JvmName("DatabaseRecoveryKt")

package com.thekr.database

import com.thekr.util.TimeHelper.now
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

/**
 * Pre-open database integrity check, the DB-side twin of the settings
 * quarantine in `data/KSettingStore.kt` (M68).
 *
 * Room's schema version is persisted in the SQLite file header as
 * `PRAGMA user_version` (offset 60, big-endian), which can be read with
 * plain file IO before Room ever opens the file. `di/DatabaseProvider`
 * still installs `fallbackToDestructiveMigration`, so an on-disk schema
 * that has no migration path to the current schema (a forgotten migration
 * on a version bump, an old APK reinstalled over a newer database, or a
 * corrupt file) would otherwise make Room silently drop every zekr count.
 *
 * Instead, before the builder is handed to Room, any database whose stored
 * version has no migration path is quarantined next to itself
 * (`thekr.db.quarantined-<epochMillis>` together with its `-wal`/`-shm`
 * side files — nothing is ever deleted), and Room rebuilds a fresh schema
 * from the bundled seed. The user's counts survive on disk, the app boots,
 * and the failure is logged loudly instead of being a silent wipe.
 */
internal fun quarantineDatabaseOnSchemaDrift(databasePath: Path) {
    if (!SystemFileSystem.exists(databasePath)) return

    val storedVersion = runCatching { readStoredSchemaVersion(databasePath) }
        .getOrElse { failure ->
            // Unreadable or not a SQLite file at all: Room would crash on
            // open. Quarantine and rebuild fresh.
            quarantine(databasePath, "unreadable or corrupt (${failure.message})")
            return
        } ?: return
    if (storedVersion == AppDatabase.SCHEMA_VERSION) return
    if (storedVersion in AppDatabase.MIGRATION_ENTRY_VERSIONS) return

    quarantine(
        databasePath,
        "schema version $storedVersion has no migration path to " +
            "version ${AppDatabase.SCHEMA_VERSION}",
    )
}

/**
 * Reads the SQLite header (16-byte magic at offset 0, `PRAGMA user_version`
 * big-endian at offset 60).
 *
 * @return the stored schema version, or `null` for an empty (0-byte) file
 *   that Room will treat as a brand-new database.
 * @throws IllegalStateException if the file is not a valid SQLite database
 *   (too small or wrong magic) — the caller quarantines it as corrupt.
 */
private fun readStoredSchemaVersion(databasePath: Path): Int? {
    SystemFileSystem.source(databasePath).buffered().use { source ->
        val magic = ByteArray(SQLITE_MAGIC.size)
        val magicBytesRead = source.readAtMostTo(magic, 0, magic.size)
        if (magicBytesRead == 0) return null
        check(magicBytesRead == SQLITE_MAGIC.size && magic.contentEquals(SQLITE_MAGIC)) {
            "not a SQLite database file"
        }
        source.skip((SQLITE_VERSION_OFFSET - SQLITE_MAGIC.size).toLong())
        return source.readInt()
    }
}

private fun quarantine(databasePath: Path, reason: String) {
    val quarantineSuffix = "$QUARANTINE_MARKER-${now()}"
    val sidecarSuffixes = listOf(WAL_SUFFIX, SHM_SUFFIX)
    val movedSidecars = mutableListOf<Pair<Path, Path>>()

    // Sidecar files first so the main file only moves once the whole set can;
    // a fresh main file must never be left next to a stale -wal/-shm.
    for (suffix in sidecarSuffixes) {
        val sidecarPath = Path(databasePath.toString() + suffix)
        if (!SystemFileSystem.exists(sidecarPath)) continue
        val quarantinedPath = Path(sidecarPath.toString() + quarantineSuffix)
        if (!moveToQuarantine(sidecarPath, quarantinedPath)) {
            restoreSidecars(movedSidecars)
            println(
                "DatabaseRecovery: $databasePath is $reason but could not be " +
                    "quarantined (failed on $sidecarPath); leaving it in place",
            )
            return
        }
        movedSidecars.add(sidecarPath to quarantinedPath)
    }

    val quarantinedPath = Path(databasePath.toString() + quarantineSuffix)
    if (!moveToQuarantine(databasePath, quarantinedPath)) {
        restoreSidecars(movedSidecars)
        println(
            "DatabaseRecovery: $databasePath is $reason but could not be " +
                "quarantined; leaving it in place",
        )
        return
    }

    println(
        "DatabaseRecovery: database was $reason; old files preserved at " +
            "$quarantinedPath, rebuilding a fresh schema",
    )
}

private fun moveToQuarantine(path: Path, quarantinedPath: Path): Boolean {
    val result = runCatching { SystemFileSystem.atomicMove(path, quarantinedPath) }
    result.onFailure { println("DatabaseRecovery: failed to quarantine $path (${it.message})") }
    return result.isSuccess
}

private fun restoreSidecars(movedSidecars: List<Pair<Path, Path>>) {
    movedSidecars.reversed().forEach { (path, quarantinedPath) ->
        runCatching { SystemFileSystem.atomicMove(quarantinedPath, path) }
    }
}

private val SQLITE_MAGIC = "SQLite format 3\u0000".encodeToByteArray()
private const val SQLITE_VERSION_OFFSET = 60
private const val WAL_SUFFIX = "-wal"
private const val SHM_SUFFIX = "-shm"
private const val QUARANTINE_MARKER = ".quarantined"
