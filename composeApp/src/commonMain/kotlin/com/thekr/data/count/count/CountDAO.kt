package com.thekr.data.count.count

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thekr.model.Count
import kotlinx.coroutines.flow.Flow

data class ThekrInstanceCountTotals(
    val dailyCount: Long,
    val weeklyCount: Long,
    val monthlyCount: Long,
    val yearlyCount: Long,
    val totalCount: Long,
)

data class CountPeriodBounds(
    val dailyStart: Long,
    val dailyEnd: Long,
    val weeklyStart: Long,
    val weeklyEnd: Long,
    val monthlyStart: Long,
    val monthlyEnd: Long,
    val yearlyStart: Long,
    val yearlyEnd: Long,
)

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

    // Suspend one-shot query (use sparingly and only when necessary)
    @Query("SELECT * FROM count")
    suspend fun findAll(): List<Count>

    @Query("SELECT * FROM count WHERE thekrCategoryId = :categoryId")
    fun findAllByCategory(categoryId: Long): Flow<List<Count>>

    @Query("SELECT * FROM count WHERE thekrInstanceId = :thekrInstanceId")
    fun findAllByThekrInstance(thekrInstanceId: Long): Flow<List<Count>>

    @Query(
        """
        SELECT
            COUNT(CASE WHEN timeCreated >= :dailyStart AND timeCreated < :dailyEnd THEN 1 END) AS dailyCount,
            COUNT(CASE WHEN timeCreated >= :weeklyStart AND timeCreated < :weeklyEnd THEN 1 END) AS weeklyCount,
            COUNT(CASE WHEN timeCreated >= :monthlyStart AND timeCreated < :monthlyEnd THEN 1 END) AS monthlyCount,
            COUNT(CASE WHEN timeCreated >= :yearlyStart AND timeCreated < :yearlyEnd THEN 1 END) AS yearlyCount,
            COUNT(*) AS totalCount
        FROM count
        WHERE thekrInstanceId = :thekrInstanceId
    """
    )
    fun getCountTotalsByThekrInstanceId(
        thekrInstanceId: Long,
        dailyStart: Long = 0,
        dailyEnd: Long = 0,
        weeklyStart: Long = 0,
        weeklyEnd: Long = 0,
        monthlyStart: Long = 0,
        monthlyEnd: Long = 0,
        yearlyStart: Long = 0,
        yearlyEnd: Long = 0,
    ): Flow<ThekrInstanceCountTotals>
}