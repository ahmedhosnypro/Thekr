package com.thekr.preview.stats

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.columnSeries
import com.thekr.data.proto.ThemeMode
import com.thekr.ui.component.LocalizedApp
import com.thekr.stats.tab.DayChart
import com.thekr.stats.DayStatisticsType
import com.thekr.ui.theme.AppTheme
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    maxY = 5000.0,
                    modelProducer = modelProducer,
                    dayStatisticsType = dayStatisticsType
                )
            }
        }
    }
}