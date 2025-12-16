package com.thekr.util

import korlibs.time.Year
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object TimeHelper {
    data class TimeHelper(
        val midnight: Long = calcMidnight(Clock.System.now().toEpochMilliseconds()),
        val nextMidnight: Long = midnight + 24 * 60 * 60 * 1000,
        val weekStart: Long = calcWeekStart(midnight),
        val weekEnd: Long = calcWeekEnd(midnight),
        val monthStart: Long = calcMonthStart(midnight),
        val monthEnd: Long = calcMonthEnd(midnight),
        val yearStart: Long = calcYearStart(midnight),
        val yearEnd: Long = calcYearEnd(midnight),
    )

    private var timeHelper = TimeHelper()
    fun now() = Clock.System.now().toEpochMilliseconds()
    fun midnight() = timeHelper.midnight
    fun nextMidnight() = timeHelper.nextMidnight
    fun weekStart() = timeHelper.weekStart
    fun weekEnd() = timeHelper.weekEnd
    fun monthStart() = timeHelper.monthStart
    fun monthEnd() = timeHelper.monthEnd
    fun yearStart() = timeHelper.yearStart
    fun yearEnd() = timeHelper.yearEnd


    // todo: update timeHelper on next midnight


    /**
     * Calculates the time in milliseconds for midnight (start of day) of the
     * given time.
     *
     * @param time The time in milliseconds (defaults to current time).
     * @param timeZone The time zone to use (defaults to the default time
     *     zone).
     * @return The time in milliseconds representing midnight of the given
     *     time.
     */
    fun calcMidnight(
        time: Long = now(),
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): Long {
        return Instant.fromEpochMilliseconds(time)
            .toLocalDateTime(timeZone)
            .date
            .atStartOfDayIn(timeZone)
            .toEpochMilliseconds()
    }

    /**
     * Calculates the time in milliseconds for the start of the week (Saturday)
     * of the given time.
     *
     * @param time The time in milliseconds.
     * @param timeZone The time zone to use (defaults to the default time
     *     zone).
     * @param weekStart The day of the week to start the week on (defaults to
     *     Saturday).
     * @return The time in milliseconds representing the start of the week
     *     (Saturday).
     */
    fun calcWeekStart(
        time: Long,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
        weekStart: DayOfWeek = DayOfWeek.SATURDAY
    ): Long {
        return Instant.fromEpochMilliseconds(time)
            .toLocalDateTime(timeZone)
            .date
            .let { date ->
                val daysToSubtract = (date.dayOfWeek.isoDayNumber - weekStart.isoDayNumber + 7) % 7
                date.minus(daysToSubtract, DateTimeUnit.DAY)
                    .atStartOfDayIn(timeZone)
                    .toEpochMilliseconds()
            }
    }

    /**
     * Calculates the time in milliseconds for the end of the week (Friday) of
     * the given time.
     *
     * @param time The time in milliseconds.
     * @param timeZone The time zone to use (defaults to the default time
     *     zone).
     * @param weekStart The day of the week to start the week on (defaults to
     *     Saturday).
     * @return The time in milliseconds representing the end of the week
     *     (Friday).
     */
    fun calcWeekEnd(
        time: Long,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
        weekStart: DayOfWeek = DayOfWeek.SATURDAY
    ): Long {
        return Instant.fromEpochMilliseconds(time)
            .toLocalDateTime(timeZone)
            .date
            .let { date ->
                val daysToAdd = (weekStart.isoDayNumber - date.dayOfWeek.isoDayNumber + 7) % 7
                date.plus(daysToAdd, DateTimeUnit.DAY)
                    .atStartOfDayIn(timeZone)
                    .toEpochMilliseconds()
            }
    }

    /**
     * Calculates the time in milliseconds for the start of the month of the
     * given time.
     *
     * @param time The time in milliseconds.
     * @param timeZone The time zone to use (defaults to the default time
     *     zone).
     * @return The time in milliseconds representing the start of the month.
     */
    fun calcMonthStart(time: Long, timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
        return Instant.fromEpochMilliseconds(time)
            .toLocalDateTime(timeZone)
            .date
            .let {
                LocalDate(it.year, it.month.number, 1)
                    .atStartOfDayIn(timeZone)
                    .toEpochMilliseconds()
            }
    }

    /**
     * Calculates the time in milliseconds for the end of the month of the
     * given time.
     *
     * @param time The time in milliseconds.
     * @param timeZone The time zone to use (defaults to the default time
     *     zone).
     * @return The time in milliseconds representing the end of the month.
     */
    fun calcMonthEnd(time: Long, timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
        return Instant.fromEpochMilliseconds(time)
            .toLocalDateTime(timeZone)
            .date
            .let {
                LocalDate(it.year, it.month.number, it.month.maxLength(it.year))
                    .plus(1, DateTimeUnit.DAY)
                    .atStartOfDayIn(timeZone)
                    .toEpochMilliseconds()
            }
    }

    private fun Month.maxLength(year: Int): Int {
        return when (this) {
            Month.FEBRUARY -> if (Year(year).isLeap) 29 else 28
            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
            else -> 31
        }
    }

    /**
     * Calculates the time in milliseconds for the start of the year of the
     * given time.
     *
     * @param time The time in milliseconds.
     * @param timeZone The time zone to use (defaults to the default time
     *     zone).
     * @return The time in milliseconds representing the start of the year.
     */
    fun calcYearStart(time: Long, timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
        return Instant.fromEpochMilliseconds(time)
            .toLocalDateTime(timeZone)
            .date
            .let {
                LocalDate(it.year, 1, 1)
                    .atStartOfDayIn(timeZone)
                    .toEpochMilliseconds()
            }
    }

    /**
     * Calculates the time in milliseconds for the end of the year of the given
     * time.
     *
     * @param time The time in milliseconds.
     * @param timeZone The time zone to use (defaults to the default time
     *     zone).
     * @return The time in milliseconds representing the end of the year.
     */
    fun calcYearEnd(time: Long, timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
        return Instant.fromEpochMilliseconds(time)
            .toLocalDateTime(timeZone)
            .date
            .let {
                LocalDate(it.year, 12, 31)
                    .plus(1, DateTimeUnit.DAY)
                    .atStartOfDayIn(timeZone)
                    .toEpochMilliseconds()
            }
    }

}
