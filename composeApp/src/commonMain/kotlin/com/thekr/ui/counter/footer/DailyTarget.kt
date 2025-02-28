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
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.data.thekr.instance.ThekrInstanceDetails
import com.thekr.model.ThekrCategoryType
import com.thekr.ui.theme.AppTheme

@Composable
fun DailyTarget(
    thekrInstanceDetails: MutableState<ThekrInstanceDetails>,
    settingsDetails: SettingsDetails,
    category: MutableState<CategoryDetails>,
    modifier: Modifier = Modifier,
) {
    val targetName = if (category.value.id == ThekrCategoryType.User.id ||
        category.value.parent == ThekrCategoryType.User.id
    ) "الهدف اليومي:" else "عدد مرات الذكر:"
    val color = AppTheme.colors(settingsDetails).primary
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$targetName ${thekrInstanceDetails.value.dailyTarget}",
            fontSize = 16.sp,
            color = color,
            modifier = modifier
        )
    }
}