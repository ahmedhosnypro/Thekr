package com.thekr.ui.counter.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.Axis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.rememberAxisTickComponent
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianLayerRangeProvider.Companion.fixed
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.multiplatform.common.Fill
import com.patrykandpatrick.vico.multiplatform.common.Insets
import com.patrykandpatrick.vico.multiplatform.common.component.TextComponent.MinWidth
import com.patrykandpatrick.vico.multiplatform.common.component.rememberLineComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberTextComponent
import com.patrykandpatrick.vico.multiplatform.common.shape.CorneredShape
import com.thekr.resources.Res
import com.thekr.resources.am
import com.thekr.resources.minute
import com.thekr.resources.pm
import com.thekr.stats.CountStatistics
import com.thekr.stats.addStatistics
import com.thekr.ui.counter.CounterHelper.dayStatistics
import com.thekr.ui.values.Dimensions.medium
import com.thekr.ui.values.Dimensions.small
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
        StatisticsProvider(midnight, dayStatisticsType)
        // Hourly or Minute stats
        HourMinuteSwitch(dayStatisticsType)
    }
}

@Composable
private fun StatisticsProvider(
    midnight: MutableLongState,
    dayStatisticsType: MutableState<DayStatisticsType>,
    modifier: Modifier = Modifier,
) {
    var dayStatistics: CountStatistics = remember {
        dayStatistics(
            midnight.longValue,
            dayStatisticsType.value,
        )
    }
    LaunchedEffect(key1 = midnight.longValue) {
        dayStatistics = dayStatistics(
            midnight.longValue,
            dayStatisticsType.value,
        )
    }

    ModelProducerProvider(modifier, dayStatistics, dayStatisticsType)
}


@Composable
fun ModelProducerProvider(
    modifier: Modifier = Modifier,
    dayStatistics: CountStatistics,
    dayStatisticsType: MutableState<DayStatisticsType>,
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(key1 = Unit) {
        modelProducer.runTransaction {
            addStatistics(dayStatistics)
        }
    }

    DayChart(
        modifier, dayStatistics.maxY, modelProducer, dayStatisticsType
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
            zoomEnabled = false
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
private fun DayNavigator(midnight: MutableLongState) {
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
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
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
                imageVector = Icons.Filled.ArrowBackIosNew,
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
            Text(text = stringResource(Res.string.minute))
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

@Composable
private fun getColumnLayer(
    verticalAxisPosition: Axis.Position.Vertical? = null,
    maxY: Double? = null
) = rememberColumnCartesianLayer(
    columnProvider = ColumnCartesianLayer.ColumnProvider.series(
        listOf(
            rememberLineComponent(
                fill = Fill(MaterialTheme.colorScheme.primary),
                thickness = 4.dp,
                shape = CorneredShape.Pill
            ),
        )
    ),
    verticalAxisPosition = verticalAxisPosition,
    rangeProvider = fixed(
        maxY = maxY,
    )
)

@Composable
private fun getLineLayer(verticalAxisPosition: Axis.Position.Vertical? = null) =
    rememberLineCartesianLayer(
        LineCartesianLayer.LineProvider.series(
            LineCartesianLayer.rememberLine(
                fill = LineCartesianLayer.LineFill.single(
                    Fill(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                ),
                pointProvider =
                LineCartesianLayer.PointProvider.single(
                    LineCartesianLayer.Point(
                        rememberShapeComponent(
                            Fill(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)),
                            CorneredShape.Pill
                        )
                    )
                ),
            ),
        ),
        verticalAxisPosition = verticalAxisPosition,
    )

private val startAxis: Axis<Axis.Position.Vertical.Start>
    @Composable get() = VerticalAxis.rememberStart(
        line = rememberAxisLineComponent(),
        label = rememberTextComponent(
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 10.sp,
            ),
            margins = Insets(
                start = 0.dp,
                end = 8.dp,
                top = 0.dp,
                bottom = 0.dp,
            ),
        ),
        itemPlacer = remember {
            VerticalAxis.ItemPlacer.count(
                count = { _ -> 5 },
                shiftTopLines = false,
            )
        },
        valueFormatter = remember { intFormatter() },
        tickLength = 1.dp,
    )


@Composable
private fun hourlyBottomAxis(): HorizontalAxis<Axis.Position.Horizontal.Bottom> {
    val am = stringResource(Res.string.am)
    val pm = stringResource(Res.string.pm)

    val layoutDirection = LocalLayoutDirection.current
    return HorizontalAxis.rememberBottom(
        labelRotationDegrees = when (layoutDirection) {
            LayoutDirection.Ltr -> 90f
            LayoutDirection.Rtl -> -90f
        },

        label = rememberTextComponent(
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 10.sp,
                textAlign = TextAlign.Start
            ),
            margins = Insets(
                start = 0.dp,
                end = 0.dp,
                top = 8.dp,
                bottom = 0.dp,
            ),
            minWidth = MinWidth.text("24 $am"),
            overflow = TextOverflow.Visible
        ),

        itemPlacer = remember {
            HorizontalAxis.ItemPlacer.aligned(
                shiftExtremeLines = false,
                addExtremeLabelPadding = true,
                offset = { _ -> 0 }
            )
        },
        guideline = null,
        valueFormatter = remember { hourFormatter(am, pm) },
        tickLength = 1.dp,
    )
}

@Composable
fun minuteBottomAxis(): HorizontalAxis<Axis.Position.Horizontal.Bottom> {
    val am = stringResource(Res.string.am)
    val pm = stringResource(Res.string.pm)

    val layoutDirection = LocalLayoutDirection.current
    return HorizontalAxis.rememberBottom(
        label = rememberTextComponent(
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            ),
            lineCount = 8,
            margins = Insets(
                start = 0.dp,
                end = 0.dp,
                top = 8.dp,
                bottom = 0.dp,
            ),
        ),
        itemPlacer = remember {
            HorizontalAxis.ItemPlacer.aligned(
                shiftExtremeLines = false,
                addExtremeLabelPadding = true,
                offset = { _ -> 4 }
            )
        },
        labelRotationDegrees = when (layoutDirection) {
            LayoutDirection.Ltr -> 90f
            LayoutDirection.Rtl -> -90f
        },
        guideline = null,
        valueFormatter = remember { minuteFormatter(am, pm) },
        tickLength = 8.dp,
    )
}