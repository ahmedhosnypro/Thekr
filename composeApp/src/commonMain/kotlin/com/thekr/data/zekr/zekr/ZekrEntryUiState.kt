package com.thekr.data.thekr.thekr

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * UI state for the Thekr entry screen.
 */
@Stable
@Immutable
data class ThekrEntryUiState(
    val thekrEntry: ThekrEntry = ThekrEntry(),
    val themeInitialized: Boolean = false,
    val isLabelValid: Boolean = true,
    val isDailyGoalValid: Boolean = true,
    val isWeeklyGoalValid: Boolean = true,
    val isMonthlyGoalValid: Boolean = true,
    val isYearlyGoalValid: Boolean = true,
    val isEntryValid: Boolean = true,
    val saved: Boolean = false
)