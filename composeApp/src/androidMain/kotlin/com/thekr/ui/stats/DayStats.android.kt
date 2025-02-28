package com.thekr.preview.stats

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.columnSeries
import com.thekr.data.proto.ThemeMode
import com.thekr.model.Count
import com.thekr.stats.DayStatisticsType
import com.thekr.stats.data.DailyStatisticsData
import com.thekr.stats.tab.DayChart
import com.thekr.stats.tab.DayNavigator
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.theme.AppTheme
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

@Composable
@Preview(locale = "ar", group = "DayNavigator")
@Preview(locale = "en", group = "DayNavigator")
private fun DayNavigatorPreview() {
    val midnight = remember { mutableLongStateOf(System.currentTimeMillis()) }

    LocalizedApp(
        language = Locale.current.language
    ) {
        AppTheme(ThemeMode.Dark) {
            Surface {
                DayNavigator(midnight = midnight)
            }
        }
    }
}

@Composable
@Preview(locale = "ar", group = "DayChartHourly")
@Preview(locale = "en", group = "DayChartHourly")
private fun DayChartPreview() {
    val x = (0..23).toList()  // 24 hours
    val y = List(24) {
        Random.nextInt(0, 5000)
    }

    val dayStatisticsType = remember {
        mutableStateOf(DayStatisticsType.Hourly)
    }

    val modelProducer = remember { CartesianChartModelProducer() }
    runBlocking {
        modelProducer.runTransaction {
            columnSeries { series(x, y) }
        }
    }

    LocalizedApp(
        language = Locale.current.language
    ) {
        AppTheme(ThemeMode.Dark) {
            Surface {
                DayChart(
                    modifier = Modifier.fillMaxWidth(),
                    maxY = 5000.0,
                    modelProducer = modelProducer,
                    dayStatisticsType = dayStatisticsType
                )
            }
        }
    }
}

@Composable
@Preview(locale = "ar", group = "DayChartMinute")
@Preview(locale = "en", group = "DayChartMinute")
private fun DayChartMinutePreview() {
    // Create sample count data with random timestamps within the day
    val sampleCounts = List(100) { 
        Count(
            timeCreated = System.currentTimeMillis() - Random.nextLong(0, 24 * 60 * 60 * 1000),
            thekrInstanceId = 1,
        )
    }
    
    val dayStatisticsType = remember {
        mutableStateOf(DayStatisticsType.Minute)
    }

    // Use DailyStatisticsData to process the counts
    val statisticsData = DailyStatisticsData.minuteDayStatistics(sampleCounts)
    
    val modelProducer = remember { CartesianChartModelProducer() }
    runBlocking {
        modelProducer.runTransaction {
            add(statisticsData.partial)
        }
    }

    LocalizedApp(
        language = Locale.current.language
    ) {
        AppTheme(ThemeMode.Dark) {
            Surface {
                DayChart(
                    modifier = Modifier.fillMaxWidth(),
                    maxY = statisticsData.maxY,
                    modelProducer = modelProducer,
                    dayStatisticsType = dayStatisticsType
                )
            }
        }
    }
}