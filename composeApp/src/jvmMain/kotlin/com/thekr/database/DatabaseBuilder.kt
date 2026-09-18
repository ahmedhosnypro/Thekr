package com.thekr.database

import androidx.room.Room
import androidx.room.RoomDatabase
import ca.gosyer.appdirs.AppDirs
import kotlinx.io.files.Path
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbDir = File(
        AppDirs {
            appName = "thekr"
            appAuthor = "thekr"
        }.getUserDataDir(),
    )
    dbDir.mkdirs()
    val dbFile = File(dbDir, AppDatabase.DATABASE_NAME)
    // Before Room can open — or destructively recreate — the file.
    quarantineDatabaseOnSchemaDrift(Path(dbFile.absolutePath))
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}
