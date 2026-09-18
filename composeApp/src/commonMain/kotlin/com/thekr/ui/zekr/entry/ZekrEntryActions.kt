package com.thekr.ui.thekr.entry

import com.thekr.data.thekr.thekr.ThekrEntry
import com.thekr.ui.viewmodel.AppStateHolder.appState

/**
 * Actions for the Thekr entry screen, holding references to the entry
 * ViewModel for the lifetime of the entry composition: [ZekrEntryScreen]
 * registers them on entry and [clearActions] on dispose, so a stale
 * invocation (e.g. save fired after the screen is gone) hits the guarded
 * no-op instead of a dead ViewModel.
 */
object ThekrEntryActions {
    private var initialized = false

    var onSaveClick: () -> Unit = {}
    var onLabelChange: (String) -> Unit = {}
    var onCoolDownChange: (String) -> Unit = {}
    var onDailyGoalChange: (String) -> Unit = {}
    var onWeeklyGoalChange: (String) -> Unit = {}
    var onMonthlyGoalChange: (String) -> Unit = {}
    var onYearlyGoalChange: (String) -> Unit = {}
    var updateThekrEntry: (ThekrEntry) -> Unit = {}

    /**
     * True only while the entry screen's composition is alive and the
     * registered actions are backed by a live ViewModel.
     */
    val isInitialized: Boolean
        get() = initialized

    fun initActions(viewModel: ThekrEntryViewModel) {
        initialized = true
        onSaveClick = {
            appState.currentViewedSebhaCategory?.value?.let { viewModel.saveItem(it) }
        }
        onLabelChange = viewModel::updateLabel
        onCoolDownChange = viewModel::updateCoolDown
        onDailyGoalChange = viewModel::updateDailyTarget
        onWeeklyGoalChange = viewModel::updateWeeklyGoal
        onMonthlyGoalChange = viewModel::updateMonthlyGoal
        onYearlyGoalChange = viewModel::updateYearlyGoal
        updateThekrEntry = viewModel::updateThekrEntry
    }

    fun clearActions() {
        initialized = false
        onSaveClick = {}
        onLabelChange = {}
        onCoolDownChange = {}
        onDailyGoalChange = {}
        onWeeklyGoalChange = {}
        onMonthlyGoalChange = {}
        onYearlyGoalChange = {}
        updateThekrEntry = {}
    }
}
