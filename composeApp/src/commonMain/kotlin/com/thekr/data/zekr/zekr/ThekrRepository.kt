package com.thekr.data.thekr.thekr

import com.thekr.model.Thekr
import kotlinx.coroutines.flow.Flow

interface ThekrRepository {
    suspend fun insert(thekr: Thekr): Long
    suspend fun insertAll(thekrList: List<Thekr>)
    fun findAll(): Flow<List<Thekr>>
    fun findByCategoryId(categoryId: Long): Flow<List<Thekr>>
    fun findById(id: Long): Flow<Thekr?>
    suspend fun deleteIfNotProtected(id: Long)
    suspend fun getLastThekr(): Thekr?
    suspend fun update(thekr: Thekr)
}