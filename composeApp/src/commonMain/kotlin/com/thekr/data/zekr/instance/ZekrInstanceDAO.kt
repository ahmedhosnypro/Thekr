package com.thekr.data.thekr.instance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.thekr.model.ThekrInstance
import kotlinx.coroutines.flow.Flow

@Dao
interface ThekrInstanceDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(thekrInstance: ThekrInstance): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(thekrInstance: List<ThekrInstance>)

    @Update
    suspend fun update(thekrInstance: ThekrInstance)

    @Query("DELETE FROM thekr_instance WHERE id = :id AND isProtected = 0")
    suspend fun deleteIfNotProtected(id: Long)

    @Query("SELECT * FROM thekr_instance WHERE id = :id")
    fun findById(id: Long): Flow<ThekrInstance>

    @Query("SELECT * FROM thekr_instance")
    fun findAll(): Flow<List<ThekrInstance>>

    @Query("SELECT * FROM thekr_instance WHERE categoryId = :categoryId")
    fun findByCategoryId(categoryId: Long): Flow<List<ThekrInstance>>
}