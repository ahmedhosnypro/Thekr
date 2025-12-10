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
import com.thekr.ui.stats.component.axis.startAxis
import com.thekr.ui.stats.component.axis.weeklyBottomAxis
import com.thekr.ui.stats.component.getColumnLayer
import com.thekr.ui.stats.component.getLineLayer
import com.thekr.values.Dimensions.medium
import com.thekr.values.Dimensions.small
import androidx.compose.ui.tooling.preview.Preview
import java.util.Calendar
import java.util.Locale

@Preview
@Composable
fun WeekStats(
    modifier: Modifier = Modifier,
    time: MutableLongState = mutableLongStateOf(System.currentTimeMillis()),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = medium)
    ) {
        WeekNavigator(time)
        WeeklyChartWrapper(
            time = time
        )
    }
}

@Composable
fun WeekNavigator(time: MutableLongState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // previous week
        IconButton(onClick = {
            time.longValue -= 604800000
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // start day of the week, saturday of the current week
        val startDay = Calendar.getInstance(Locale.getDefault()).apply {
            timeInMillis = time.longValue
            set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        }[Calendar.DAY_OF_MONTH]

        // end day of the week, friday of the current week
        val endDay = Calendar.getInstance(Locale.getDefault()).apply {
            timeInMillis = time.longValue
            set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
        }[Calendar.DAY_OF_MONTH]

        // monthName of the week: String
        val monthName = Calendar.getInstance(Locale.getDefault()).apply {
            timeInMillis = time.longValue
        }.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())


        Text(
            text = "$monthName $startDay - $endDay",
            color = MaterialTheme.colorScheme.onBackground
        )

        var isCurrentWeek by remember {
            mutableStateOf(false)
        }

        // check if time is in the current week, then disable the next button
        LaunchedEffect(key1 = time.longValue) {
            val today = Calendar.getInstance()
            today.timeInMillis = System.currentTimeMillis()
            val week = Calendar.getInstance()
            week.timeInMillis = time.longValue
            isCurrentWeek = today[Calendar.WEEK_OF_YEAR] == week[Calendar.WEEK_OF_YEAR]
        }

        // next week
        IconButton(
            onClick = {
                time.longValue += 604800000
            }, enabled = !isCurrentWeek
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = if (isCurrentWeek) {
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.onBackground
                }
            )
        }
    }
}

@Composable
private fun WeeklyChartWrapper(
    time: MutableLongState = mutableLongStateOf(System.currentTimeMillis()),
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    var weekStatisticsData = remember {
        mutableStateOf(CounterHelper.weekStatisticsData(time.longValue))
    }

    LaunchedEffect(key1 = time.longValue) {
        weekStatisticsData = mutableStateOf(CounterHelper.weekStatisticsData(time.longValue))
        modelProducer.runTransaction {
            add(weekStatisticsData.value.partial)
        }
    }

    WeekChart(
        modelProducer = modelProducer,
        maxY = weekStatisticsData.value.maxY,
    )
}

@Composable
fun WeekChart(
    modifier: Modifier = Modifier,
    maxY: Double?,
    modelProducer: CartesianChartModelProducer,
) {
    val scrollState = rememberVicoScrollState()
    val zoomState = rememberVicoZoomState(
        zoomEnabled = false
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