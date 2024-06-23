package com.thekr.ui.counter.footer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.ui.values.Dimensions.large
import com.thekr.ui.theme.ZekrTheme
import org.jetbrains.compose.resources.painterResource
import com.thekr.resources.Res
import com.thekr.resources.next_zekr

@Composable
fun CurrentZekrIndicator(
    settingsDetails: SettingsDetails,
    categoryDetails: MutableState<CategoryDetails>,
    tabIndex: Int,
    modifier: Modifier = Modifier,
) {
    if (categoryDetails.value.zekrList.size > 1) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(large),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val fontSize = 16.sp
                val lineHeightDp = with(LocalDensity.current) {
                    fontSize.toDp()
                }
                val colors = ZekrTheme.colors(settingsDetails)
                Image(
                    painter = painterResource(Res.drawable.next_zekr),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(lineHeightDp)
                        .graphicsLayer(scaleX = -1f),
                    colorFilter = ColorFilter.tint(
                        if (tabIndex == 0) colors.disabledZekrIndicator
                        else colors.zekrIndicator
                    )
                )
                Text(
                    text = "${tabIndex + 1} / ${categoryDetails.value.zekrList.size}",
                    fontSize = fontSize,
                )

                Image(
                    painter = painterResource(Res.drawable.next_zekr),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(lineHeightDp),
                    colorFilter = ColorFilter.tint(
                        if (tabIndex == categoryDetails.value.zekrList.size - 1) colors.disabledZekrIndicator
                        else colors.zekrIndicator
                    )
                )
            }
        }
    }
}