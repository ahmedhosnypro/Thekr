package com.thekr.data.zekr.instance

import com.thekr.model.ZekrInstance
import kotlinx.coroutines.flow.Flow

interface ZekrInstanceRepository {
    suspend fun insert(zekrInstance: ZekrInstance):Long
    suspend fun insertAll(zekrInstance: List<ZekrInstance>)
    suspend fun update(zekrInstance: ZekrInstance)
    suspend fun deleteIfNotProtected(id: Long)
    fun findById(id: Long): Flow<ZekrInstance>
    fun findAll(): Flow<List<ZekrInstance>>
    fun findByCategoryId(categoryId: Long): Flow<List<ZekrInstance>>
}