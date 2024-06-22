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
     * Finds an active session for the given zekrCategoryId and zekrInstanceId.
     * An active session is defined as one where timeEnded is 0.
     *
     * @param zekrCategoryId The ID of the Zekr category.
     * @param zekrInstanceId The ID of the Zekr instance.
     * @return Flow emitting the active session, if any.
     */
    @Query("SELECT * from session WHERE zekrCategoryId = :zekrCategoryId AND zekrInstanceId = :zekrInstanceId AND timeEnded = 0")
    fun findActiveSession(zekrCategoryId: Long, zekrInstanceId: Long): Flow<Session>
}