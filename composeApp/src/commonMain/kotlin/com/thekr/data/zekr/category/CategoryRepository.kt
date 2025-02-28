package com.thekr.data.thekr.category

import com.thekr.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun insert(category: Category): Long
    suspend fun insertAll(categoryList: List<Category>)
    fun findById(id: Long): Flow<Category>
    fun findAll(): Flow<List<Category>>
    fun findByParentId(parent: Long): Flow<List<Category>>
    fun findByRootId(id: Long): Flow<List<Category>>

    suspend fun findAllExcept(id: Long): List<Category>
    suspend fun delete(category: Category)
    suspend fun update(category: Category)

    fun isCategoryHasChild(id: Long): Flow<Boolean>
}