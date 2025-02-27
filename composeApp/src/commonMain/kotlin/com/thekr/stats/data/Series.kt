package com.thekr.stats.data

import com.patrykandpatrick.vico.multiplatform.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.multiplatform.cartesian.data.LineCartesianLayerModel

fun lineSeries(groupedCount: MutableMap<Int, Int>) =
        groupedCount.values.mapIndexed { index, value ->
            LineCartesianLayerModel.Entry(
                x = index,
                y = value
            )
        }

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
): Double {
    val maxCount = currentCountGroup.values.maxOrNull()
    val max = maxCount?.times(1.2) ?: 0.0
    val maxDivisibleBy5 = (max / 5 + 1) * 5
    return maxDivisibleBy5
}