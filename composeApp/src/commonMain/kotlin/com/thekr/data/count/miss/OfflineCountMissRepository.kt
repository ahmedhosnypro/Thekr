package com.thekr.data.count.miss

import com.thekr.model.CountMiss
import kotlinx.coroutines.flow.Flow

class OfflineCountMissRepository(
    private val countMissDAO: CountMissDAO
) : CountMissRepository {
    override suspend fun insert(countMiss: CountMiss) = countMissDAO.insert(countMiss)

    override fun findAll(): Flow<List<CountMiss>> = countMissDAO.findAll()

    override fun findByZekrInstanceId(zekrId: Long): Flow<List<CountMiss>> =
        countMissDAO.findByZekrInstanceId(zekrId)

    override fun getTotalCountByZekrId(zekrId: Long): Flow<Long> =
        countMissDAO.getTotalCountByZekrInstanceId(zekrId)
}