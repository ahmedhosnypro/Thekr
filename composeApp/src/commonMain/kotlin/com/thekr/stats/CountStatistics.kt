package com.thekr.stats

import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.columnSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.data.lineSeries

enum class SeriesType {
    LINEAR,
    COLUMN
}

data class CountStatistics(
    val type: SeriesType,
    val x: Collection<Number>,
    val y: Collection<Number>,
    val maxY: Double? = null,
    val minY: Double? = null,
)


fun  CartesianChartModelProducer.Transaction.addStatistics(statistics:  CountStatistics) {
    when (statistics.type) {
        SeriesType.LINEAR -> {
            lineSeries {
                series(
                    x = statistics.x,
                    y = statistics.y,
                )
            }
        }
        SeriesType.COLUMN -> {
            columnSeries {
                series(
                    x = statistics.x,
                    y = statistics.y,
                )
            }
        }
    }
}