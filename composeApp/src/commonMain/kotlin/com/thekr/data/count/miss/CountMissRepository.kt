package com.thekr.data.count.miss

import com.thekr.model.CountMiss
import kotlinx.coroutines.flow.Flow

interface CountMissRepository {
    suspend fun insert(countMiss: CountMiss)
    fun findAll(): Flow<List<CountMiss>>
    fun findByZekrInstanceId(zekrId: Long): Flow<List<CountMiss>>
    fun getTotalCountByZekrId(zekrId: Long): Flow<Long>
}