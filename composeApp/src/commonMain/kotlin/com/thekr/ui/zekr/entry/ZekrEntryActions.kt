package com.thekr.ui.zekr.entry

import com.thekr.data.zekr.zekr.ZekrEntry
import com.thekr.ui.viewmodel.AzkarStateHelper.azkarState

object ZekrEntryActions {
    var onSaveClick: () -> Unit = {}
    var onLabelChange: (String) -> Unit = {}
    var onCoolDownChange: (String) -> Unit = {}
    var onDailyGoalChange: (String) -> Unit = {}
    var onWeeklyGoalChange: (String) -> Unit = {}
    var onMonthlyGoalChange: (String) -> Unit = {}
    var onYearlyGoalChange: (String) -> Unit = {}
    var updateZekrEntry: (ZekrEntry) -> Unit = {}
    
    fun initActions(viewModel: ZekrEntryViewModel){
        onSaveClick = {
            viewModel.saveItem(azkarState.currentViewedSebhaCategory?.value!!)
        }
        onLabelChange = viewModel::updateLabel
        onCoolDownChange = viewModel::updateCoolDown
        onDailyGoalChange = viewModel::updateDailyTarget
        onWeeklyGoalChange = viewModel::updateWeeklyGoal
        onMonthlyGoalChange = viewModel::updateMonthlyGoal
        onYearlyGoalChange = viewModel::updateYearlyGoal
        updateZekrEntry = viewModel::updateZekrEntry
    }
}