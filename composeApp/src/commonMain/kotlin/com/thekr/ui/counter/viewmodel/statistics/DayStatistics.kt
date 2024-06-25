//package com.thekr.ui.counter.viewModel.statistics
//
//import com.thekr.model.Count
//import com.thekr.service.stats.DayStatistics
//import com.thekr.ui.counter.stats.DayStatisticsType
//import com.thekr.ui.counter.viewModel.CountStatistics
//import com.thekr.ui.counter.viewModel.ZekrCounterViewModel
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.runBlocking
//
//fun getDayStatistics(
//    viewModel: ZekrCounterViewModel, midnight: Long, dayStatisticsType: DayStatisticsType
//): CountStatistics {
//    val nextMidnight = midnight + 24 * 60 * 60 * 1000
//    val todayCountItems: List<Count>
//
//
//    with(viewModel) {
//        runBlocking {
//            todayCountItems =
//                countRepository.findCounts(zekrId, midnight, nextMidnight).first()
//        }
//    }
//
//
//
//    return when (dayStatisticsType) {
//        DayStatisticsType.Hourly -> DayStatistics.hourDayStatistics(todayCountItems)
//        DayStatisticsType.Minute -> DayStatistics.minuteDayStatistics(
//            todayCountItems, currentDayMidnight = midnight
//        )
//    }
//}