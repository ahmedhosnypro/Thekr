package com.thekr.data.session

import com.thekr.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun insert(session: Session)

    suspend fun update(session: Session)

    fun findAll(): Flow<List<Session>>

    fun findById(id: Long): Flow<Session>

    /**
     * Finds an active session for the given thekrCategoryId and thekrInstanceId.
     * An active session is defined as one where timeEnded is 0.
     *
     * @param thekrCategoryId The ID of the Thekr category.
     * @param thekrInstanceId The ID of the Thekr instance.
     * @return Flow emitting the active session, if any.
     */
    fun findActiveSession(thekrCategoryId: Long, thekrInstanceId: Long): Flow<Session>
}