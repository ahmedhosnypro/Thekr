package com.thekr.data.count.miss

import com.thekr.model.CountMiss
import kotlinx.coroutines.flow.Flow

class OfflineCountMissRepository(
    private val countMissDAO: CountMissDAO
) : CountMissRepository {
    override suspend fun insert(countMiss: CountMiss) = countMissDAO.insert(countMiss)

    override fun findAll(): Flow<List<CountMiss>> = countMissDAO.findAll()

    override fun findByThekrInstanceId(thekrId: Long): Flow<List<CountMiss>> =
        countMissDAO.findByThekrInstanceId(thekrId)

    override fun getTotalCountByThekrId(thekrId: Long): Flow<Long> =
        countMissDAO.getTotalCountByThekrInstanceId(thekrId)
}