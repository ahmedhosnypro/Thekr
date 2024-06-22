package com.thekr.data.zekr.zekr

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.thekr.model.Zekr
import kotlinx.coroutines.flow.Flow

@Dao
interface ZekrDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(zekr: Zekr): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(zekrList: List<Zekr>)

    @Query("DELETE FROM zekr WHERE id = :id AND isProtected = 0")
    suspend fun deleteIfNotProtected(id: Long)

    @Query("SELECT * FROM zekr WHERE id = :id")
    fun findById(id: Long): Flow<Zekr>

    @Query("SELECT * FROM zekr")
    fun findAll(): Flow<List<Zekr>>

    @Query("SELECT * FROM zekr WHERE id IN (SELECT zekrId FROM zekr_instance WHERE categoryId = :categoryId)")
    fun findByCategoryId(categoryId: Long): Flow<List<Zekr>>

    @Query("SELECT * FROM zekr ORDER BY timeCreated DESC LIMIT 1")
    suspend fun getLastZekr(): Zekr?

    @Update
    suspend fun update(zekr: Zekr)
}