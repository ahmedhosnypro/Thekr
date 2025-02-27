package com.thekr.stats.component.axis

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.Axis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.BaseAxis.Size
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.common.Insets
import com.patrykandpatrick.vico.multiplatform.common.component.TextComponent.MinWidth
import com.patrykandpatrick.vico.multiplatform.common.component.rememberTextComponent
import com.thekr.resources.Res
import com.thekr.resources.am
import com.thekr.resources.pm
import org.jetbrains.compose.resources.stringResource

@Composable
fun hourlyBottomAxis(): HorizontalAxis<Axis.Position.Horizontal.Bottom> {
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
                offset = { _ -> 0 },
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
                textAlign = TextAlign.Start
            ),
            lineCount = 8,
            margins = Insets(
                start = 0.dp,
                end = 0.dp,
                top = 8.dp,
                bottom = 0.dp,
            ),
            minWidth = MinWidth.text("12:59 $pm"),
            overflow = TextOverflow.Visible
        ),
        size = Size.Text("12:59 $am "),
        itemPlacer = remember {
            HorizontalAxis.ItemPlacer.aligned(
                shiftExtremeLines = false,
                addExtremeLabelPadding = true,
                offset = { _ -> 0 }
            )
        },
        labelRotationDegrees = when (layoutDirection) {
            LayoutDirection.Ltr -> 90f
            LayoutDirection.Rtl -> -90f
        },
        guideline = null,
        valueFormatter = remember { extendedMinuteFormatter(am, pm) },
        tickLength = 1.dp,
    )
}