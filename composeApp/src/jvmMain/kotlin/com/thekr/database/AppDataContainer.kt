package com.thekr.database

import com.thekr.data.count.count.CountRepository
import com.thekr.data.count.count.OfflineCountRepository
import com.thekr.data.count.miss.CountMissRepository
import com.thekr.data.count.miss.OfflineCountMissRepository
import com.thekr.data.goal.completion.OfflineZekrGoalCompletionRepository
import com.thekr.data.goal.completion.ZekrGoalCompletionRepository
import com.thekr.data.session.OfflineSessionRepository
import com.thekr.data.session.SessionRepository
import com.thekr.data.zekr.category.CategoryRepository
import com.thekr.data.zekr.category.OfflineCategoryRepository
import com.thekr.data.zekr.fadl.FadlRepository
import com.thekr.data.zekr.fadl.OfflineFadlRepository
import com.thekr.data.zekr.instance.OfflineZekrInstanceRepository
import com.thekr.data.zekr.instance.ZekrInstanceRepository
import com.thekr.data.zekr.zekr.OfflineZekrRepository
import com.thekr.data.zekr.zekr.ZekrRepository
import com.thekr.di.DatabaseProvider.database

/**
 * [AppContainer] implementation that provides instance of
 * [OfflineZekrRepository]
 */
actual class AppDataContainer() : AppContainer {
    override val categoryRepository: CategoryRepository by lazy {
        OfflineCategoryRepository(database.categoryDao())
    }

    override val zekrRepository: ZekrRepository by lazy {
        OfflineZekrRepository(database.zekrDAO())
    }

    override val zekrInstanceRepository: ZekrInstanceRepository by lazy {
        OfflineZekrInstanceRepository(database.zekrInstanceDao())
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

    override val zekrGoalCompletionRepository: ZekrGoalCompletionRepository by lazy {
        OfflineZekrGoalCompletionRepository(
            database.zekrGoalCompletionDao()
        )
    }

    override val sessionRepository: SessionRepository by lazy {
        OfflineSessionRepository(database.sessionDao())
    }
}