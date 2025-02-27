package com.thekr.stats.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.Axis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianLayerRangeProvider.Companion.fixed
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.common.Fill
import com.patrykandpatrick.vico.multiplatform.common.component.rememberLineComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.shape.CorneredShape

@Composable
fun getColumnLayer(
    verticalAxisPosition: Axis.Position.Vertical? = null, maxY: Double? = null
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
fun getLineLayer(verticalAxisPosition: Axis.Position.Vertical? = null) =
    rememberLineCartesianLayer(
        LineCartesianLayer.LineProvider.series(
            LineCartesianLayer.rememberLine(
                fill = LineCartesianLayer.LineFill.single(
                    Fill(MaterialTheme.colorScheme.primary),
                ),
                stroke = LineCartesianLayer.LineStroke.Continuous(
                    thickness = 0.5.dp,
                    cap = StrokeCap.Round
                ),
            ),
        ),
        verticalAxisPosition = verticalAxisPosition,
    )