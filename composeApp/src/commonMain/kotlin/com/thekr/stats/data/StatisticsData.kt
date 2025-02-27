package com.thekr.stats.data

import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianLayerModel

import com.patrykandpatrick.vico.multiplatform.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.multiplatform.cartesian.data.LineCartesianLayerModel

data class StatisticsData(
    val partial: CartesianLayerModel.Partial,
    val maxY: Double? = null,
    val minY: Double? = null,
)

fun linePartial(data: MutableMap<Int, Int>) =
    LineCartesianLayerModel.Partial(
        listOf(
            data.values.mapIndexed { index, value ->
                LineCartesianLayerModel.Entry(
                    x = index,
                    y = value
                )
            }
        )
    )


fun columnPartial(data: MutableMap<Int, Int>) =
    ColumnCartesianLayerModel.Partial(
        listOf(
            data.values.mapIndexed { index, value ->
                ColumnCartesianLayerModel.Entry(
                    x = index,
                    y = value
                )
            }
        )
    )

fun maxY(
    currentCountGroup: MutableMap<Int, Int>,
): Double {
    val maxCount = currentCountGroup.values.maxOrNull()
    val max = maxCount?.times(1.2) ?: 0.0
    val maxDivisibleBy5 = (max / 5 + 1) * 5
    return maxDivisibleBy5
}