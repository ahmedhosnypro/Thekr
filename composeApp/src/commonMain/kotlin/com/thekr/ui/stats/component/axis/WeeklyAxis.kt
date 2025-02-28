package com.thekr.ui.stats.component.axis

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
import com.thekr.resources.fri
import com.thekr.resources.mon
import com.thekr.resources.sat
import com.thekr.resources.sun
import com.thekr.resources.thu
import com.thekr.resources.tue
import com.thekr.resources.wed
import org.jetbrains.compose.resources.stringResource

@Composable
fun weeklyBottomAxis(): HorizontalAxis<Axis.Position.Horizontal.Bottom> {
    val saturday = stringResource(Res.string.sat)
    val sunday = stringResource(Res.string.sun)
    val monday = stringResource(Res.string.mon)
    val tuesday = stringResource(Res.string.tue)
    val wednesday = stringResource(Res.string.wed)
    val thursday = stringResource(Res.string.thu)
    val friday = stringResource(Res.string.fri)

    // detect rtl
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
            minWidth = MinWidth.text("Wednesday"),
        ),
        size = Size.Text("Wednesday"),
        itemPlacer = remember {
            HorizontalAxis.ItemPlacer.aligned(
                shiftExtremeLines = false,
                addExtremeLabelPadding = true,
                offset = { _ -> 0 },
            )
        },
        guideline = null,
        valueFormatter =  remember {
            weekdayFormatter(
                saturday = saturday,
                sunday = sunday,
                monday = monday,
                tuesday = tuesday,
                wednesday = wednesday,
                thursday = thursday,
                friday = friday
            )
        },
        tickLength = 0.dp,
    )
}
