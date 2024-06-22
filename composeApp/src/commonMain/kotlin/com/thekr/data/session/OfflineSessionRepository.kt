package com.thekr.data.session

import com.thekr.model.Session

class OfflineSessionRepository(
    private val sessionDAO: SessionDAO
) : SessionRepository {
    override suspend fun insert(session: Session) = sessionDAO.insert(session)

    override suspend fun update(session: Session) = sessionDAO.update(session)

    override fun findAll() = sessionDAO.findAll()

    override fun findById(id: Long) = sessionDAO.findById(id)

    override fun findActiveSession(zekrCategoryId: Long, zekrInstanceId: Long) =
        sessionDAO.findActiveSession(zekrCategoryId, zekrInstanceId)
}