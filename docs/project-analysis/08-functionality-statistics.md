# Thekr - Statistics & Goal Tracking

## Statistics System

### Overview

The app provides detailed statistics visualization using the Vico charting library:

```
┌─────────────────────────────────────────────────────────────┐
│                   STATISTICS VIEWS                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │    Daily    │  │   Weekly    │  │   Monthly   │         │
│  │   Stats     │  │   Stats     │  │   Stats     │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
│        │                │                │                  │
│        ▼                ▼                ▼                  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │  By Hour    │  │  By Day of  │  │  By Week    │         │
│  │  By Minute  │  │    Week     │  │  of Month   │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Daily Statistics

Two visualization modes:

#### 1. Hourly View (Column Chart)
Groups counts by hour of day (0-23):

```kotlin
private fun hourDayStatistics(todayCountItems: List<Count>): StatisticsData {
    val todayCountByHour = mutableMapOf<Int, Int>()
    
    // Initialize all hours to 0
    (0..23).forEach { hour ->
        todayCountByHour[hour] = 0
    }
    
    // Group counts by hour
    todayCountItems.forEach { countItem ->
        calendar.timeInMillis = countItem.timeCreated
        val hour = calendar[Calendar.HOUR_OF_DAY]
        todayCountByHour[hour] = todayCountByHour.getOrDefault(hour, 0) + 1
    }
    
    return StatisticsData(
        partial = columnPartial(todayCountByHour),
        maxY = maxY(todayCountByHour)
    )
}
```

#### 2. Minute View (Line Chart)
Groups counts by minute of day (0-1440):

```kotlin
fun minuteDayStatistics(
    currentDayCountItems: List<Count>,
    currentDayMidnight: Long
): StatisticsData {
    val currentDayCountByMinute = mutableMapOf<Int, Int>()
    
    // Initialize all minutes to 0
    (0..24 * 60).forEach { minute ->
        currentDayCountByMinute[minute] = 0
    }
    
    // Group counts by minute
    currentDayCountItems.forEach { countItem ->
        calendar.timeInMillis = countItem.timeCreated
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minuteOfHour = calendar[Calendar.MINUTE]
        val minute = hour * 60 + minuteOfHour
        currentDayCountByMinute[minute] = currentDayCountByMinute.getOrDefault(minute, 0) + 1
    }
    
    return StatisticsData(
        partial = linePartial(currentDayCountByMinute),
        maxY = maxY(currentDayCountByMinute)
    )
}
```

### Weekly Statistics

Groups counts by day of week (Saturday-Friday, following Islamic week):

```kotlin
fun calcWeekStatistics(viewModel: ThekrCounterViewModel, time: Long): StatisticsData {
    val weekStart = calcWeekStart(time)
    val weekEnd = calcWeekEnd(time)
    
    // Query counts within week range
    val countItems = countRepository.findCounts(thekrId, weekStart, weekEnd)
    
    // Map to day of week (0=Saturday, 6=Friday)
    val countByDay = mutableMapOf<Int, Int>()
    (0..6).forEach { day -> countByDay[day] = 0 }
    
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
        countByDay[day] = countByDay.getOrDefault(day, 0) + 1
    }
    
    return StatisticsData(
        partial = columnPartial(countByDay),
        maxY = maxY(countByDay)
    )
}
```

### Statistics Data Structure

```kotlin
data class StatisticsData(
    val partial: CartesianLayerModel.Partial,  // Chart data
    val maxY: Double? = null,                   // Y-axis max for scaling
    val minY: Double? = null,                   // Y-axis min
)
```

## Goal Tracking System

### Goal Types

Each ThekrInstance can have four independent goals:

| Goal Type | Reset Period | Status Options |
|-----------|--------------|----------------|
| Daily     | Midnight     | Enabled/Disabled/Impossible |
| Weekly    | Saturday     | Enabled/Disabled/Impossible |
| Monthly   | 1st of month | Enabled/Disabled/Impossible |
| Yearly    | Jan 1st      | Enabled/Disabled/Impossible |

### Goal Status Enum

```kotlin
enum class ThekrTargetStatus {
    Enabled,     // Goal is active and tracked
    Disabled,    // Goal is not tracked
    Impossible   // Goal cannot be achieved (e.g., past deadline)
}
```

### Progress Calculation

```kotlin
// In ZekrCount.kt
fun progressState(target: Long, count: Long): ProgressState {
    return when {
        target == 0L -> ProgressState.TargetIsZero
        count == 0L -> ProgressState.ScoreIsZero
        count == target -> ProgressState.EQUAL
        count > target -> ProgressState.BIGGER
        else -> ProgressState.SMALLER
    }
}
```

### Visual Progress Indicators

#### Circular Progress (Main Counter)
```kotlin
CircularProgressIndicator(
    progress = { progressAnimate.value },  // count / target
    color = progressColor,                  // Green when complete
    strokeWidth = 8.dp,
)
```

#### Linear Progress (Detailed View)
```kotlin
LinearProgressIndicator(
    progress = { progressAnimate.value },
    color = progressColor,
    trackColor = Color.Transparent,
)
```

### Progress Colors

```kotlin
fun progressColor(colors: AppColors, progressState: ProgressState): Color {
    return when (progressState) {
        ProgressState.EQUAL, 
        ProgressState.BIGGER -> colors.successColor    // Green - goal met!
        ProgressState.SMALLER -> colors.progressColor  // Blue - in progress
        else -> MaterialTheme.colorScheme.onSurface    // Default
    }
}
```

### Auto-Advance on Goal Completion

When daily target is reached, automatically scroll to next thekr:

```kotlin
// In Count.kt
if (count.dailyCount + 1 == thekrInstance.dailyTarget) {
    CounterHelper.scrollToNextThekr()
}
```

## Count Display Options

Users can customize which counts are visible:

```kotlin
// In SettingsDetails
val showCount: Boolean = true,        // Master toggle
val showDailyCount: Boolean = true,
val showWeeklyCount: Boolean = true,
val showMonthlyCount: Boolean = true,
val showYearlyCount: Boolean = true,
val showTotalCount: Boolean = true,
val showSessionCount: Boolean = true,
```

### Detailed Count View

Shows all enabled count types with progress bars:

```
┌─────────────────────────────────────────────┐
│  Daily    ████████████░░░░░░░░  50 / 100   │
│  Weekly   ██████░░░░░░░░░░░░░░  150 / 500  │
│  Monthly  ████░░░░░░░░░░░░░░░░  300 / 2000 │
│  Yearly   █░░░░░░░░░░░░░░░░░░░  1200/10000 │
│  ─────────────────────────────────────────  │
│  Total                           12,450     │
└─────────────────────────────────────────────┘
```

---
*Analysis continues in subsequent files...*
