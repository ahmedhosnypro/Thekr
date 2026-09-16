package com.thekr.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thekr.di.DatabaseProvider

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(AppDatabase.DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

private val initLock = Any()

/**
 * Builds the Room database unless already built, serialized so a background
 * warm-up and a first-access call never construct two instances.
 */
fun initDatabaseIfNeeded(ctx: Context) {
    synchronized(initLock) {
        DatabaseProvider.initDatabase(getDatabaseBuilder(ctx))
    }
}