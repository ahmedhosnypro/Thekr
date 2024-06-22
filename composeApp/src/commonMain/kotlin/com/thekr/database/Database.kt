package com.thekr.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.thekr.data.zekr.category.CategoryDAO
import com.thekr.model.Count
import com.thekr.data.count.count.CountDAO
import com.thekr.data.count.miss.CountMissDAO
import com.thekr.data.zekr.fadl.FadlDAO
import com.thekr.model.ZekrGoalCompletion
import com.thekr.data.goal.completion.ZekrGoalCompletionDAO
import com.thekr.data.session.SessionDAO
import com.thekr.data.zekr.instance.ZekrInstanceDAO
import com.thekr.model.Zekr
import com.thekr.data.zekr.zekr.ZekrDAO
import com.thekr.model.Category
import com.thekr.model.CountMiss
import com.thekr.model.Session
import com.thekr.model.ZekrFadl
import com.thekr.model.ZekrInstance
import kotlinx.coroutines.Dispatchers

@Database(
    entities = [
        Zekr::class,
        ZekrInstance::class,
        ZekrFadl::class,
        Count::class,
        CountMiss::class,
        ZekrGoalCompletion::class,
        Category::class,
        Session::class,
    ],
    version = 1,
    exportSchema = false,
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun zekrDAO(): ZekrDAO
    abstract fun zekrInstanceDao(): ZekrInstanceDAO
    abstract fun fadlDao(): FadlDAO
    abstract fun countDao(): CountDAO
    abstract fun countMissDao(): CountMissDAO
    abstract fun zekrGoalCompletionDao(): ZekrGoalCompletionDAO
    abstract fun categoryDao(): CategoryDAO
    abstract fun sessionDao(): SessionDAO

    companion object {
        const val DATABASE_NAME = "thekr.db"
    }
}