package com.thekr.data.count.miss

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thekr.model.CountMiss
import kotlinx.coroutines.flow.Flow

@Dao
interface CountMissDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(countMiss: CountMiss)

    @Query("SELECT * FROM count_miss")
    fun findAll(): Flow<List<CountMiss>>

    @Query("SELECT * FROM count_miss where thekrInstanceId= :thekrInstanceId")
    fun findByThekrInstanceId(thekrInstanceId: Long): Flow<List<CountMiss>>

    @Query("SELECT COUNT(*) FROM count_miss WHERE thekrInstanceId= :thekrInstanceId")
    fun getTotalCountByThekrInstanceId(thekrInstanceId: Long): Flow<Long>
}