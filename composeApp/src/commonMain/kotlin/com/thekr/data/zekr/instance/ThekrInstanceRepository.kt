package com.thekr.data.thekr.instance

import com.thekr.model.ThekrInstance
import kotlinx.coroutines.flow.Flow

interface ThekrInstanceRepository {
    suspend fun insert(thekrInstance: ThekrInstance):Long
    suspend fun insertAll(thekrInstance: List<ThekrInstance>)
    suspend fun update(thekrInstance: ThekrInstance)
    suspend fun deleteIfNotProtected(id: Long)
    fun findById(id: Long): Flow<ThekrInstance>
    fun findAll(): Flow<List<ThekrInstance>>
    fun findByCategoryId(categoryId: Long): Flow<List<ThekrInstance>>
}