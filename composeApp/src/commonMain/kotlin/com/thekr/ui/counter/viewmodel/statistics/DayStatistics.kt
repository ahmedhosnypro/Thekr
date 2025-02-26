package com.thekr.ui.counter.viewmodel.statistics

import com.thekr.model.Count
import com.thekr.stats.CountStatistics
import com.thekr.stats.DayStatistics
import com.thekr.ui.counter.stats.DayStatisticsType
import com.thekr.ui.counter.viewmodel.ZekrCounterViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

fun calcDayStatistics(
    viewModel: ZekrCounterViewModel, midnight: Long, dayStatisticsType: DayStatisticsType
): CountStatistics {
    val nextMidnight = midnight + 24 * 60 * 60 * 1000
    val todayCountItems: List<Count>

    with(viewModel) {
        runBlocking {
            todayCountItems =
                countRepository.findCounts(zekrId, midnight, nextMidnight).first()
        }
    }

    return when (dayStatisticsType) {
        DayStatisticsType.Hourly -> DayStatistics.hourDayStatistics(todayCountItems)
        DayStatisticsType.Minute -> DayStatistics.minuteDayStatistics(
            todayCountItems, currentDayMidnight = midnight
        )
    }
}