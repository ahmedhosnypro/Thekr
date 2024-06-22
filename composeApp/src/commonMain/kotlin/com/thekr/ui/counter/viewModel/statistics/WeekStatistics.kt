//package com.thekr.ui.counter.viewModel.statistics
//
//import com.thekr.model.Count
//import com.thekr.service.stats.WeekStatistics
//import com.thekr.service.stats.maxY
//import com.thekr.ui.counter.viewModel.CountStatistics
//import com.thekr.ui.counter.viewModel.ZekrCounterViewModel
//import com.patrykandpatrick.vico.core.model.ColumnCartesianLayerModel
//import com.thekr.util.calcWeekEnd
//import com.thekr.util.calcWeekStart
//import kotlinx.coroutines.flow.firstOrNull
//import kotlinx.coroutines.runBlocking
//
//fun getWeekStatistics(
//    viewModel: ZekrCounterViewModel,
//    time: Long
//): CountStatistics {
//    // find all count items between weekStart and weekEnd
//    var countItems: List<Count>
//    val weekStart = calcWeekStart(time)
//    val weekEnd = calcWeekEnd(time)
//
//    with(viewModel) {
//        runBlocking {
//            countItems =
//                countRepository.findCounts(zekrId, weekStart, weekEnd).firstOrNull()
//                    ?: listOf()
//        }
//    }
//
//    val (countByDay, weekSeries) = WeekStatistics.weekModelSeries(countItems)
//
//    // find the max value of the chart
//    val maxY = maxY(countByDay)
//
//    return CountStatistics(
//        currentModel = ColumnCartesianLayerModel.Partial(
//            series = weekSeries
//        ), maxY = maxY
//    )
//}