package com.thekr.data.zekr.category

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.thekr.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: Category): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(categoryList: List<Category>)

    @Delete
    suspend fun delete(category: Category)

    @Update
    suspend fun update(category: Category)

    @Query("SELECT * FROM category WHERE id = :id")
    fun findById(id: Long): Flow<Category>

    @Query("SELECT * FROM category")
    fun findAll(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE parent = :parent")
    fun findByParentId(parent: Long): Flow<List<Category>>

    @Query("SELECT EXISTS (SELECT 1 FROM category WHERE parent = :id)")
    fun isCategoryHasChild(id: Long): Flow<Boolean>

    @Query("SELECT * FROM category WHERE id != :id")
    fun findAllExcept(id: Long): List<Category>

    @Query("SELECT * FROM category WHERE id = :id OR parent = :id")
    fun findByRootId(id: Long): Flow<List<Category>>
}