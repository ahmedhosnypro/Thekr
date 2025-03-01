package com.thekr.ui.stats.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.Zoom
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoZoomState
import com.thekr.resources.Res
import com.thekr.resources.hour
import com.thekr.resources.minute
import com.thekr.ui.counter.CounterHelper.dayStatisticsData
import com.thekr.ui.stats.DayStatisticsType
import com.thekr.ui.stats.component.axis.hourlyBottomAxis
import com.thekr.ui.stats.component.axis.minuteBottomAxis
import com.thekr.ui.stats.component.axis.startAxis
import com.thekr.ui.stats.component.getColumnLayer
import com.thekr.ui.stats.component.getLineLayer
import com.thekr.values.Dimensions.medium
import com.thekr.values.Dimensions.small
import com.thekr.util.TimeHelper.calcMidnight
import org.jetbrains.compose.resources.stringResource
import java.util.Calendar
import java.util.Locale

@Composable
fun DayStats(
    modifier: Modifier = Modifier,
    midnight: MutableLongState = mutableLongStateOf(calcMidnight()),
    dayStatisticsType: MutableState<DayStatisticsType> = mutableStateOf(DayStatisticsType.Hourly),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = medium)
    ) {
        // Day name and Date
        DayNavigator(midnight)
        // chart
        DailyChartWrapper(midnight, dayStatisticsType)
        // Hourly or Minute stats
        HourMinuteSwitch(dayStatisticsType)
    }
}

@Composable
private fun DailyChartWrapper(
    midnight: MutableLongState,
    dayStatisticsType: MutableState<DayStatisticsType>,
    modifier: Modifier = Modifier,
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val dayStatistics = remember {
        mutableStateOf(dayStatisticsData(midnight.longValue, dayStatisticsType.value))
    }
    LaunchedEffect(
        key1 = midnight.longValue,
        key2 = dayStatisticsType.value
    ) {
        dayStatistics.value = dayStatisticsData(
            midnight.longValue,
            dayStatisticsType.value,
        )

        modelProducer.runTransaction {
            add(dayStatistics.value.partial)
        }
    }
    DayChart(
        modifier, dayStatistics.value.maxY, modelProducer, dayStatisticsType
    )
}

@Composable
fun DayChart(
    modifier: Modifier = Modifier,
    maxY: Double?,
    modelProducer: CartesianChartModelProducer,
    dayStatisticsType: MutableState<DayStatisticsType>,
) {
    Box(
        modifier = modifier.padding(medium)
    ) {
        val scrollState = rememberVicoScrollState()
        val zoomState = rememberVicoZoomState(
            zoomEnabled = true,
            initialZoom = remember { Zoom.min(Zoom.fixed(), Zoom.Content) },
        )
        CartesianChartHost(
            modifier = modifier,
            chart = rememberCartesianChart(
                getLineLayer(),
                getColumnLayer(maxY = maxY),
                startAxis = startAxis,
                bottomAxis = when (dayStatisticsType.value) {
                    DayStatisticsType.Hourly -> hourlyBottomAxis()
                    DayStatisticsType.Minute -> minuteBottomAxis()
                }
            ),
            modelProducer = modelProducer,
            scrollState = scrollState,
            zoomState = zoomState,
        )
    }
}

@Composable
fun DayNavigator(midnight: MutableLongState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // previous day
        IconButton(onClick = {
            midnight.longValue -= 86400000
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = midnight.longValue
        val dayName = calendar.getDisplayName(
            Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()
        )
        val dayNumber = calendar[Calendar.DAY_OF_MONTH]
        val monthName = calendar.getDisplayName(
            Calendar.MONTH, Calendar.LONG, Locale.getDefault()
        )

        Text(
            text = "$dayName - $dayNumber $monthName",
            color = MaterialTheme.colorScheme.onBackground
        )

        var isToday by remember {
            mutableStateOf(false)
        }

        // check if it's today, then disable the next button
        LaunchedEffect(key1 = midnight.longValue) {
            isToday = midnight.longValue == calcMidnight()
        }

        // next day
        IconButton(
            onClick = {
                midnight.longValue += 86400000
            },
            enabled = !isToday
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = if (isToday) {
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.onBackground
                }
            )
        }
    }
}

@Composable
private fun HourMinuteSwitch(dayStatisticsType: MutableState<DayStatisticsType>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        val selectedColor: ButtonColors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
        val unselectedColor: ButtonColors = ButtonDefaults.outlinedButtonColors(
        )
        OutlinedButton(
            onClick = {
                dayStatisticsType.value = DayStatisticsType.Hourly
            },
            shape = MaterialTheme.shapes.medium,
            colors = if (dayStatisticsType.value == DayStatisticsType.Hourly) {
                selectedColor
            } else {
                unselectedColor
            },
            border = ButtonDefaults.outlinedButtonBorder(enabled = dayStatisticsType.value == DayStatisticsType.Hourly)
        ) {
            Text(text = stringResource(Res.string.hour))
        }
        OutlinedButton(
            onClick = {
                dayStatisticsType.value = DayStatisticsType.Minute
            },
            colors = if (dayStatisticsType.value == DayStatisticsType.Minute) {
                selectedColor
            } else {
                unselectedColor
            },
            shape = MaterialTheme.shapes.medium,
            border = ButtonDefaults.outlinedButtonBorder(enabled = dayStatisticsType.value == DayStatisticsType.Minute)
        ) {
            Text(text = stringResource(Res.string.minute))
        }
    }
}

