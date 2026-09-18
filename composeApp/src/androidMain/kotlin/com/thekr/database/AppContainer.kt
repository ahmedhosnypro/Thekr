package com.thekr.database

import android.content.Context
import com.thekr.data.thekr.category.CategoryRepository
import com.thekr.data.thekr.category.OfflineCategoryRepository
import com.thekr.data.count.count.CountRepository
import com.thekr.data.count.count.OfflineCountRepository
import com.thekr.data.count.miss.CountMissRepository
import com.thekr.data.count.miss.OfflineCountMissRepository
import com.thekr.data.thekr.fadl.FadlRepository
import com.thekr.data.thekr.fadl.OfflineFadlRepository
import com.thekr.data.goal.completion.OfflineThekrGoalCompletionRepository
import com.thekr.data.goal.completion.ThekrGoalCompletionRepository
import com.thekr.data.session.OfflineSessionRepository
import com.thekr.data.session.SessionRepository
import com.thekr.data.thekr.instance.OfflineThekrInstanceRepository
import com.thekr.data.thekr.instance.ThekrInstanceRepository
import com.thekr.data.thekr.thekr.ThekrRepository
import com.thekr.data.thekr.thekr.OfflineThekrRepository
import com.thekr.di.DatabaseProvider


/**
 * [AppContainer] implementation that provides instance of
 * [OfflineThekrRepository]
 */
actual class AppDataContainer(private val context: Context) : AppContainer {

    private fun db(): AppDatabase {
        // Lock-free fast path once the warm-up has built the database; the
        // slow path builds off-thread (see initDatabaseIfNeeded).
        if (DatabaseProvider.isDatabaseInitialized) return DatabaseProvider.database
        return initDatabaseIfNeeded(context)
    }

    override val categoryRepository: CategoryRepository by lazy {
        GuardedCategoryRepository(OfflineCategoryRepository(db().categoryDao()))
    }

    override val thekrRepository: ThekrRepository by lazy {
        GuardedThekrRepository(OfflineThekrRepository(db().thekrDAO()))
    }

    override val thekrInstanceRepository: ThekrInstanceRepository by lazy {
        GuardedThekrInstanceRepository(OfflineThekrInstanceRepository(db().thekrInstanceDao()))
    }

    override val fadlRepository: FadlRepository by lazy {
        GuardedFadlRepository(OfflineFadlRepository(db().fadlDao()))
    }

    override val countRepository: CountRepository by lazy {
        GuardedCountRepository(OfflineCountRepository(db().countDao()))
    }

    override val countMissRepository: CountMissRepository by lazy {
        GuardedCountMissRepository(OfflineCountMissRepository(db().countMissDao()))
    }

    override val thekrGoalCompletionRepository: ThekrGoalCompletionRepository by lazy {
        OfflineThekrGoalCompletionRepository(
            db().thekrGoalCompletionDao()
        )
    }

    override val sessionRepository: SessionRepository by lazy {
        GuardedSessionRepository(OfflineSessionRepository(db().sessionDao()))
    }
}