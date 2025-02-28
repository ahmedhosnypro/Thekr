package com.thekr.data.thekr.thekr

import com.thekr.model.Thekr

class OfflineThekrRepository(private val thekrDAO: ThekrDAO) : ThekrRepository {
    override suspend fun insert(thekr: Thekr) = thekrDAO.insert(thekr)

    override suspend fun insertAll(thekrList: List<Thekr>) = thekrDAO.insertAll(thekrList)

    override fun findAll() = thekrDAO.findAll()

    override fun findByCategoryId(categoryId: Long) = thekrDAO.findByCategoryId(categoryId)

    override fun findById(id: Long) = thekrDAO.findById(id)

    override suspend fun deleteIfNotProtected(id: Long) = thekrDAO.deleteIfNotProtected(id)

    override suspend fun getLastThekr(): Thekr? = thekrDAO.getLastThekr()

    override suspend fun update(thekr: Thekr) = thekrDAO.update(thekr)
}