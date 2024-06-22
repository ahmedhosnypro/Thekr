package com.thekr.data.zekr.instance

import com.thekr.model.ZekrInstance
import kotlinx.coroutines.flow.Flow

class OfflineZekrInstanceRepository(private val zekrInstanceDAO: ZekrInstanceDAO) :
    ZekrInstanceRepository {
    override suspend fun insert(zekrInstance: ZekrInstance) = zekrInstanceDAO.insert(zekrInstance)

    override suspend fun insertAll(zekrInstance: List<ZekrInstance>) =
        zekrInstanceDAO.insertAll(zekrInstance)

    override suspend fun update(zekrInstance: ZekrInstance) = zekrInstanceDAO.update(zekrInstance)

    override suspend fun deleteIfNotProtected(id: Long) = zekrInstanceDAO.deleteIfNotProtected(id)

    override fun findById(id: Long): Flow<ZekrInstance> = zekrInstanceDAO.findById(id)

    override fun findAll(): Flow<List<ZekrInstance>> = zekrInstanceDAO.findAll()

    override fun findByCategoryId(categoryId: Long) = zekrInstanceDAO.findByCategoryId(categoryId)
}