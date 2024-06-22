package com.thekr.data.zekr.zekr

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * UI state for the Zekr entry screen.
 */
@Stable
@Immutable
data class ZekrEntryUiState(
    val zekrEntry: ZekrEntry = ZekrEntry(),
    val themeInitialized: Boolean = false,
    val isLabelValid: Boolean = true,
    val isDailyGoalValid: Boolean = true,
    val isWeeklyGoalValid: Boolean = true,
    val isMonthlyGoalValid: Boolean = true,
    val isYearlyGoalValid: Boolean = true,
    val isEntryValid: Boolean = true,
    val saved: Boolean = false
)