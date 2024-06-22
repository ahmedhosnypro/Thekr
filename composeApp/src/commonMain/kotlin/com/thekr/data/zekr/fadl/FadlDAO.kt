package com.thekr.data.zekr.fadl

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.thekr.model.ZekrFadl
import kotlinx.coroutines.flow.Flow

@Dao
interface FadlDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(fadl: ZekrFadl)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(fadlList: List<ZekrFadl>)

    @Update
    suspend fun update(zekrFadl: ZekrFadl)

    @Query("SELECT * FROM zekr_fadl")
    fun findAll(): Flow<List<ZekrFadl>>

    @Query("SELECT * FROM zekr_fadl WHERE zekrId = :zekrId")
    fun findByZekrId(zekrId: Long): Flow<List<ZekrFadl>>

    @Query("SELECT * FROM zekr_fadl WHERE zekrId IN (SELECT zekrId FROM zekr_instance WHERE categoryId = :categoryId)")
    fun findByCategoryId(categoryId: Long): Flow<List<ZekrFadl>>
}