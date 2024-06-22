package com.thekr.ui.settings.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingLabel(
    label: String,
    horizontalPadding: Dp
) {
    Text(
        text = label,
        color = Color.Gray,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        fontFamily = FontFamily.Default,
        modifier = Modifier.padding(horizontal = horizontalPadding)
    )
}