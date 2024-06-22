package com.thekr.data.zekr.instance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.thekr.model.ZekrInstance
import kotlinx.coroutines.flow.Flow

@Dao
interface ZekrInstanceDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(zekrInstance: ZekrInstance):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(zekrInstance: List<ZekrInstance>)

    @Update
    suspend fun update(zekrInstance: ZekrInstance)

    @Query("DELETE FROM zekr_instance WHERE id = :id AND isProtected = 0")
    suspend fun deleteIfNotProtected(id: Long)

    @Query("SELECT * FROM zekr_instance WHERE id = :id")
    fun findById(id: Long): Flow<ZekrInstance>

    @Query("SELECT * FROM zekr_instance")
    fun findAll(): Flow<List<ZekrInstance>>

    @Query("SELECT * FROM zekr_instance WHERE categoryId = :categoryId")
    fun findByCategoryId(categoryId: Long): Flow<List<ZekrInstance>>
}