package com.thekr.data.count.count

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thekr.model.Count
import kotlinx.coroutines.flow.Flow

@Dao
interface CountDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(count: Count)

    // Combine similar queries using a single base query with optional parameters
    @Query(
        """
        SELECT * FROM count
        WHERE (:thekrInstanceId IS NULL OR thekrInstanceId = :thekrInstanceId)
        AND (:timeCreatedAfter IS NULL OR timeCreated > :timeCreatedAfter)
        AND (:timeCreatedBefore IS NULL OR timeCreated < :timeCreatedBefore)
    """
    )
    fun findCounts(
        thekrInstanceId: Long? = null,
        timeCreatedAfter: Long? = null,
        timeCreatedBefore: Long? = null
    ): Flow<List<Count>>

    // Use a single query for both total and filtered counts
    @Query(
        """
        SELECT COUNT(*) FROM count
        WHERE (:thekrInstanceId IS NULL OR thekrInstanceId = :thekrInstanceId)
        AND (:timeCreatedAfter IS NULL OR timeCreated > :timeCreatedAfter)
        AND (:timeCreatedBefore IS NULL OR timeCreated < :timeCreatedBefore)
    """
    )
    fun getCount(
        thekrInstanceId: Long? = null,
        timeCreatedAfter: Long? = null,
        timeCreatedBefore: Long? = null
    ): Flow<Int>

    // Simplify last count retrieval
    @Query("SELECT * FROM count WHERE thekrInstanceId = :thekrInstanceId ORDER BY timeCreated DESC LIMIT 1")
    fun getLastCountByThekrInstanceId(thekrInstanceId: Long): Flow<Count?>

    // Synchronous queries (use sparingly and only when necessary)
    @Query("SELECT * FROM count")
    suspend fun findAllSync(): List<Count>

    @Query("SELECT * FROM count WHERE thekrCategoryId = :categoryId")
    fun findAllByCategorySync(categoryId: Long): Flow<List<Count>>

    @Query("SELECT * FROM count WHERE thekrInstanceId = :thekrInstanceId")
    fun findAllByThekrInstanceSync(thekrInstanceId: Long): Flow<List<Count>>
}