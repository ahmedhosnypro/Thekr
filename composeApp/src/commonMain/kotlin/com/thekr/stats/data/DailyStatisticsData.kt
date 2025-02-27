package com.thekr.stats.data

import com.thekr.model.Count
import com.thekr.stats.DayStatisticsType
import com.thekr.ui.counter.viewmodel.ZekrCounterViewModel
import com.thekr.util.TimeHelper.calcMidnight
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.*

object DailyStatisticsData {
    private val dayMinuteStatistics = mutableMapOf<Long, MutableMap<Int, Int>>()

    fun calcDayStatistics(
        viewModel: ZekrCounterViewModel, midnight: Long, dayStatisticsType: DayStatisticsType
    ): StatisticsData {
        val nextMidnight = midnight + 24 * 60 * 60 * 1000
        val todayCountItems: List<Count>

        with(viewModel) {
            runBlocking {
                todayCountItems =
                    countRepository.findCounts(zekrId, midnight, nextMidnight).first()
            }
        }

        return when (dayStatisticsType) {
            DayStatisticsType.Hourly -> hourDayStatistics(todayCountItems)
            DayStatisticsType.Minute -> minuteDayStatistics(
                todayCountItems, currentDayMidnight = midnight
            )
        }
    }

    private fun hourDayStatistics(
        todayCountItems: List<Count>,
    ): StatisticsData {
        val todayCountByHour = mutableMapOf<Int, Int>()
        (0..23).forEach { hour ->
            todayCountByHour[hour] = 0
        }

        groupDayCountByHour(todayCountItems, todayCountByHour)

        val maxY = maxY(todayCountByHour)
        return StatisticsData(
            partial = columnPartial(todayCountByHour),
            maxY = maxY
        )
    }

     fun minuteDayStatistics(
        currentDayCountItems: List<Count>,
        currentDayMidnight: Long = calcMidnight()
    ): StatisticsData {
        val currentDayCountByMinute = mutableMapOf<Int, Int>()

        (0..24 * 60).forEach { minute ->
            currentDayCountByMinute[minute] = 0
        }
        groupDayCountByMinute(currentDayCountItems, currentDayCountByMinute)
        dayMinuteStatistics[currentDayMidnight] = currentDayCountByMinute

        val maxY = maxY(currentDayCountByMinute)
        return StatisticsData(
            partial = linePartial(currentDayCountByMinute),
            maxY = maxY
        )
    }

     private fun groupDayCountByHour(
        todayCountItems: List<Count>,
        todayCountByHour: MutableMap<Int, Int>
    ) {
        val calendar = Calendar.getInstance()
        todayCountItems.forEach { countItem ->
            calendar.timeInMillis = countItem.timeCreated
            val hour = calendar[Calendar.HOUR_OF_DAY]
            if (todayCountByHour.containsKey(hour)) {
                todayCountByHour[hour] = todayCountByHour.getOrDefault(hour, 0) + 1
            }
        }
    }

     private fun groupDayCountByMinute(
        todayCountItems: List<Count>,
        todayCountByMinute: MutableMap<Int, Int>
    ) {
        val calendar = Calendar.getInstance()
        todayCountItems.forEach { countItem ->
            calendar.timeInMillis = countItem.timeCreated
            val hour = calendar[Calendar.HOUR_OF_DAY]
            val minuteOfHour = calendar[Calendar.MINUTE]
            val minute = hour * 60 + minuteOfHour
            if (todayCountByMinute.containsKey(minute)) {
                todayCountByMinute[minute] = todayCountByMinute.getOrDefault(minute, 0) + 1
            }
        }
    }
}