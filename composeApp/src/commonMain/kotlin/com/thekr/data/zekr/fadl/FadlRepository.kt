package com.thekr.data.thekr.fadl

import com.thekr.model.ThekrFadl
import kotlinx.coroutines.flow.Flow

interface FadlRepository {
    suspend fun insert(thekrFadl: ThekrFadl)

    suspend fun insertAll(thekrFadl: List<ThekrFadl>)

    suspend fun update(thekrFadl: ThekrFadl)

    fun findAll(): Flow<List<ThekrFadl>>

    fun findByThekrId(thekrId: Long): Flow<List<ThekrFadl>>

    fun findByCategoryId(id: Long): Flow<List<ThekrFadl>>
}