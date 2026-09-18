package com.thekr.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thekr.di.DatabaseProvider
import kotlinx.io.files.Path
import java.util.concurrent.Executors

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(AppDatabase.DATABASE_NAME)
    // Runs on the builder thread (this is only called from
    // initDatabaseIfNeeded's executor), before Room can open — or
    // destructively recreate — the file.
    quarantineDatabaseOnSchemaDrift(Path(dbFile.absolutePath))
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}

private val initLock = Any()

// The Room build itself runs on this dedicated thread, so even a main-thread
// first access that loses the warm-up race only waits for the build instead
// of executing it synchronously on main.
private val buildExecutor = Executors.newSingleThreadExecutor { runnable ->
    Thread(runnable, "ThekrDbBuilder").apply { isDaemon = true }
}

/**
 * Builds the Room database unless already built, serialized so a background
 * warm-up and a first-access call never construct two instances. The fast
 * path is lock-free; the build never runs on the calling thread.
 */
fun initDatabaseIfNeeded(ctx: Context): AppDatabase {
    if (DatabaseProvider.isDatabaseInitialized) return DatabaseProvider.database
    synchronized(initLock) {
        if (DatabaseProvider.isDatabaseInitialized) return DatabaseProvider.database
        return buildExecutor.submit<AppDatabase> {
            DatabaseProvider.initDatabase(getDatabaseBuilder(ctx))
            DatabaseProvider.database
        }.get()
    }
}
