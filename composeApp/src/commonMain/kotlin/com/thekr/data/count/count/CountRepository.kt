package com.thekr.data.count.count

import com.thekr.model.Count
import kotlinx.coroutines.flow.Flow

interface CountRepository {
    suspend fun insert(count: Count)
    fun findCounts(
        zekrInstanceId: Long? = null,
        timeCreatedAfter: Long? = null,
        timeCreatedBefore: Long? = null
    ): Flow<List<Count>>

    fun getCount(
        zekrInstanceId: Long? = null,
        timeCreatedAfter: Long? = null,
        timeCreatedBefore: Long? = null
    ): Flow<Int>

    fun getLastCountByZekrInstanceId(zekrInstanceId: Long): Flow<Count?>
    suspend fun findAllSync(): List<Count>
     fun findAllByCategorySync(categoryId: Long): Flow<List<Count>>
    fun findAllByZekrInstanceSync(zekrInstanceId: Long): Flow<List<Count>>
}