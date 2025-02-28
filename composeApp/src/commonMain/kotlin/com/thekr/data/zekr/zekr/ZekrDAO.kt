package com.thekr.data.thekr.thekr

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.thekr.model.Thekr
import kotlinx.coroutines.flow.Flow

@Dao
interface ThekrDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(thekr: Thekr): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(thekrList: List<Thekr>)

    @Query("DELETE FROM thekr WHERE id = :id AND isProtected = 0")
    suspend fun deleteIfNotProtected(id: Long)

    @Query("SELECT * FROM thekr WHERE id = :id")
    fun findById(id: Long): Flow<Thekr>

    @Query("SELECT * FROM thekr")
    fun findAll(): Flow<List<Thekr>>

    @Query("SELECT * FROM thekr WHERE id IN (SELECT thekrId FROM thekr_instance WHERE categoryId = :categoryId)")
    fun findByCategoryId(categoryId: Long): Flow<List<Thekr>>

    @Query("SELECT * FROM thekr ORDER BY timeCreated DESC LIMIT 1")
    suspend fun getLastThekr(): Thekr?

    @Update
    suspend fun update(thekr: Thekr)
}