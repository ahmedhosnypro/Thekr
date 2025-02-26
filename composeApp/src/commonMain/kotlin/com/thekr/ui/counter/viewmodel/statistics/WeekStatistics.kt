package com.thekr.ui.counter.viewmodel.statistics

import com.thekr.model.Count
import com.thekr.stats.CountStatistics
import com.thekr.stats.SeriesType
import com.thekr.stats.WeekStatistics
import com.thekr.stats.maxY
import com.thekr.ui.counter.viewmodel.ZekrCounterViewModel
import com.thekr.util.TimeHelper.calcWeekEnd
import com.thekr.util.TimeHelper.calcWeekStart
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

fun calcWeekStatistics(
    viewModel: ZekrCounterViewModel,
    time: Long
): CountStatistics {
    // find all count items between weekStart and weekend
    var countItems: List<Count>
    val weekStart = calcWeekStart(time)
    val weekEnd = calcWeekEnd(time)

    with(viewModel) {
        runBlocking {
            countItems =
                countRepository.findCounts(zekrId, weekStart, weekEnd).firstOrNull()
                    ?: listOf()
        }
    }

    val countByDay= WeekStatistics.weekModelSeries(countItems)

    // find the max value of the chart
    val maxY = maxY(countByDay)

    return CountStatistics(
        type = SeriesType.COLUMN,
        x = countByDay.keys,
        y = countByDay.values,
        maxY = maxY
    )
}