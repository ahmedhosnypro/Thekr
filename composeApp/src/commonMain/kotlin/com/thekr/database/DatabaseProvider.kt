package com.thekr.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

object DatabaseProvider {
    lateinit var database: AppDatabase

    fun initDatabase(
        builder: RoomDatabase.Builder<AppDatabase>
    ) {
        if (DatabaseProvider::database.isInitialized.not()) {

            database = builder
//                .addMigrations(MIGRATIONS)
                .fallbackToDestructiveMigration(true)
                .fallbackToDestructiveMigrationOnDowngrade(true)
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
        }
    }
}