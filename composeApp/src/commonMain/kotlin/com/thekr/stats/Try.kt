package com.thekr.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.lineSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.multiplatform.common.*
import com.patrykandpatrick.vico.multiplatform.common.component.ShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberLineComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberTextComponent
import com.patrykandpatrick.vico.multiplatform.common.data.ExtraStore
import com.patrykandpatrick.vico.multiplatform.common.shape.CorneredShape
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun JetpackComposeAITestScores(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
) {
    val lineColors = listOf(Color(0xff916cda), Color(0xffd877d8), Color(0xfff094bb))
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    lineColors.map { color ->
                        LineCartesianLayer.rememberLine(
                            fill = LineCartesianLayer.LineFill.single(fill(color)),
                            areaFill = null,
                            pointProvider =
                                LineCartesianLayer.PointProvider.single(
                                    LineCartesianLayer.Point(rememberShapeComponent(fill(color), CorneredShape.Pill))
                                ),
                        )
                    }
                ),

            ),
            rememberColumnCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(),
            marker = rememberMarker(),
        ),
        modelProducer,
        modifier.height(304.dp),
        rememberVicoScrollState(scrollEnabled = false),
    )
}

@Composable
fun PreviewBox(content: @Composable BoxScope.() -> Unit) {
  Box(modifier = Modifier.background(Color.Black).padding(16.dp), content = content)
}

private val data =
    mapOf<String, Map<Int, Number>>(
      "Image recognition" to
        mapOf(
          2009 to -100,
          2012 to -44.16,
          2014 to -6.8,
          2015 to 0.69,
          2016 to 6.62,
          2018 to 11.69,
          2019 to 9.52,
          2020 to 16.45,
        ),
      "Nuanced-language interpretation" to mapOf(2019 to -100, 2021 to 2.73, 2022 to 8.2),
      "Programming" to mapOf(2021 to -100, 2022 to -48.04, 2023 to -12.64),
    )

private val LegendLabelKey = ExtraStore.Key<Set<String>>()

@Composable
fun JetpackComposeAITestScores(modifier: Modifier = Modifier) {
  val modelProducer = remember { CartesianChartModelProducer() }
  LaunchedEffect(Unit) {
    modelProducer.runTransaction {
      // Learn more: https://patrykandpatrick.com/vmml6t.
      lineSeries { data.forEach { (_, map) -> series(map.keys, map.values) } }
      extras { extraStore -> extraStore[LegendLabelKey] = data.keys }
    }
  }
  JetpackComposeAITestScores(modelProducer, modifier)
}

@Composable
@Preview
private fun Preview() {
  val modelProducer = remember { CartesianChartModelProducer() }
  // Use `runBlocking` only for previews, which don’t support asynchronous execution.
  runBlocking {
    modelProducer.runTransaction {
      // Learn more: https://patrykandpatrick.com/vmml6t.
      lineSeries { data.forEach { (_, map) -> series(map.keys, map.values) } }
      extras { extraStore -> extraStore[LegendLabelKey] = data.keys }
    }
  }
  PreviewBox { JetpackComposeAITestScores(modelProducer) }
}