package com.thekr.stats.data

import com.thekr.model.Count
import com.thekr.ui.counter.viewmodel.ZekrCounterViewModel
import com.thekr.util.TimeHelper.calcWeekEnd
import com.thekr.util.TimeHelper.calcWeekStart
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import java.util.Calendar

object WeeklyStatisticsData {

    fun calcWeekStatistics(
        viewModel: ZekrCounterViewModel,
        time: Long
    ): StatisticsData {
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

        val countByDay= weekModelSeries(countItems)

        return StatisticsData(
            partial = columnPartial(countByDay),
            maxY = maxY(countByDay)
        )
    }

    private fun weekModelSeries(
        countItems: List<Count>,
    ): MutableMap<Int, Int> {
        val calendar: Calendar = Calendar.getInstance()
        // create a map of day to count items
        val countItemsByDay = mutableMapOf<Int, MutableList<Count>>()
        countItems.forEach { countItem ->
            calendar.timeInMillis = countItem.timeCreated
            val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]
            val day = when (dayOfWeek) {
                Calendar.SATURDAY -> 0
                Calendar.SUNDAY -> 1
                Calendar.MONDAY -> 2
                Calendar.TUESDAY -> 3
                Calendar.WEDNESDAY -> 4
                Calendar.THURSDAY -> 5
                Calendar.FRIDAY -> 6
                else -> 0
            }
            if (countItemsByDay.containsKey(day)) {
                countItemsByDay[day]?.add(countItem)
            } else {
                countItemsByDay[day] = mutableListOf(countItem)
            }
        }

        // create a map of day to count
        val countByDay = mutableMapOf<Int, Int>()
        // push day of the week to the map
        (0..6).forEach { day ->
            countByDay[day] = 0
        }
        // push the count of each day to the map
        countItemsByDay.forEach { (day, countItems) ->
            countByDay[day] = countItems.size
        }

        return countByDay
    }
}
