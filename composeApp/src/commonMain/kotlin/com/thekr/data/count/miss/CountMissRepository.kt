package com.thekr.data.count.miss

import com.thekr.model.CountMiss
import kotlinx.coroutines.flow.Flow

interface CountMissRepository {
    suspend fun insert(countMiss: CountMiss)
    fun findAll(): Flow<List<CountMiss>>
    fun findByThekrInstanceId(thekrId: Long): Flow<List<CountMiss>>
    fun getTotalCountByThekrId(thekrId: Long): Flow<Long>
}