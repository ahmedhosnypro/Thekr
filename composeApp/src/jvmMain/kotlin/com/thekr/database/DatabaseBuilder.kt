package com.thekr.database

import androidx.room.Room
import androidx.room.RoomDatabase
import ca.gosyer.appdirs.AppDirs
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbDir = File(
        AppDirs {
            appName = "thekr"
            appAuthor = "thekr"
        }.getUserDataDir()
    )
    dbDir.mkdirs()
    return Room.databaseBuilder<AppDatabase>(
        name = File(dbDir, AppDatabase.DATABASE_NAME).absolutePath,
    )
}
