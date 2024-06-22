package com.thekr.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A vertical divider with a default height of 1dp and a default color of [MaterialTheme.colorScheme.outline].
 *  Use in Row(Modifier.height(IntrinsicSize.Min),
 * @param modifier The modifier to be applied to the divider.
 * @param color The color of the divider.
 */
@Composable
fun DefaultVerticalDivider(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.outline,
) {
    VerticalDivider(
        modifier = modifier.padding(paddingValues),
        thickness = thickness,
        color = color,
    )
}


@Composable
fun DefaultHorizontalDivider(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.outline,
) {
    HorizontalDivider(
        modifier = modifier
            .padding(paddingValues),
        thickness = thickness,
        color = color
    )
}