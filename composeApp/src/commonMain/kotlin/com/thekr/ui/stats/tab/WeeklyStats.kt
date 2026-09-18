package com.thekr.ui.stats.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoZoomState
import com.thekr.ui.counter.CounterHelper
import com.thekr.ui.stats.StatsTimeHelper
import com.thekr.ui.stats.component.axis.startAxis
import com.thekr.ui.stats.component.axis.weeklyBottomAxis
import com.thekr.ui.stats.component.getColumnLayer
import com.thekr.ui.stats.component.getLineLayer
import com.thekr.ui.stats.data.emptyStatisticsData
import com.thekr.util.TimeHelper.calcWeekStart
import com.thekr.values.Dimensions.medium
import com.thekr.values.Dimensions.small
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.tooling.preview.Preview
import java.util.Calendar
import java.util.Locale

@Suppress("ktlint:standard:function-naming")
@Composable
fun WeekStats(
    time: MutableLongState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = medium),
    ) {
        WeekNavigator(time)
        WeeklyChartWrapper(
            time = time,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
fun WeekStatsPreview() {
    WeekStats(
        time = remember { mutableLongStateOf(System.currentTimeMillis()) },
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun WeekNavigator(time: MutableLongState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // previous week
        IconButton(onClick = {
            // DST-correct: shift the Sat–Fri week start by whole calendar days
            // instead of a raw 7×24h step, which drifts across DST transitions.
            time.longValue = StatsTimeHelper.midnightOffsetBy(calcWeekStart(time.longValue), -7)
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }

        // Week boundaries via the app's own Sat–Fri convention (calcWeekStart):
        // Calendar.set(DAY_OF_WEEK, ...) resolves within the locale's calendar
        // week and picks the wrong Saturday/Friday in Sunday-first locales.
        val weekStartDate = calcWeekStart(time.longValue)

        // start day of the week, saturday of the current week
        val startDay = Calendar.getInstance(Locale.getDefault()).apply {
            timeInMillis = weekStartDate
        }[Calendar.DAY_OF_MONTH]

        // end day of the week, friday of the current week
        val endDay = Calendar.getInstance(Locale.getDefault()).apply {
            timeInMillis = weekStartDate
            add(Calendar.DAY_OF_MONTH, 6)
        }[Calendar.DAY_OF_MONTH]

        // monthName of the week: String
        val monthName = Calendar.getInstance(Locale.getDefault()).apply {
            timeInMillis = weekStartDate
        }.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())

        Text(
            text = "$monthName $startDay - $endDay",
            color = MaterialTheme.colorScheme.onBackground,
        )

        var isCurrentWeek by remember {
            mutableStateOf(false)
        }

        // check if time is in the current week, then disable the next button
        LaunchedEffect(key1 = time.longValue) {
            isCurrentWeek = calcWeekStart(System.currentTimeMillis()) == calcWeekStart(time.longValue)
        }

        // next week
        IconButton(
            onClick = {
                time.longValue = StatsTimeHelper.midnightOffsetBy(calcWeekStart(time.longValue), 7)
            },
            enabled = !isCurrentWeek,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = if (isCurrentWeek) {
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun WeeklyChartWrapper(
    time: MutableLongState,
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val weekStatistics = produceState(
        initialValue = emptyStatisticsData(),
        key1 = time.longValue,
    ) {
        // Load on Dispatchers.IO so the blocking DB read never runs on the
        // main thread during composition.
        value = withContext(Dispatchers.IO) {
            CounterHelper.weekStatisticsData(time.longValue)
        }
        modelProducer.runTransaction {
            add(value.partial)
        }
    }

    WeekChart(
        modelProducer = modelProducer,
        maxY = weekStatistics.value.maxY,
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun WeekChart(
    modifier: Modifier = Modifier,
    maxY: Double?,
    modelProducer: CartesianChartModelProducer,
) {
    val scrollState = rememberVicoScrollState()
    val zoomState = rememberVicoZoomState(
        zoomEnabled = false,
    )

    Box(
        modifier = modifier.padding(medium),
    ) {
        CartesianChartHost(
            chart = rememberCartesianChart(
                getLineLayer(),
                getColumnLayer(maxY = maxY),
                startAxis = startAxis,
                bottomAxis = weeklyBottomAxis(),
            ),
            modelProducer = modelProducer,
            scrollState = scrollState,
            zoomState = zoomState,
        )
    }
}
