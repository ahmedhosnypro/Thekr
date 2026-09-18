package com.thekr.data.count.count

import com.thekr.model.Count
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Buffers count-tap inserts and writes them to Room in batches, so a
 * tapping burst produces one [CountDAO.insertAll] transaction per flush
 * window instead of one committed transaction per tap. The optimistic
 * in-memory count shown by the UI remains the fresh source between
 * flushes; every collector aggregating from Room observes a flush through
 * the normal table emission.
 *
 * The buffer self-flushes on a short debounce after the latest tap and
 * once the batch-size limit is reached; [flush] is the explicit trigger
 * for lifecycle ON_STOP and freshness-sensitive reads.
 */
class OfflineCountRepository(
    private val countDao: CountDAO,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
) : CountRepository {
    private val mutex = Mutex()
    private val pendingCounts = mutableListOf<Count>()
    private var flushJob: Job? = null

    override suspend fun insert(count: Count) {
        val reachedBatchLimit = mutex.withLock {
            pendingCounts.add(count)
            pendingCounts.size >= MAX_PENDING_COUNTS
        }
        if (reachedBatchLimit) {
            flush()
        } else {
            scheduleFlush()
        }
    }

    override suspend fun flush() {
        val batch = mutex.withLock {
            if (pendingCounts.isEmpty()) {
                null
            } else {
                val drained = pendingCounts.toList()
                pendingCounts.clear()
                drained
            }
        } ?: return
        // The batch is already claimed out of the buffer, so it must reach
        // Room even if the surrounding coroutine is cancelled mid-write.
        withContext(NonCancellable) {
            countDao.insertAll(batch)
        }
    }

    /**
     * Each tap resets the debounce timer, so a burst coalesces into one
     * write while the last partial batch still flushes at most
     * [FLUSH_INTERVAL_MILLIS] after the final tap.
     */
    private fun scheduleFlush() {
        flushJob?.cancel()
        flushJob = scope.launch {
            delay(FLUSH_INTERVAL_MILLIS)
            flush()
        }
    }

    override fun findCounts(
        thekrInstanceId: Long?,
        timeCreatedAfter: Long?,
        timeCreatedBefore: Long?,
    ) = countDao.findCounts(thekrInstanceId, timeCreatedAfter, timeCreatedBefore)

    override fun getCount(
        thekrInstanceId: Long?,
        timeCreatedAfter: Long?,
        timeCreatedBefore: Long?,
    ) = countDao.getCount(thekrInstanceId, timeCreatedAfter, timeCreatedBefore)

    override fun getLastCountByThekrInstanceId(thekrInstanceId: Long): Flow<Count?> = flow {
        // Cooldown seeding must observe every tap, including rows still
        // held in the batch buffer.
        flush()
        emitAll(countDao.getLastCountByThekrInstanceId(thekrInstanceId))
    }

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

    private companion object {
        private const val MAX_PENDING_COUNTS = 25
        private const val FLUSH_INTERVAL_MILLIS = 2000L
    }
}
