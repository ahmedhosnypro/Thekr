package com.thekr.data.zekr.fadl

import com.thekr.model.ZekrFadl
import kotlinx.coroutines.flow.Flow

interface FadlRepository {
    suspend fun insert(zekrFadl: ZekrFadl)

    suspend fun insertAll(zekrFadl: List<ZekrFadl>)

    suspend fun update(zekrFadl: ZekrFadl)

    fun findAll(): Flow<List<ZekrFadl>>

    fun findByZekrId(zekrId: Long): Flow<List<ZekrFadl>>

    fun findByCategoryId(id: Long): Flow<List<ZekrFadl>>
}