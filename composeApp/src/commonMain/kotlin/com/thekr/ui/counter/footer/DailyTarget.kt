package com.thekr.ui.counter.footer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.data.zekr.instance.ZekrInstanceDetails
import com.thekr.model.ZekrCategoryType
import com.thekr.ui.theme.ZekrTheme

@Composable
fun DailyTarget(
    zekrInstanceDetails: MutableState<ZekrInstanceDetails>,
    settingsDetails: SettingsDetails,
    category: MutableState<CategoryDetails>,
    modifier: Modifier = Modifier,
) {
    val targetName = if (category.value.id == ZekrCategoryType.User.id ||
        category.value.parent == ZekrCategoryType.User.id
    ) "الهدف اليومي:" else "عدد مرات الذكر:"
    val color = ZekrTheme.colors(settingsDetails).primary
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$targetName ${zekrInstanceDetails.value.dailyTarget}",
            fontSize = 16.sp,
            color = color,
            modifier = modifier
        )
    }
}