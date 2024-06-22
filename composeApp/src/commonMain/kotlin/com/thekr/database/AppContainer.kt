package com.thekr.database

import com.thekr.data.count.count.CountRepository
import com.thekr.data.count.miss.CountMissRepository
import com.thekr.data.goal.completion.ZekrGoalCompletionRepository
import com.thekr.data.session.SessionRepository
import com.thekr.data.zekr.category.CategoryRepository
import com.thekr.data.zekr.fadl.FadlRepository
import com.thekr.data.zekr.instance.ZekrInstanceRepository
import com.thekr.data.zekr.zekr.ZekrRepository

/** App container for Dependency injection. */
interface AppContainer {
    val categoryRepository: CategoryRepository
    val zekrRepository: ZekrRepository
    val zekrInstanceRepository: ZekrInstanceRepository
    val fadlRepository: FadlRepository
    val countRepository: CountRepository
    val countMissRepository: CountMissRepository
    val zekrGoalCompletionRepository: ZekrGoalCompletionRepository
    val sessionRepository: SessionRepository
}