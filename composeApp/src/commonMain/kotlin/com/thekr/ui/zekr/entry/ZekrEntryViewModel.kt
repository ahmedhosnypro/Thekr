package com.thekr.ui.thekr.entry

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.data.thekr.count.ThekrCount
import com.thekr.data.thekr.instance.ThekrInstanceRepository
import com.thekr.data.thekr.thekr.ThekrEntry
import com.thekr.data.thekr.thekr.ThekrEntryUiState
import com.thekr.data.thekr.thekr.ThekrRepository
import com.thekr.ui.navigation.NavigationActions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 * ViewModel for the Thekr entry screen.
 * Handles saving new Thekrs and managing the UI state.
 */
class ThekrEntryViewModel(
    private val thekrRepository: ThekrRepository,
    private val thekrInstanceRepository: ThekrInstanceRepository,
) : ViewModel() {

    private val _viewState = MutableStateFlow(ThekrEntryUiState())
    val viewState = _viewState.asStateFlow()

    /**
     * Saves the current Thekr entry.
     *
     * @param parentCategory The category to which the Thekr belongs.
     */
    fun saveItem(parentCategory: CategoryDetails) {
        if (!viewState.value.saved && validateInput(viewState.value)) {
            _viewState.update { it.copy(saved = true) }
            viewModelScope.launch {
                saveThekrAndInstance(parentCategory)
            }
            NavigationActions.navigateUp()
        }
    }

    /**
     * Saves the Thekr and ThekrInstance entities to the database.
     *
     * @param parentCategory The category to which the Thekr belongs.
     */
    private suspend fun saveThekrAndInstance(parentCategory: CategoryDetails) {
        val thekr = viewState.value.thekrEntry.toThekr().copy(categoryId = parentCategory.id)
        runBlocking(Dispatchers.IO) {
            val insertedThekrId = thekrRepository.insert(thekr)

            val thekrInstance = viewState.value.thekrEntry.toThekrInstance()
                .copy(thekrId = insertedThekrId, categoryId = parentCategory.id)
            val thekrInstanceId = thekrInstanceRepository.insert(thekrInstance)

            parentCategory.countList.add(
                mutableStateOf(
                    ThekrCount(
                        thekrInstanceId = thekrInstanceId,
                        categoryId = parentCategory.id,
                        timeUpdated = System.currentTimeMillis(),
                    )
                )
            )
        }
    }

    /**
     * Updates the label of the Thekr entry.
     *
     * @param label The new label value.
     */
    fun updateLabel(label: String) {
        val isLabelValid = label.isNotBlank()
        val isEntryValid = validateInput(
            viewState.value.copy(thekrEntry = viewState.value.thekrEntry.copy(text = label))
        )

        _viewState.update { currentState ->
            currentState.copy(
                thekrEntry = currentState.thekrEntry.copy(text = label),
                isLabelValid = isLabelValid,
                isEntryValid = isEntryValid
            )
        }
    }

    /**
     * Updates the cool-down time for the Thekr entry.
     *
     * @param coolDown The new cool down value as a string.
     */
    fun updateCoolDown(coolDown: String) {
        val updatedCoolDown = if (coolDown.isBlank()) 400L else coolDown.toLongOrNull()
        val isEntryValid = validateInput(
            viewState.value.copy(thekrEntry = viewState.value.thekrEntry.copy(coolDown = updatedCoolDown ?: 0))
        )

        _viewState.update { currentState ->
            currentState.copy(
                thekrEntry = currentState.thekrEntry.copy(coolDown = updatedCoolDown ?: 400),
                isEntryValid = isEntryValid
            )
        }
    }

    /**
     * Updates a target value for the Thekr entry and validates input.
     *
     * @param targetValue The new target value as a string.
     * @param updateStateLambda A lambda function to update the specific target value in the state.
     */
    private fun updateTargetValue(
        targetValue: String,
        updateStateLambda: (ThekrEntry, Long) -> ThekrEntry
    ) {
        val updatedTarget = if (targetValue.isBlank()) 0L else targetValue.toLongOrNull()
        val isEntryValid = validateInput(
            viewState.value.copy(thekrEntry = updateStateLambda(viewState.value.thekrEntry, updatedTarget ?: 0))
        )
        _viewState.update { currentState ->
            currentState.copy(
                thekrEntry = updateStateLambda(currentState.thekrEntry, updatedTarget ?: 0),
                isEntryValid = isEntryValid
            )
        }
    }

    /**
     * Updates the daily target for the Thekr entry.
     *
     * @param dailyTarget The new daily target value as a string.
     */
    fun updateDailyTarget(dailyTarget: String) = updateTargetValue(dailyTarget) { thekrEntry, target ->
        thekrEntry.copy(dailyTarget = target)
    }

    /**
     * Updates the weekly target for the Thekr entry.
     *
     * @param weeklyTarget The new weekly target value as a string.
     */
    fun updateWeeklyGoal(weeklyTarget: String) = updateTargetValue(weeklyTarget) { thekrEntry, target ->
        thekrEntry.copy(weeklyTarget = target)
    }

    /**
     * Updates the monthly target for the Thekr entry.
     *
     * @param monthlyTarget The new monthly target value as a string.
     */
    fun updateMonthlyGoal(monthlyTarget: String) = updateTargetValue(monthlyTarget) { thekrEntry, target ->
        thekrEntry.copy(monthlyTarget = target)
    }

    /**
     * Updates the yearly target for the Thekr entry.
     *
     * @param yearlyTarget The new yearly target value as a string.
     */
    fun updateYearlyGoal(yearlyTarget: String) = updateTargetValue(yearlyTarget) { thekrEntry, target ->
        thekrEntry.copy(yearlyTarget = target)
    }

    /**
     * Updates the entire ThekrEntry object in the UI state.
     *
     * @param thekrEntry The new ThekrEntry object.
     */
    fun updateThekrEntry(thekrEntry: ThekrEntry) {
        _viewState.update { currentState ->
            currentState.copy(thekrEntry = thekrEntry)
        }
    }
}

/**
 * Validates the input of the ThekrEntryUiState.
 *
 * @param thekrEntryUiState The UI state to validate.
 * @return True if the input is valid, false otherwise.
 */
fun validateInput(thekrEntryUiState: ThekrEntryUiState): Boolean {
    return with(thekrEntryUiState) {
        thekrEntry.text.isNotBlank()
    }
}