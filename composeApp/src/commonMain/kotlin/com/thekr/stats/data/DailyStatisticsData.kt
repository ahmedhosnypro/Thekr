package com.thekr.stats.data

import com.thekr.model.Count
import com.thekr.util.TimeHelper.calcMidnight
import java.util.*

object DayStatistics {
    private val dayMinuteStatistics = mutableMapOf<Long, MutableMap<Int, Int>>()

    fun hourDayStatistics(
        todayCountItems: List<Count>,
    ): CountStatistics {
        val todayCountByHour = mutableMapOf<Int, Int>()
        (0..23).forEach { hour ->
            todayCountByHour[hour] = 0
        }

        groupDayCountByHour(todayCountItems, todayCountByHour)

        val maxY = maxY(todayCountByHour)
        return CountStatistics(
            type = SeriesType.COLUMN,
            x = todayCountByHour.keys,
            y = todayCountByHour.values,
            maxY = maxY
        )
    }

    fun minuteDayStatistics(
        currentDayCountItems: List<Count>,
        currentDayMidnight: Long = calcMidnight()
    ): CountStatistics {
        val currentDayCountByMinute = mutableMapOf<Int, Int>()

        (0..24 * 60).forEach { minute ->
            currentDayCountByMinute[minute] = 0
        }
        groupDayCountByMinute(currentDayCountItems, currentDayCountByMinute)
        dayMinuteStatistics[currentDayMidnight] = currentDayCountByMinute

        val maxY = maxY(currentDayCountByMinute)
        return CountStatistics(
            type = SeriesType.LINEAR,
            x = currentDayCountByMinute.keys,
            y = currentDayCountByMinute.values,
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