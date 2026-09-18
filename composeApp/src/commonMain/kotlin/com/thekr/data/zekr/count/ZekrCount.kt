package com.thekr.data.thekr.count

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.thekr.util.TimeHelper.now

/**
 * Aggregated counts displayed for one Thekr row.
 *
 * [thekrId] is the Thekr definition id and the key the countList entry is
 * matched by — one entry per thekr, matching the per-thekr card displays.
 * [instanceId] carries the true per-instance id the aggregation was
 * derived from, so consumers can migrate to per-instance keying without
 * re-plumbing the collectors.
 *
 * [timeUpdated] is the entry's data-freshness clock: for collector-derived
 * entries it is the time of the newest persisted count row, so a Room
 * emission only overwrites an entry whose optimistic in-memory increments
 * are not newer than the persisted data.
 */
@Stable
@Immutable
data class ThekrCount(
    val thekrId: Long = 0,
    val instanceId: Long = 0,
    val categoryId: Long = 0,
    val dailyCount: Long = 0,
    val weeklyCount: Long = 0,
    val monthlyCount: Long = 0,
    val yearlyCount: Long = 0,
    val totalCount: Long = 0,
    val timeUpdated: Long = now(),
) {
    /**
     * Compatibility alias for pre-rename consumers: the value is the Thekr
     * definition id, not an instance id.
     */
    @Deprecated("The entry is keyed by the Thekr definition id; use thekrId")
    val thekrInstanceId: Long
        get() = thekrId

    /** Compatibility constructor for pre-rename construction sites. */
    @Deprecated("The entry is keyed by the Thekr definition id; use thekrId")
    constructor(
        thekrInstanceId: Long = 0,
        categoryId: Long = 0,
        dailyCount: Long = 0,
        weeklyCount: Long = 0,
        monthlyCount: Long = 0,
        yearlyCount: Long = 0,
        totalCount: Long = 0,
        timeUpdated: Long = now(),
    ) : this(
        thekrId = thekrInstanceId,
        categoryId = categoryId,
        dailyCount = dailyCount,
        weeklyCount = weeklyCount,
        monthlyCount = monthlyCount,
        yearlyCount = yearlyCount,
        totalCount = totalCount,
        timeUpdated = timeUpdated,
    )
}
