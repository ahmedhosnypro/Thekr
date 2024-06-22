package com.thekr.data.count.count

import com.thekr.model.Count
import kotlinx.coroutines.flow.Flow

class OfflineCountRepository(private val countDao: CountDAO) : CountRepository {
    override suspend fun insert(count: Count) {
        countDao.insert(count)
    }

    override fun findCounts(
        zekrInstanceId: Long?,
        timeCreatedAfter: Long?,
        timeCreatedBefore: Long?
    ) = countDao.findCounts(zekrInstanceId, timeCreatedAfter, timeCreatedBefore)

    override fun getCount(
        zekrInstanceId: Long?,
        timeCreatedAfter: Long?,
        timeCreatedBefore: Long?
    ) = countDao.getCount(zekrInstanceId, timeCreatedAfter, timeCreatedBefore)

    override fun getLastCountByZekrInstanceId(zekrInstanceId: Long) =
        countDao.getLastCountByZekrInstanceId(zekrInstanceId)

    override suspend fun findAllSync() = countDao.findAllSync()

    override fun findAllByCategorySync(categoryId: Long) =
        countDao.findAllByCategorySync(categoryId)

    override fun findAllByZekrInstanceSync(zekrInstanceId: Long) =
        countDao.findAllByZekrInstanceSync(zekrInstanceId)
}