package com.thekr.data.thekr.fadl

import com.thekr.model.ThekrFadl

class OfflineFadlRepository(private val fadlDAO: FadlDAO) : FadlRepository {
    override suspend fun insert(thekrFadl: ThekrFadl) = fadlDAO.insert(thekrFadl)

    override suspend fun insertAll(thekrFadl: List<ThekrFadl>) = fadlDAO.insertAll(thekrFadl)

    override suspend fun update(thekrFadl: ThekrFadl) = fadlDAO.update(thekrFadl)

    override fun findAll() = fadlDAO.findAll()

    override fun findByThekrId(thekrId: Long) = fadlDAO.findByThekrId(thekrId)

    override fun findByCategoryId(id: Long) = fadlDAO.findByCategoryId(id)
}