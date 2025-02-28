package com.thekr.data.thekr.fadl

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.thekr.model.ThekrFadl
import kotlinx.coroutines.flow.Flow

@Dao
interface FadlDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(fadl: ThekrFadl)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(fadlList: List<ThekrFadl>)

    @Update
    suspend fun update(thekrFadl: ThekrFadl)

    @Query("SELECT * FROM thekr_fadl")
    fun findAll(): Flow<List<ThekrFadl>>

    @Query("SELECT * FROM thekr_fadl WHERE thekrId = :thekrId")
    fun findByThekrId(thekrId: Long): Flow<List<ThekrFadl>>

    @Query("SELECT * FROM thekr_fadl WHERE thekrId IN (SELECT thekrId FROM thekr_instance WHERE categoryId = :categoryId)")
    fun findByCategoryId(categoryId: Long): Flow<List<ThekrFadl>>
}