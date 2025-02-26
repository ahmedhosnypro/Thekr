package com.thekr.ui.counter.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
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
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoZoomState
import com.thekr.ui.counter.CounterHelper
import com.thekr.ui.values.Dimensions.medium
import com.thekr.ui.values.Dimensions.small
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.util.Calendar
import java.util.Locale
import com.thekr.stats.CountStatistics
import com.thekr.stats.JetpackComposeAITestScores
import com.thekr.stats.addStatistics

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
        WeekChart(
            time = time
        )
    }
}

@Composable
private fun WeekNavigator(time: MutableLongState) {
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
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
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
                imageVector = Icons.Filled.ArrowBackIosNew,
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
private fun WeekChart(
    modifier: Modifier = Modifier,
    time: MutableLongState = mutableLongStateOf(System.currentTimeMillis()),
) {
    var weekStatistics: CountStatistics

    val dailyModelProducer = remember {
        CartesianChartModelProducer()
    }

    val scrollState = rememberVicoScrollState()
    val zoomState = rememberVicoZoomState(
        zoomEnabled = false
    )

    LaunchedEffect(key1 = time.longValue) {
        weekStatistics = CounterHelper.weekStatistics(time.longValue)
        dailyModelProducer.runTransaction {
            addStatistics(weekStatistics)
        }
    }

    JetpackComposeAITestScores(
        modelProducer = dailyModelProducer,
        modifier = Modifier.fillMaxWidth()
    )
//    Box(
//        modifier = modifier.padding(small),
//    ) {
//        CartesianChartHost(
//            chart = rememberCartesianChart(
////                getColumnLayer(maxY = weekStatistics.maxY),
////                getLineLayer(),
//                rememberLineCartesianLayer(),
//                startAxis = VerticalAxis.rememberStart(),
//                bottomAxis = HorizontalAxis.rememberBottom(),
////                startAxis = startAxis,
////                bottomAxis = hourlyBottomAxis(),
//            ),
//            modelProducer = dailyModelProducer,
//            scrollState = scrollState,
//            zoomState = zoomState,
//        )
//    }
}

//@Composable
//private fun getColumnLayer(
//    verticalAxisPosition: Axis.Position.Vertical? = null, maxY: Float? = null
//) = rememberColumnCartesianLayer(
//    columns = listOf(
//        rememberLineComponent(
//            color = MaterialTheme.colorScheme.primary,
//            thickness = 16.dp,
//            shape = Shapes.pillShape,
//        ),
//    ), verticalAxisPosition = verticalAxisPosition,
//    // AxisValueOverrider<ColumnCartesianLayerModel>?
//    axisValueOverrider = AxisValueOverrider.fixed(
//        maxY = maxY,
//        maxX = 6f,
//    )
//)
//
//@Composable
//private fun getLineLayer(verticalAxisPosition: Axis.Position.Vertical? = null): LineCartesianLayer {
//    val colors = MaterialTheme.colorScheme
//    return rememberLineCartesianLayer(
//        lines = listOf(
//            lineSpec(
//                shader = DynamicShaders.color(colors.onBackground.copy(alpha = 0.5f)),
//                backgroundShader = verticalGradient(
//                    arrayOf(Color.DarkGray, Color.DarkGray.copy(alpha = 0f)),
//                ),
//            ),
//        ),
//        verticalAxisPosition = verticalAxisPosition,
//    )
//}
//
//private val startAxis: Axis<Axis.Position.Vertical.Start>
//    @Composable get() = rememberStartAxis(
//        label = rememberTextComponent(color = MaterialTheme.colorScheme.onBackground),
//        itemPlacer = remember {
//            AxisItemPlacer.Vertical.default(maxItemCount = { 5 })
//        },
//        valueFormatter = { value, _, _ ->
//            value.toInt().toString()
//        },
//    )

//@Composable
//private fun hourlyBottomAxis(): HorizontalAxis<Axis.Position.Horizontal.Bottom> {
//    val saturday = stringResource(Res.string.sat)
//    val sunday = stringResource(Res.string.sun)
//    val monday = stringResource(Res.string.mon)
//    val tuesday = stringResource(Res.string.tue)
//    val wednesday = stringResource(Res.string.wed)
//    val thursday = stringResource(Res.string.thu)
//    val friday = stringResource(Res.string.fri)
//
//    // detect rtl
//    val layoutDirection = LocalLayoutDirection.current
//
//
//    return rememberBottomAxis(
//        label = rememberTextComponent(
//            color = MaterialTheme.colorScheme.onBackground,
//            lineCount = 10,
//            textSize = 10.sp,
//            margins = MutableDimensions(
//                startDp = 0f,
//                endDp = 0f,
//                topDp = 8f,
//                bottomDp = 0f,
//            ),
//        ),
//        labelRotationDegrees = when (layoutDirection) {
//            LayoutDirection.Ltr -> 90f
//            LayoutDirection.Rtl -> -90f
//        },
//        itemPlacer = remember {
//            AxisItemPlacer.Horizontal.default(
////                addExtremeLabelPadding = true,
//            )
//        },
//        guideline = null,
//        valueFormatter = { value, _, _ ->
//            when (value.toInt()) {
//                0 -> saturday
//                1 -> sunday
//                2 -> monday
//                3 -> tuesday
//                4 -> wednesday
//                5 -> thursday
//                6 -> friday
//                else -> ""
//            }
//        },
//        tickLength = 0.dp,
//        sizeConstraint = Axis.SizeConstraint.TextWidth(
//            text = "ًWednesday",
//        )
//    )
//}
