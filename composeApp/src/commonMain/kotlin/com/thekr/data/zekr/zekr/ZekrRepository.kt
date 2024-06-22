package com.thekr.data.zekr.zekr

import com.thekr.model.Zekr
import kotlinx.coroutines.flow.Flow

interface ZekrRepository {
    suspend fun insert(zekr: Zekr): Long
    suspend fun insertAll(zekrList: List<Zekr>)
    fun findAll(): Flow<List<Zekr>>
    fun findByCategoryId(categoryId: Long): Flow<List<Zekr>>
    fun findById(id: Long): Flow<Zekr?>
    suspend fun deleteIfNotProtected(id: Long)
    suspend fun getLastZekr(): Zekr?
    suspend fun update(zekr: Zekr)
}