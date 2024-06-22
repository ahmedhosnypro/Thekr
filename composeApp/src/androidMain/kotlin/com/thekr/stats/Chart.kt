package com.thekr.stats

import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel

fun lineSeries(groupedCount: MutableMap<Int, Int>) =
    listOf(
        groupedCount.values.mapIndexed { index, value ->
            LineCartesianLayerModel.Entry(
                x = index,
                y = value
            )
        }
    )

fun columnSeries(groupedCount: MutableMap<Int, Int>) =
    listOf(
        groupedCount.values.mapIndexed { index, value ->
            ColumnCartesianLayerModel.Entry(
                x = index,
                y = value
            )
        }
    )

fun maxY(
    currentCountGroup: MutableMap<Int, Int>,
): Float {
    val maxCount = currentCountGroup.values.maxOrNull()
    val max = maxCount?.times(1.2) ?: 0.0
    val maxDivisibleBy5 = (max / 5 + 1) * 5
    return maxDivisibleBy5.toFloat()
}