package com.thekr.stats.axis

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.Axis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.common.Insets
import com.patrykandpatrick.vico.multiplatform.common.component.rememberTextComponent

val startAxis: Axis<Axis.Position.Vertical.Start>
    @Composable get() = VerticalAxis.rememberStart(
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

