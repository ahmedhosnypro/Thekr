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
    version = 1,
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
    }
}