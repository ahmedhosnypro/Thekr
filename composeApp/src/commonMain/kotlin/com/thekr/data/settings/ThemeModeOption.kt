package com.thekr.data.settings

import com.thekr.data.proto.ThemeMode

data class ThemeModeOption(
    val text: String,
    val description: String? = null,
    val value: ThemeMode,
    val selected: Boolean = false
)