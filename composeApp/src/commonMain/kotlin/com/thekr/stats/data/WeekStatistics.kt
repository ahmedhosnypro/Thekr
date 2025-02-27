package com.thekr.stats.series

import com.thekr.model.Count
import java.util.Calendar

object WeekStatistics {
    fun weekModelSeries(
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
