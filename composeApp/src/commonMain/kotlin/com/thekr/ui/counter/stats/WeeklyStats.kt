//package com.thekr.ui.counter.stats
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.Icon
//import androidx.compose.material.IconButton
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
//import androidx.compose.material.icons.filled.ArrowBackIosNew
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.MutableLongState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableLongStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalLayoutDirection
//import androidx.compose.ui.res.dimensionResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.LayoutDirection
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.thekr.R
//import com.thekr.ui.counter.CounterHelper
//import com.thekr.ui.counter.stats.chart.rememberMarker
//import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
//import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
//import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
//import com.patrykandpatrick.vico.compose.chart.layer.lineSpec
//import com.patrykandpatrick.vico.compose.chart.layer.rememberColumnCartesianLayer
//import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
//import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
//import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
//import com.patrykandpatrick.vico.compose.component.rememberLineComponent
//import com.patrykandpatrick.vico.compose.component.rememberTextComponent
//import com.patrykandpatrick.vico.compose.component.shape.shader.color
//import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
//import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
//import com.patrykandpatrick.vico.core.axis.Axis
//import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
//import com.patrykandpatrick.vico.core.axis.AxisPosition
//import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
//import com.patrykandpatrick.vico.core.chart.layer.LineCartesianLayer
//import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
//import com.patrykandpatrick.vico.core.chart.scale.AutoScaleUp
//import com.patrykandpatrick.vico.core.chart.values.AxisValueOverrider
//import com.patrykandpatrick.vico.core.component.shape.Shapes
//import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
//import com.patrykandpatrick.vico.core.dimensions.MutableDimensions
//import com.patrykandpatrick.vico.core.marker.Marker
//import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
//import kotlinx.coroutines.Dispatchers
//import java.util.Calendar
//import java.util.Locale
//
//@Preview
//@Composable
//fun WeekStats(
//    modifier: Modifier = Modifier,
//    time: MutableLongState = mutableLongStateOf(System.currentTimeMillis()),
//) {
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(top = dimensionResource(id = R.dimen.padding_medium))
//    ) {
//        WeekNavigator(time)
//        WeekChart(
//            time = time
//        )
//    }
//}
//
//@Composable
//private fun WeekNavigator(time: MutableLongState) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(dimensionResource(id = R.dimen.padding_small)),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // previous week
//        IconButton(onClick = {
//            time.longValue -= 604800000
//        }) {
//            Icon(
//                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
//                contentDescription = null,
//                tint = MaterialTheme.colorScheme.onBackground
//            )
//        }
//
//        // start day of the week, saturday of the current week
//        val startDay = Calendar.getInstance(Locale.getDefault()).apply {
//            timeInMillis = time.longValue
//            set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
//        }[Calendar.DAY_OF_MONTH]
//
//        // end day of the week, friday of the current week
//        val endDay = Calendar.getInstance(Locale.getDefault()).apply {
//            timeInMillis = time.longValue
//            set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
//        }[Calendar.DAY_OF_MONTH]
//
//        // monthName of the week: String
//        val monthName = Calendar.getInstance(Locale.getDefault()).apply {
//            timeInMillis = time.longValue
//        }.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
//
//
//        Text(
//            text = "$monthName $startDay - $endDay",
//            color = MaterialTheme.colorScheme.onBackground
//        )
//
//        var isCurrentWeek by remember {
//            mutableStateOf(false)
//        }
//
//        // check if time is in the current week, then disable the next button
//        LaunchedEffect(key1 = time.longValue) {
//            val today = Calendar.getInstance()
//            today.timeInMillis = System.currentTimeMillis()
//            val week = Calendar.getInstance()
//            week.timeInMillis = time.longValue
//            isCurrentWeek = today[Calendar.WEEK_OF_YEAR] == week[Calendar.WEEK_OF_YEAR]
//        }
//
//        // next week
//        IconButton(
//            onClick = {
//                time.longValue += 604800000
//            }, enabled = !isCurrentWeek
//        ) {
//            Icon(
//                imageVector = Icons.Filled.ArrowBackIosNew,
//                contentDescription = null,
//                tint = if (isCurrentWeek) {
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
//private fun WeekChart(
//    modifier: Modifier = Modifier,
//    time: MutableLongState = mutableLongStateOf(System.currentTimeMillis()),
//) {
//    var weekStatistics = CounterHelper.getWeekStatistics(time.longValue)
//
//    var transaction: CartesianChartModelProducer.Transaction.() -> Unit = {
//        add(weekStatistics.currentModel)
//    }
//
//    val dailyModelProducer = CartesianChartModelProducer.build(
//        dispatcher = Dispatchers.Default
//    ) {
//        transaction()
//    }
//
//    LaunchedEffect(key1 = time.longValue) {
//        weekStatistics = CounterHelper.getWeekStatistics(time.longValue)
//        transaction = {
//            add(weekStatistics.currentModel)
//            tryCommit()
//        }
//    }
//
//    Box(
//        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
//    ) {
//        ProvideChartStyle {
//            CartesianChartHost(
//                chart = rememberCartesianChart(
//                    getColumnLayer(maxY = weekStatistics.maxY),
//                    getLineLayer(),
//                    startAxis = startAxis,
//                    bottomAxis = hourlyBottomAxis(),
//                ),
//                modelProducer = dailyModelProducer,
//                isZoomEnabled = false,
//                autoScaleUp = AutoScaleUp.Full,
//                marker = rememberMarker(),
//                horizontalLayout = HorizontalLayout.FullWidth(
//
//                ),
//                chartScrollSpec = rememberChartScrollSpec(
//                    isScrollEnabled = false,
//                ),
//            )
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
//    verticalAxisPosition: AxisPosition.Vertical? = null, maxY: Float? = null
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
//private fun getLineLayer(verticalAxisPosition: AxisPosition.Vertical? = null): LineCartesianLayer {
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
//private val startAxis: Axis<AxisPosition.Vertical.Start>
//    @Composable get() = rememberStartAxis(
//        label = rememberTextComponent(color = MaterialTheme.colorScheme.onBackground),
//        itemPlacer = remember {
//            AxisItemPlacer.Vertical.default(maxItemCount = { 5 })
//        },
//        valueFormatter = { value, _, _ ->
//            value.toInt().toString()
//        },
//    )
//
//@Composable
//private fun hourlyBottomAxis(): HorizontalAxis<AxisPosition.Horizontal.Bottom> {
//    val saturday = stringResource(id = R.string.sat)
//    val sunday = stringResource(id = R.string.sun)
//    val monday = stringResource(id = R.string.mon)
//    val tuesday = stringResource(id = R.string.tue)
//    val wednesday = stringResource(id = R.string.wed)
//    val thursday = stringResource(id = R.string.thu)
//    val friday = stringResource(id = R.string.fri)
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
