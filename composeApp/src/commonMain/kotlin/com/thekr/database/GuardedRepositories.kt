package com.thekr.database

import com.thekr.data.count.count.CountPeriodBounds
import com.thekr.data.count.count.CountRepository
import com.thekr.data.count.count.ThekrInstanceCountTotals
import com.thekr.data.count.miss.CountMissRepository
import com.thekr.data.session.SessionRepository
import com.thekr.model.Count
import com.thekr.model.CountMiss
import com.thekr.model.Session
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen

/**
 * Guards the flows the database layer exposes so a mid-session Room/IO
 * failure cannot kill a collector: the failure is logged and the cold
 * flow is re-collected after a growing delay, keeping the last good
 * emission in the UI while retrying. The upstream Room flow is
 * re-queried on each retry, so a transient error (disk briefly full,
 * IO hiccup) self-heals without any process crash.
 *
 * Deliberate semantics:
 *  - [CancellationException] and non-[Exception] throwables (OOM etc.)
 *    are rethrown untouched — collector cancellation and fatal errors
 *    must keep propagating.
 *  - No fallback data is ever emitted: a failing query must not fake
 *    an empty or zeroed result in the UI.
 *  - Suspend write paths are NOT guarded here — swallowing a failed
 *    count-tap insert would silently fake success in the UI. Write
 *    call sites must observe the failure (see the M76/M74 follow-up).
 */
internal fun <T> Flow<T>.guardedEmissions(): Flow<T> = retryWhen { cause, attempt ->
    if (cause is CancellationException || cause !is Exception) throw cause
    val delayMillis =
        minOf(RetryBackoffBaseMillis * (attempt + 1), RetryBackoffMaxMillis)
    println(
        "DatabaseFlow: emission failed (retry ${attempt + 1} in $delayMillis ms): $cause",
    )
    delay(delayMillis)
    true
}

internal class GuardedCountRepository(
    private val delegate: CountRepository,
) : CountRepository by delegate {
    override fun findCounts(
        thekrInstanceId: Long?,
        timeCreatedAfter: Long?,
        timeCreatedBefore: Long?,
    ): Flow<List<Count>> =
        delegate
            .findCounts(thekrInstanceId, timeCreatedAfter, timeCreatedBefore)
            .guardedEmissions()

    override fun getCount(
        thekrInstanceId: Long?,
        timeCreatedAfter: Long?,
        timeCreatedBefore: Long?,
    ): Flow<Int> =
        delegate
            .getCount(thekrInstanceId, timeCreatedAfter, timeCreatedBefore)
            .guardedEmissions()

    override fun getLastCountByThekrInstanceId(thekrInstanceId: Long): Flow<Count?> =
        delegate.getLastCountByThekrInstanceId(thekrInstanceId).guardedEmissions()

    override fun findAllByCategory(categoryId: Long): Flow<List<Count>> =
        delegate.findAllByCategory(categoryId).guardedEmissions()

    override fun findAllByThekrInstance(thekrInstanceId: Long): Flow<List<Count>> =
        delegate.findAllByThekrInstance(thekrInstanceId).guardedEmissions()

    override fun getCountTotalsByThekrInstanceId(
        thekrInstanceId: Long,
        periods: CountPeriodBounds,
    ): Flow<ThekrInstanceCountTotals> =
        delegate
            .getCountTotalsByThekrInstanceId(thekrInstanceId, periods)
            .guardedEmissions()
}

internal class GuardedCountMissRepository(
    private val delegate: CountMissRepository,
) : CountMissRepository by delegate {
    override fun findAll(): Flow<List<CountMiss>> = delegate.findAll().guardedEmissions()

    override fun findByThekrInstanceId(thekrId: Long): Flow<List<CountMiss>> =
        delegate.findByThekrInstanceId(thekrId).guardedEmissions()

    override fun getTotalCountByThekrId(thekrId: Long): Flow<Long> =
        delegate.getTotalCountByThekrId(thekrId).guardedEmissions()
}

internal class GuardedSessionRepository(
    private val delegate: SessionRepository,
) : SessionRepository by delegate {
    override fun findAll(): Flow<List<Session>> = delegate.findAll().guardedEmissions()

    override fun findById(id: Long): Flow<Session?> = delegate.findById(id).guardedEmissions()

    override fun findActiveSession(
        thekrCategoryId: Long,
        thekrInstanceId: Long,
    ): Flow<Session?> =
        delegate
            .findActiveSession(thekrCategoryId, thekrInstanceId)
            .guardedEmissions()
}

private const val RetryBackoffBaseMillis = 1_000L
private const val RetryBackoffMaxMillis = 30_000L
