//package com.thekr.ui.counter.stats
//
//import android.text.Layout
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
//import androidx.compose.material.icons.filled.ArrowBackIosNew
//import androidx.compose.material3.ButtonColors
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Shapes
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.MutableLongState
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableLongStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalLayoutDirection
//import androidx.compose.ui.res.dimensionResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.LayoutDirection
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
//import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
//import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
//import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
//import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
//import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
//import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
//import com.patrykandpatrick.vico.core.cartesian.HorizontalLayout
//import com.patrykandpatrick.vico.core.cartesian.axis.Axis
//import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
//import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition
//import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
//import com.patrykandpatrick.vico.core.cartesian.data.AxisValueOverrider
//import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
//import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
//import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
//import com.thekr.ui.counter.stats.chart.rememberMarker
//import com.thekr.ui.counter.CounterHelper.getDayStatistics
//import com.thekr.ui.dimen.Dimension.medium
//import com.thekr.util.TimeHelper.calcMidnight
//import org.jetbrains.compose.resources.stringResource
//import org.slf4j.Marker
//import thekr.composeapp.generated.resources.Res
//import thekr.composeapp.generated.resources.am
//import thekr.composeapp.generated.resources.minute
//import thekr.composeapp.generated.resources.pm
//import java.util.Calendar
//import java.util.Locale
//
//
//@Preview
//@Composable
//fun DayStats(
//    modifier: Modifier = Modifier,
//    midnight: MutableLongState = mutableLongStateOf(calcMidnight()),
//    dayStatisticsType: MutableState<DayStatisticsType> = mutableStateOf(DayStatisticsType.Hourly),
//) {
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(top = medium)
//    ) {
//        // Day name and Date
//        DayNavigator(midnight)
//        // chart
//        DayChart(midnight, dayStatisticsType)
//        // Hourly or Minute stats
//        HourMinuteSwitch(dayStatisticsType)
//    }
//}
//
//@Composable
//private fun DayChart(
//    midnight: MutableLongState,
//    dayStatisticsType: MutableState<DayStatisticsType>,
//    modifier: Modifier = Modifier,
//) {
//    Box(
//        modifier = modifier.padding(medium)
//    ) {
//        var dayStatistics = getDayStatistics(
//            midnight.longValue,
//            dayStatisticsType.value,
//        )
//
//        var transaction: CartesianChartModelProducer.Transaction.() -> Unit = {
//            add(dayStatistics.currentModel)
//        }
//
//        val dailyModelProducer = CartesianChartModelProducer.build {
//            transaction()
//        }
//
//        LaunchedEffect(key1 = midnight.longValue) {
//            dayStatistics = getDayStatistics(
//                midnight.longValue,
//                dayStatisticsType.value,
//            )
//            transaction = {
//                add(dayStatistics.currentModel)
//                tryCommit()
//            }
//        }
////        ProvideChartStyle {
//        CartesianChartHost(
//            chart = rememberCartesianChart(
//                getColumnLayer(maxY = dayStatistics.maxY),
//                getLineLayer(),
//                startAxis = startAxis,
//                bottomAxis = when (dayStatisticsType.value) {
//                    DayStatisticsType.Hourly -> hourlyBottomAxis()
//                    DayStatisticsType.Minute -> minuteBottomAxis()
//                }
//            ),
//            modelProducer = dailyModelProducer,
//            marker = rememberMarker(),
//            horizontalLayout = HorizontalLayout.Segmented,
//        )
////        }
//    }
//}
//
//@Composable
//private fun DayNavigator(midnight: MutableLongState) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(small),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // previous day
//        IconButton(onClick = {
//            midnight.longValue -= 86400000
//        }) {
//            Icon(
//                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
//                contentDescription = null,
//                tint = MaterialTheme.colorScheme.onBackground
//            )
//        }
//
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = midnight.longValue
//        val dayName = calendar.getDisplayName(
//            Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()
//        )
//        val dayNumber = calendar[Calendar.DAY_OF_MONTH]
//        val monthName = calendar.getDisplayName(
//            Calendar.MONTH, Calendar.LONG, Locale.getDefault()
//        )
//
//        Text(
//            text = "$dayName - $dayNumber $monthName",
//            color = MaterialTheme.colorScheme.onBackground
//        )
//
//        var isToday by remember {
//            mutableStateOf(false)
//        }
//
//        // check if it's today, then disable the next button
//        LaunchedEffect(key1 = midnight.longValue) {
//            isToday = midnight.longValue == calcMidnight()
//        }
//
//        // next day
//        IconButton(
//            onClick = {
//                midnight.longValue += 86400000
//            },
//            enabled = !isToday
//        ) {
//            Icon(
//                imageVector = Icons.Filled.ArrowBackIosNew,
//                contentDescription = null,
//                tint = if (isToday) {
//                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
//                } else {
//                    MaterialTheme.colorScheme.onBackground
//                }
//            )
//        }
//    }
//}
//
//@Composable
//private fun HourMinuteSwitch(dayStatisticsType: MutableState<DayStatisticsType>) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceEvenly,
//    ) {
//        val selectedColor: ButtonColors = ButtonDefaults.outlinedButtonColors(
//            containerColor = MaterialTheme.colorScheme.surfaceVariant
//        )
//        val unselectedColor: ButtonColors = ButtonDefaults.outlinedButtonColors(
//        )
//        OutlinedButton(
//            onClick = {
//                dayStatisticsType.value = DayStatisticsType.Hourly
//            },
//            shape = MaterialTheme.shapes.medium,
//            colors = if (dayStatisticsType.value == DayStatisticsType.Hourly) {
//                selectedColor
//            } else {
//                unselectedColor
//            },
//            border = if (dayStatisticsType.value == DayStatisticsType.Hourly) {
//                null
//            } else {
//                ButtonDefaults.outlinedButtonBorder
//            },
//
//            ) {
//            Text(text = stringResource(Res.string.hour))
//        }
//        OutlinedButton(
//            onClick = {
//                dayStatisticsType.value = DayStatisticsType.Minute
//            },
//            colors = if (dayStatisticsType.value == DayStatisticsType.Minute) {
//                selectedColor
//            } else {
//                unselectedColor
//            },
//            shape = MaterialTheme.shapes.medium,
//            border = if (dayStatisticsType.value == DayStatisticsType.Minute) {
//                null
//            } else {
//                ButtonDefaults.outlinedButtonBorder
//            },
//        ) {
//            Text(text = stringResource(Res.string.minute))
//        }
//    }
//}
//
//
//private val markerMap: Map<Float, Marker>
//    @Composable get() = mapOf(4f to rememberMarker())
//
//@Composable
//private fun getColumnLayer(
//    verticalAxisPosition: AxisPosition.Vertical? = null,
//    maxY: Float? = null
//) =
//    rememberColumnCartesianLayer(
//        columnProvider = ColumnCartesianLayer.ColumnProvider.series(
//            listOf(
//                rememberLineComponent(
//                    color = MaterialTheme.colorScheme.primary,
//                    thickness = 8.dp,
//                    // todo: shape
////                    shape = Shapes.pillShape,
//                ),
//            )
//        ),
//
//        verticalAxisPosition = verticalAxisPosition,
//        // AxisValueOverrider<ColumnCartesianLayerModel>?
//        axisValueOverrider = AxisValueOverrider.fixed(
//            maxY = maxY,
//        )
//    )
//
//@Composable
//private fun getLineLayer(verticalAxisPosition: AxisPosition.Vertical? = null): LineCartesianLayer {
//    val colors = MaterialTheme.colorScheme
//    return rememberLineCartesianLayer(
//        lines =
//        listOf(
//            lineSpec(
//                shader = DynamicShaders.color(colors.onBackground.copy(alpha = 0.5f)),
//                backgroundShader =
//                verticalGradient(
//                    arrayOf(Color.DarkGray, Color.DarkGray.copy(alpha = 0f)),
//                ),
//            ),
//        ),
//        verticalAxisPosition = verticalAxisPosition,
//    )
//}
//
//private val startAxis: Axis<AxisPosition.Vertical.Start>
//    @Composable get() =
//        rememberStartAxis(
//            label = rememberTextComponent(color = MaterialTheme.colorScheme.onBackground),
//            itemPlacer = remember {
////                AxisItemPlacer.Vertical.default(
////                    maxItemCount = { 5 }
////                )
//            },
//            valueFormatter = { value, _, _ ->
//                value.toInt().toString()
//            },
//        )
//
//@Composable
//private fun hourlyBottomAxis(): HorizontalAxis<AxisPosition.Horizontal.Bottom> {
//    val am = stringResource(Res.string.am)
//    val pm = stringResource(Res.string.pm)
//
//    return rememberBottomAxis(
//        label = rememberTextComponent(
//            color = MaterialTheme.colorScheme.onBackground,
//            lineCount = 8,
//            textAlignment = Layout.Alignment.ALIGN_CENTER,
//            textSize = 10.sp,
//        ),
//        itemPlacer = remember {
//            AxisItemPlacer.Horizontal.default(
//                shiftExtremeTicks = false,
//                addExtremeLabelPadding = true,
//                offset = 4
//            )
//        },
//        guideline = null,
//        valueFormatter = { value, _, _ ->
//            // hours from 0 to 23
//            // map them to start from 12 am, 4 am, 8 am, 12 pm, 4 pm, 8 pm, 12 am
//            when (value.toInt()) {
//                0 -> "12\n$am"
//                4 -> "4\n$am"
//                8 -> "8\n$am"
//                12 -> "12\n$pm"
//                16 -> "4\n$pm"
//                20 -> "8\n$pm"
//                23 -> "12\n$am"
//                else -> ""
//            }
//        },
//        tickLength = 1.dp,
//    )
//}
//
//@Composable
//fun minuteBottomAxis(): HorizontalAxis<AxisPosition.Horizontal.Bottom> {
//    val am = stringResource(id = R.string.am)
//    val pm = stringResource(id = R.string.pm)
//
//    val layoutDirection = LocalLayoutDirection.current
//    return rememberBottomAxis(
//        label = rememberTextComponent(
//            color = MaterialTheme.colorScheme.onBackground,
//            lineCount = 8,
//            textAlignment = Layout.Alignment.ALIGN_CENTER,
//            textSize = 10.sp,
//            margins = MutableDimensions(
//                startDp = 0f,
//                endDp = 0f,
//                topDp = 8f,
//                bottomDp = 0f,
//            ),
//        ),
//        itemPlacer = remember {
//            AxisItemPlacer.Horizontal.default(
//                shiftExtremeTicks = false,
//                addExtremeLabelPadding = true,
//                offset = 4
//            )
//        },
//        labelRotationDegrees = when (layoutDirection) {
//            LayoutDirection.Ltr -> 90f
//            LayoutDirection.Rtl -> -90f
//        },
//        guideline = null,
//        valueFormatter = { value, _, _ ->
//            // hours from 0 to 23
//            // map them to start from 12 am, 4 am, 8 am, 12 pm, 4 pm, 8 pm, 12 am
//            when (value.toInt()) {
//                30 -> "12 $am"
//                240 -> "4 $am"
//                480 -> "8 $am"
//                720 -> "12 $pm"
//                960 -> "4 $pm"
//                1200 -> "8 $pm"
//                1410 -> "12 $am"
//                else -> ""
//            }
//        },
//        tickLength = 0.dp,
//        sizeConstraint = Axis.SizeConstraint.TextWidth(
//            text = "ًWednesday",
//        )
//    )
//}
