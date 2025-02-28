package com.thekr.data.thekr.instance

import com.thekr.model.ThekrInstance
import kotlinx.coroutines.flow.Flow

class OfflineThekrInstanceRepository(private val thekrInstanceDAO: ThekrInstanceDAO) :
    ThekrInstanceRepository {
    override suspend fun insert(thekrInstance: ThekrInstance) = thekrInstanceDAO.insert(thekrInstance)

    override suspend fun insertAll(thekrInstance: List<ThekrInstance>) =
        thekrInstanceDAO.insertAll(thekrInstance)

    override suspend fun update(thekrInstance: ThekrInstance) = thekrInstanceDAO.update(thekrInstance)

    override suspend fun deleteIfNotProtected(id: Long) = thekrInstanceDAO.deleteIfNotProtected(id)

    override fun findById(id: Long): Flow<ThekrInstance> = thekrInstanceDAO.findById(id)

    override fun findAll(): Flow<List<ThekrInstance>> = thekrInstanceDAO.findAll()

    override fun findByCategoryId(categoryId: Long) = thekrInstanceDAO.findByCategoryId(categoryId)
}