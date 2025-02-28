package com.thekr.ui.thekr.entry

import com.thekr.data.thekr.thekr.ThekrEntry
import com.thekr.ui.viewmodel.AppStateHolder.appState

object ThekrEntryActions {
    var onSaveClick: () -> Unit = {}
    var onLabelChange: (String) -> Unit = {}
    var onCoolDownChange: (String) -> Unit = {}
    var onDailyGoalChange: (String) -> Unit = {}
    var onWeeklyGoalChange: (String) -> Unit = {}
    var onMonthlyGoalChange: (String) -> Unit = {}
    var onYearlyGoalChange: (String) -> Unit = {}
    var updateThekrEntry: (ThekrEntry) -> Unit = {}
    
    fun initActions(viewModel: ThekrEntryViewModel){
        onSaveClick = {
            viewModel.saveItem(appState.currentViewedSebhaCategory?.value!!)
        }
        onLabelChange = viewModel::updateLabel
        onCoolDownChange = viewModel::updateCoolDown
        onDailyGoalChange = viewModel::updateDailyTarget
        onWeeklyGoalChange = viewModel::updateWeeklyGoal
        onMonthlyGoalChange = viewModel::updateMonthlyGoal
        onYearlyGoalChange = viewModel::updateYearlyGoal
        updateThekrEntry = viewModel::updateThekrEntry
    }
}