package com.thekr.data.zekr.fadl

import com.thekr.model.ZekrFadl

class OfflineFadlRepository(private val fadlDAO: FadlDAO) : FadlRepository {
    override suspend fun insert(zekrFadl: ZekrFadl) = fadlDAO.insert(zekrFadl)

    override suspend fun insertAll(zekrFadl: List<ZekrFadl>) = fadlDAO.insertAll(zekrFadl)

    override suspend fun update(zekrFadl: ZekrFadl) = fadlDAO.update(zekrFadl)

    override fun findAll() = fadlDAO.findAll()

    override fun findByZekrId(zekrId: Long) = fadlDAO.findByZekrId(zekrId)

    override fun findByCategoryId(id: Long) = fadlDAO.findByCategoryId(id)
}