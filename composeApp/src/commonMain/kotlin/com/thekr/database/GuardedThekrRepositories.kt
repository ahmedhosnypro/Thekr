package com.thekr.database

import com.thekr.data.thekr.category.CategoryRepository
import com.thekr.data.thekr.fadl.FadlRepository
import com.thekr.data.thekr.instance.ThekrInstanceRepository
import com.thekr.data.thekr.thekr.ThekrRepository
import com.thekr.model.Category
import com.thekr.model.Thekr
import com.thekr.model.ThekrFadl
import com.thekr.model.ThekrInstance
import kotlinx.coroutines.flow.Flow

/**
 * Flow-emission guards over the thekr-family repositories; the guard
 * semantics live in [guardedEmissions].
 */
internal class GuardedCategoryRepository(private val delegate: CategoryRepository) : CategoryRepository by delegate {
    override fun findById(id: Long): Flow<Category?> =
        delegate.findById(id).guardedEmissions()

    override fun findAll(): Flow<List<Category>> = delegate.findAll().guardedEmissions()

    override fun findByParentId(parent: Long): Flow<List<Category>> =
        delegate.findByParentId(parent).guardedEmissions()

    override fun findByRootId(id: Long): Flow<List<Category>> =
        delegate.findByRootId(id).guardedEmissions()

    override fun isCategoryHasChild(id: Long): Flow<Boolean> =
        delegate.isCategoryHasChild(id).guardedEmissions()
}

internal class GuardedThekrRepository(private val delegate: ThekrRepository) : ThekrRepository by delegate {
    override fun findAll(): Flow<List<Thekr>> = delegate.findAll().guardedEmissions()

    override fun findByCategoryId(categoryId: Long): Flow<List<Thekr>> =
        delegate.findByCategoryId(categoryId).guardedEmissions()

    override fun findById(id: Long): Flow<Thekr?> = delegate.findById(id).guardedEmissions()
}

internal class GuardedThekrInstanceRepository(private val delegate: ThekrInstanceRepository) : ThekrInstanceRepository by delegate {
    override fun findById(id: Long): Flow<ThekrInstance?> =
        delegate.findById(id).guardedEmissions()

    override fun findAll(): Flow<List<ThekrInstance>> =
        delegate.findAll().guardedEmissions()

    override fun findByCategoryId(categoryId: Long): Flow<List<ThekrInstance>> =
        delegate.findByCategoryId(categoryId).guardedEmissions()
}

internal class GuardedFadlRepository(private val delegate: FadlRepository) : FadlRepository by delegate {
    override fun findAll(): Flow<List<ThekrFadl>> = delegate.findAll().guardedEmissions()

    override fun findByThekrId(thekrId: Long): Flow<List<ThekrFadl>> =
        delegate.findByThekrId(thekrId).guardedEmissions()

    override fun findByCategoryId(id: Long): Flow<List<ThekrFadl>> =
        delegate.findByCategoryId(id).guardedEmissions()
}
