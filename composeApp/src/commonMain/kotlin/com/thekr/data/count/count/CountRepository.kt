package com.thekr.data.count.count

import com.thekr.model.Count
import kotlinx.coroutines.flow.Flow

interface CountRepository {
    suspend fun insert(count: Count)
    fun findCounts(
        thekrInstanceId: Long? = null,
        timeCreatedAfter: Long? = null,
        timeCreatedBefore: Long? = null
    ): Flow<List<Count>>

    fun getCount(
        thekrInstanceId: Long? = null,
        timeCreatedAfter: Long? = null,
        timeCreatedBefore: Long? = null
    ): Flow<Int>

    fun getLastCountByThekrInstanceId(thekrInstanceId: Long): Flow<Count?>
    suspend fun findAll(): List<Count>
    fun findAllByCategory(categoryId: Long): Flow<List<Count>>
    fun findAllByThekrInstance(thekrInstanceId: Long): Flow<List<Count>>
    fun getCountTotalsByThekrInstanceId(
        thekrInstanceId: Long,
        periods: CountPeriodBounds,
    ): Flow<ThekrInstanceCountTotals>
}