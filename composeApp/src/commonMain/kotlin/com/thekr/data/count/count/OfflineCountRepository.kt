package com.thekr.data.count.count

import com.thekr.model.Count
import kotlinx.coroutines.flow.Flow

class OfflineCountRepository(private val countDao: CountDAO) : CountRepository {
    override suspend fun insert(count: Count) {
        countDao.insert(count)
    }

    override fun findCounts(
        thekrInstanceId: Long?,
        timeCreatedAfter: Long?,
        timeCreatedBefore: Long?
    ) = countDao.findCounts(thekrInstanceId, timeCreatedAfter, timeCreatedBefore)

    override fun getCount(
        thekrInstanceId: Long?,
        timeCreatedAfter: Long?,
        timeCreatedBefore: Long?
    ) = countDao.getCount(thekrInstanceId, timeCreatedAfter, timeCreatedBefore)

    override fun getLastCountByThekrInstanceId(thekrInstanceId: Long) =
        countDao.getLastCountByThekrInstanceId(thekrInstanceId)

    override suspend fun findAllSync() = countDao.findAllSync()

    override fun findAllByCategorySync(categoryId: Long) =
        countDao.findAllByCategorySync(categoryId)

    override fun findAllByThekrInstanceSync(thekrInstanceId: Long) =
        countDao.findAllByThekrInstanceSync(thekrInstanceId)
}