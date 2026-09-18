package com.thekr.di

import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import com.thekr.database.AppDatabase
import kotlinx.coroutines.Dispatchers

object DatabaseProvider {
    lateinit var database: AppDatabase

    /** Exposes the lateinit check to other source sets (the backing field
     *  isn't accessible there). */
    val isDatabaseInitialized: Boolean
        get() = this::database.isInitialized

    private val MIGRATION_1_2 =
        object : Migration(1, 2) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_count_thekrInstanceId_timeCreated` " +
                        "ON `count` (`thekrInstanceId`, `timeCreated`)"
                )
                connection.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_count_miss_thekrInstanceId` " +
                        "ON `count_miss` (`thekrInstanceId`)"
                )
            }
        }

    fun initDatabase(
        builder: RoomDatabase.Builder<AppDatabase>
    ) {
        if (DatabaseProvider::database.isInitialized.not()) {

            // No upgrade-path destructive fallback: a genuinely missing upgrade
            // migration fails fast (Room IllegalStateException at open) instead of
            // silently booting an empty database. Downgrades still fall back
            // destructively — DatabaseRecovery's pre-open quarantine has already
            // preserved the old file and its sidecars.
            database = builder
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigrationOnDowngrade(true)
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
        }
    }
}
