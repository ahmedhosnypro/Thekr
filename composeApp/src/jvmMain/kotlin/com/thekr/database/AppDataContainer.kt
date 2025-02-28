package com.thekr.database

import com.thekr.data.count.count.CountRepository
import com.thekr.data.count.count.OfflineCountRepository
import com.thekr.data.count.miss.CountMissRepository
import com.thekr.data.count.miss.OfflineCountMissRepository
import com.thekr.data.goal.completion.OfflineThekrGoalCompletionRepository
import com.thekr.data.goal.completion.ThekrGoalCompletionRepository
import com.thekr.data.session.OfflineSessionRepository
import com.thekr.data.session.SessionRepository
import com.thekr.data.thekr.category.CategoryRepository
import com.thekr.data.thekr.category.OfflineCategoryRepository
import com.thekr.data.thekr.fadl.FadlRepository
import com.thekr.data.thekr.fadl.OfflineFadlRepository
import com.thekr.data.thekr.instance.OfflineThekrInstanceRepository
import com.thekr.data.thekr.instance.ThekrInstanceRepository
import com.thekr.data.thekr.thekr.OfflineThekrRepository
import com.thekr.data.thekr.thekr.ThekrRepository
import com.thekr.di.DatabaseProvider.database

/**
 * [AppContainer] implementation that provides instance of
 * [OfflineThekrRepository]
 */
actual class AppDataContainer : AppContainer {
    override val categoryRepository: CategoryRepository by lazy {
        OfflineCategoryRepository(database.categoryDao())
    }

    override val thekrRepository: ThekrRepository by lazy {
        OfflineThekrRepository(database.thekrDAO())
    }

    override val thekrInstanceRepository: ThekrInstanceRepository by lazy {
        OfflineThekrInstanceRepository(database.thekrInstanceDao())
    }

    override val fadlRepository: FadlRepository by lazy {
        OfflineFadlRepository(database.fadlDao())
    }

    override val countRepository: CountRepository by lazy {
        OfflineCountRepository(database.countDao())
    }

    override val countMissRepository: CountMissRepository by lazy {
        OfflineCountMissRepository(database.countMissDao())
    }

    override val thekrGoalCompletionRepository: ThekrGoalCompletionRepository by lazy {
        OfflineThekrGoalCompletionRepository(
            database.thekrGoalCompletionDao()
        )
    }

    override val sessionRepository: SessionRepository by lazy {
        OfflineSessionRepository(database.sessionDao())
    }
}