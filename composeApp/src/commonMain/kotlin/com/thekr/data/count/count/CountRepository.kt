package com.thekr.data.count.count

import com.thekr.model.Count
import kotlinx.coroutines.flow.Flow

interface CountRepository {
    suspend fun insert(count: Count)

    /**
     * Durably writes any count rows still held in the batch buffer.
     *
     * [insert] batches count-tap writes and defers them to Room; call this
     * on lifecycle ON_STOP, before a read that must observe every tap, or
     * wherever immediate durability is required.
     */
    suspend fun flush()

    fun findCounts(
        thekrInstanceId: Long? = null,
        timeCreatedAfter: Long? = null,
        timeCreatedBefore: Long? = null,
    ): Flow<List<Count>>

    fun getCount(
        thekrInstanceId: Long? = null,
        timeCreatedAfter: Long? = null,
        timeCreatedBefore: Long? = null,
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
