package com.thekr.stats.axis

import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.Axis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianValueFormatter

private class IntFormatter : CartesianValueFormatter {
    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?,
    ): CharSequence = value.toInt().toString()

    override fun equals(other: Any?) =
        this === other || other is IntFormatter

    override fun hashCode() = javaClass.hashCode()
}

/** Formats values as integers. */
fun intFormatter(): CartesianValueFormatter = IntFormatter()

private class HourFormatter(
    private val am: String,
    private val pm: String
) : CartesianValueFormatter {
    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?,
    ): CharSequence = when (value.toInt()) {
        0 -> "12 $am"
        4 -> "4 $am"
        8 -> "8 $am"
        12 -> "12 $pm"
        16 -> "4 $pm"
        20 -> "8 $pm"
        23 -> "11 $pm"
        else -> " "
    }

    override fun equals(other: Any?) =
        this === other || other is HourFormatter

    override fun hashCode() = javaClass.hashCode()
}

/** Formats values as hours. */
fun hourFormatter(
    am: String = "am",
    pm: String = "pm",
): CartesianValueFormatter = HourFormatter(am, pm)

private class MinuteFormatter(
    private val am: String,
    private val pm: String
) : CartesianValueFormatter {
    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?,
    ): CharSequence = when (value.toInt()) {
        30 -> "12 $am"
        240 -> "4 $am"
        480 -> "8 $am"
        720 -> "12 $pm"
        960 -> "4 $pm"
        1200 -> "8 $pm"
        1410 -> "12 $am"
        else -> " "
    }

    override fun equals(other: Any?) =
        this === other || other is MinuteFormatter

    override fun hashCode() = javaClass.hashCode()
}

/** Formats values as minutes. */
fun minuteFormatter(
    am: String = "am",
    pm: String = "pm",
): CartesianValueFormatter = MinuteFormatter(am, pm)

private class WeekdayFormatter(
    private val saturday: String,
    private val sunday: String,
    private val monday: String,
    private val tuesday: String,
    private val wednesday: String,
    private val thursday: String,
    private val friday: String
) : CartesianValueFormatter {
    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?,
    ): CharSequence = when (value.toInt()) {
        0 -> saturday
        1 -> sunday
        2 -> monday
        3 -> tuesday
        4 -> wednesday
        5 -> thursday
        6 -> friday
        else -> " "
    }

    override fun equals(other: Any?) =
        this === other || other is WeekdayFormatter

    override fun hashCode() = javaClass.hashCode()
}

/** Formats values as weekdays. */
fun weekdayFormatter(
    saturday: String = "Saturday",
    sunday: String = "Sunday",
    monday: String = "Monday",
    tuesday: String = "Tuesday",
    wednesday: String = "Wednesday",
    thursday: String = "Thursday",
    friday: String = "Friday",
): CartesianValueFormatter = WeekdayFormatter(
    saturday, sunday, monday, tuesday, wednesday, thursday, friday
)