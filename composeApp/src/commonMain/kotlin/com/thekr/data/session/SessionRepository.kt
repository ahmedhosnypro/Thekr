package com.thekr.data.session

import com.thekr.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun insert(session: Session)

    suspend fun update(session: Session)

    fun findAll(): Flow<List<Session>>

    fun findById(id: Long): Flow<Session>

    /**
     * Finds an active session for the given zekrCategoryId and zekrInstanceId.
     * An active session is defined as one where timeEnded is 0.
     *
     * @param zekrCategoryId The ID of the Zekr category.
     * @param zekrInstanceId The ID of the Zekr instance.
     * @return Flow emitting the active session, if any.
     */
    fun findActiveSession(zekrCategoryId: Long, zekrInstanceId: Long): Flow<Session>
}