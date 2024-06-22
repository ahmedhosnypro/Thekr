package com.thekr.data.zekr.zekr

import com.thekr.model.Zekr

class OfflineZekrRepository(private val zekrDAO: ZekrDAO) : ZekrRepository {
    override suspend fun insert(zekr: Zekr) = zekrDAO.insert(zekr)

    override suspend fun insertAll(zekrList: List<Zekr>) = zekrDAO.insertAll(zekrList)

    override fun findAll() = zekrDAO.findAll()

    override fun findByCategoryId(categoryId: Long) = zekrDAO.findByCategoryId(categoryId)

    override fun findById(id: Long) = zekrDAO.findById(id)

    override suspend fun deleteIfNotProtected(id: Long) = zekrDAO.deleteIfNotProtected(id)

    override suspend fun getLastZekr(): Zekr? = zekrDAO.getLastZekr()

    override suspend fun update(zekr: Zekr) = zekrDAO.update(zekr)
}