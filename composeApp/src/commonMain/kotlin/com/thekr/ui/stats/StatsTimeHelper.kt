package com.thekr.ui.stats

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object StatsTimeHelper {

    /**
     * Midnight (start of local day) of `time` shifted by `days` calendar
     * days. Navigates through the local date instead of absolute 24h steps,
     * so it stays correct across DST transitions in either direction.
     */
    fun midnightOffsetBy(
        time: Long,
        days: Int,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): Long {
        return Instant.fromEpochMilliseconds(time)
            .toLocalDateTime(timeZone)
            .date
            .plus(days, DateTimeUnit.DAY)
            .atStartOfDayIn(timeZone)
            .toEpochMilliseconds()
    }
}
