package com.thekr.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.thekr.data.count.count.CountDAO
import com.thekr.data.count.miss.CountMissDAO
import com.thekr.data.goal.completion.ThekrGoalCompletionDAO
import com.thekr.data.session.SessionDAO
import com.thekr.data.thekr.category.CategoryDAO
import com.thekr.data.thekr.fadl.FadlDAO
import com.thekr.data.thekr.instance.ThekrInstanceDAO
import com.thekr.data.thekr.thekr.ThekrDAO
import com.thekr.model.Category
import com.thekr.model.Count
import com.thekr.model.CountMiss
import com.thekr.model.Session
import com.thekr.model.Thekr
import com.thekr.model.ThekrFadl
import com.thekr.model.ThekrGoalCompletion
import com.thekr.model.ThekrInstance

@Database(
    entities = [
        Thekr::class,
        ThekrInstance::class,
        ThekrFadl::class,
        Count::class,
        CountMiss::class,
        ThekrGoalCompletion::class,
        Category::class,
        Session::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun thekrDAO(): ThekrDAO
    abstract fun thekrInstanceDao(): ThekrInstanceDAO
    abstract fun fadlDao(): FadlDAO
    abstract fun countDao(): CountDAO
    abstract fun countMissDao(): CountMissDAO
    abstract fun thekrGoalCompletionDao(): ThekrGoalCompletionDAO
    abstract fun categoryDao(): CategoryDAO
    abstract fun sessionDao(): SessionDAO

    companion object {
        const val DATABASE_NAME = "thekr.db"

        /**
         * Current Room schema version — must stay in sync with the
         * `@Database(version = ...)` literal above (KSP cannot resolve a
         * constant reference there, so the two cannot share one symbol).
         * A version bump MUST ship two changes together, in the same commit:
         *  1. a real, non-destructive `Migration` registered in
         *     `di/DatabaseProvider` (the MIGRATION_1_2 count-index migration
         *     is the precedent — never rely on a destructive fallback), and
         *  2. the previous schema version added to [MIGRATION_ENTRY_VERSIONS]
         *     and this constant raised.
         *
         * If either is forgotten, the pre-open drift check in
         * `DatabaseRecovery.quarantineDatabaseOnSchemaDrift` quarantines the
         * old database file (preserving every count row next to the new
         * file) instead of letting Room silently recreate the schema — so a
         * silent user-data wipe is impossible by construction, and the
         * omission fails loudly in the first QA run instead of shipping.
         */
        const val SCHEMA_VERSION = 2

        /**
         * On-disk schema versions from which `di/DatabaseProvider` carries a
         * migration to [SCHEMA_VERSION] (today: 1 -> 2). Versions outside
         * this set plus [SCHEMA_VERSION] are quarantined before Room opens
         * the file.
         */
        val MIGRATION_ENTRY_VERSIONS = setOf(1)
    }
}
