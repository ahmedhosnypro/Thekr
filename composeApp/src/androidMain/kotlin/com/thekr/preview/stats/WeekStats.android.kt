package com.thekr.preview.stats

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.columnSeries
import com.thekr.data.proto.ThemeMode
import com.thekr.stats.tab.WeekChart
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.theme.AppTheme
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

@Composable
@Preview(locale = "ar", group = "WeekChart")
@Preview(locale = "en", group = "WeekChart")
private fun WeekChartPreview() {
    val x = (0..6).toList()  // 7 days of the week
    val y = List(7) {
        Random.nextInt(0, 50)
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
                WeekChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    maxY = 60.0,
                    modelProducer = modelProducer
                )
            }
        }
    }
}

