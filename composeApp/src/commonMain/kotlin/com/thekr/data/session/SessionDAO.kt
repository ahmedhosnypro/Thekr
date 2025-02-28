package com.thekr.data.session

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.thekr.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(session: Session)

    @Update
    suspend fun update(session: Session)

    @Query("SELECT * from session")
    fun findAll(): Flow<List<Session>>

    @Query("SELECT * from session WHERE id = :id")
    fun findById(id: Long): Flow<Session>

    /**
     * Finds an active session for the given thekrCategoryId and thekrInstanceId.
     * An active session is defined as one where timeEnded is 0.
     *
     * @param thekrCategoryId The ID of the Thekr category.
     * @param thekrInstanceId The ID of the Thekr instance.
     * @return Flow emitting the active session, if any.
     */
    @Query("SELECT * from session WHERE thekrCategoryId = :thekrCategoryId AND thekrInstanceId = :thekrInstanceId AND timeEnded = 0")
    fun findActiveSession(thekrCategoryId: Long, thekrInstanceId: Long): Flow<Session>
}