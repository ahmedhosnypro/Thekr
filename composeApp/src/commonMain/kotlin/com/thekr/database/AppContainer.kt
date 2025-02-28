package com.thekr.database

import com.thekr.data.count.count.CountRepository
import com.thekr.data.count.miss.CountMissRepository
import com.thekr.data.goal.completion.ThekrGoalCompletionRepository
import com.thekr.data.session.SessionRepository
import com.thekr.data.thekr.category.CategoryRepository
import com.thekr.data.thekr.fadl.FadlRepository
import com.thekr.data.thekr.instance.ThekrInstanceRepository
import com.thekr.data.thekr.thekr.ThekrRepository

/** App container for Dependency injection. */
interface AppContainer {
    val categoryRepository: CategoryRepository
    val thekrRepository: ThekrRepository
    val thekrInstanceRepository: ThekrInstanceRepository
    val fadlRepository: FadlRepository
    val countRepository: CountRepository
    val countMissRepository: CountMissRepository
    val thekrGoalCompletionRepository: ThekrGoalCompletionRepository
    val sessionRepository: SessionRepository
}