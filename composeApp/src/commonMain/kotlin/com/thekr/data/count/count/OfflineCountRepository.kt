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

    override suspend fun findAll() = countDao.findAll()

    override fun findAllByCategory(categoryId: Long) =
        countDao.findAllByCategory(categoryId)

    override fun findAllByThekrInstance(thekrInstanceId: Long) =
        countDao.findAllByThekrInstance(thekrInstanceId)

    override fun getCountTotalsByThekrInstanceId(
        thekrInstanceId: Long,
        periods: CountPeriodBounds,
    ) = countDao.getCountTotalsByThekrInstanceId(
        thekrInstanceId = thekrInstanceId,
        dailyStart = periods.dailyStart,
        dailyEnd = periods.dailyEnd,
        weeklyStart = periods.weeklyStart,
        weeklyEnd = periods.weeklyEnd,
        monthlyStart = periods.monthlyStart,
        monthlyEnd = periods.monthlyEnd,
        yearlyStart = periods.yearlyStart,
        yearlyEnd = periods.yearlyEnd,
    )
}