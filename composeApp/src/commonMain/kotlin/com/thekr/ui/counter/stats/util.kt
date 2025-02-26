package com.thekr.ui.counter.stats

import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.Axis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianValueFormatter

private class IntFormatter : CartesianValueFormatter {
    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?,
    ): CharSequence {
        println("IntFormatter, value: $value, result: ${value.toInt()}")
        return value.toInt().toString()
    }

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
    ): CharSequence {
        val result = when (val hour = value.toInt()) {
            0 -> "12 $am"
            4 -> "4 $am"
            8 -> "8 $am"
            12 -> "12 $pm"
            16 -> "4 $pm"
            20 -> "8 $pm"
            23 -> "11 $pm"
            else -> " "
        }
        println("HourFormatter, value: $value, result: $result")
        return result
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
    ): CharSequence {
        val result = when (val minute = value.toInt()) {
            30 -> "12 $am"
            240 -> "4 $am"
            480 -> "8 $am"
            720 -> "12 $pm"
            960 -> "4 $pm"
            1200 -> "8 $pm"
            1410 -> "12 $am"
            else -> " "
        }
        println("MinuteFormatter, value: $value, result: $result")
        return result
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