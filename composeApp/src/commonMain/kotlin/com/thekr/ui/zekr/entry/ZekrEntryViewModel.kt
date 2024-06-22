package com.thekr.ui.zekr.entry

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.data.zekr.count.ZekrCount
import com.thekr.data.zekr.instance.ZekrInstanceRepository
import com.thekr.data.zekr.zekr.ZekrEntry
import com.thekr.data.zekr.zekr.ZekrEntryUiState
import com.thekr.data.zekr.zekr.ZekrRepository
import com.thekr.ui.navigation.NavigationActions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 * ViewModel for the Zekr entry screen.
 * Handles saving new Zekrs and managing the UI state.
 */
class ZekrEntryViewModel(
    private val zekrRepository: ZekrRepository,
    private val zekrInstanceRepository: ZekrInstanceRepository,
) : ViewModel() {

    private val _viewState = MutableStateFlow(ZekrEntryUiState())
    val viewState = _viewState.asStateFlow()

    /**
     * Saves the current Zekr entry.
     *
     * @param parentCategory The category to which the Zekr belongs.
     */
    fun saveItem(parentCategory: CategoryDetails) {
        if (!viewState.value.saved && validateInput(viewState.value)) {
            _viewState.update { it.copy(saved = true) }
            viewModelScope.launch {
                saveZekrAndInstance(parentCategory)
            }
            NavigationActions.navigateUp()
        }
    }

    /**
     * Saves the Zekr and ZekrInstance entities to the database.
     *
     * @param parentCategory The category to which the Zekr belongs.
     */
    private suspend fun saveZekrAndInstance(parentCategory: CategoryDetails) {
        val zekr = viewState.value.zekrEntry.toZekr().copy(categoryId = parentCategory.id)
        runBlocking(Dispatchers.IO) {
            val insertedZekrId = zekrRepository.insert(zekr)

            val zekrInstance = viewState.value.zekrEntry.toZekrInstance()
                .copy(zekrId = insertedZekrId, categoryId = parentCategory.id)
            val zekrInstanceId = zekrInstanceRepository.insert(zekrInstance)

            parentCategory.countList.add(
                mutableStateOf(
                    ZekrCount(
                        zekrInstanceId = zekrInstanceId,
                        categoryId = parentCategory.id,
                        timeUpdated = System.currentTimeMillis(),
                    )
                )
            )
        }
    }

    /**
     * Updates the label of the Zekr entry.
     *
     * @param label The new label value.
     */
    fun updateLabel(label: String) {
        val isLabelValid = label.isNotBlank()
        val isEntryValid = validateInput(
            viewState.value.copy(zekrEntry = viewState.value.zekrEntry.copy(text = label))
        )

        _viewState.update { currentState ->
            currentState.copy(
                zekrEntry = currentState.zekrEntry.copy(text = label),
                isLabelValid = isLabelValid,
                isEntryValid = isEntryValid
            )
        }
    }

    /**
     * Updates the cool-down time for the Zekr entry.
     *
     * @param coolDown The new cool down value as a string.
     */
    fun updateCoolDown(coolDown: String) {
        val updatedCoolDown = if (coolDown.isBlank()) 400L else coolDown.toLongOrNull()
        val isEntryValid = validateInput(
            viewState.value.copy(zekrEntry = viewState.value.zekrEntry.copy(coolDown = updatedCoolDown ?: 0))
        )

        _viewState.update { currentState ->
            currentState.copy(
                zekrEntry = currentState.zekrEntry.copy(coolDown = updatedCoolDown ?: 400),
                isEntryValid = isEntryValid
            )
        }
    }

    /**
     * Updates a target value for the Zekr entry and validates input.
     *
     * @param targetValue The new target value as a string.
     * @param updateStateLambda A lambda function to update the specific target value in the state.
     */
    private fun updateTargetValue(
        targetValue: String,
        updateStateLambda: (ZekrEntry, Long) -> ZekrEntry
    ) {
        val updatedTarget = if (targetValue.isBlank()) 0L else targetValue.toLongOrNull()
        val isEntryValid = validateInput(
            viewState.value.copy(zekrEntry = updateStateLambda(viewState.value.zekrEntry, updatedTarget ?: 0))
        )
        _viewState.update { currentState ->
            currentState.copy(
                zekrEntry = updateStateLambda(currentState.zekrEntry, updatedTarget ?: 0),
                isEntryValid = isEntryValid
            )
        }
    }

    /**
     * Updates the daily target for the Zekr entry.
     *
     * @param dailyTarget The new daily target value as a string.
     */
    fun updateDailyTarget(dailyTarget: String) = updateTargetValue(dailyTarget) { zekrEntry, target ->
        zekrEntry.copy(dailyTarget = target)
    }

    /**
     * Updates the weekly target for the Zekr entry.
     *
     * @param weeklyTarget The new weekly target value as a string.
     */
    fun updateWeeklyGoal(weeklyTarget: String) = updateTargetValue(weeklyTarget) { zekrEntry, target ->
        zekrEntry.copy(weeklyTarget = target)
    }

    /**
     * Updates the monthly target for the Zekr entry.
     *
     * @param monthlyTarget The new monthly target value as a string.
     */
    fun updateMonthlyGoal(monthlyTarget: String) = updateTargetValue(monthlyTarget) { zekrEntry, target ->
        zekrEntry.copy(monthlyTarget = target)
    }

    /**
     * Updates the yearly target for the Zekr entry.
     *
     * @param yearlyTarget The new yearly target value as a string.
     */
    fun updateYearlyGoal(yearlyTarget: String) = updateTargetValue(yearlyTarget) { zekrEntry, target ->
        zekrEntry.copy(yearlyTarget = target)
    }

    /**
     * Updates the entire ZekrEntry object in the UI state.
     *
     * @param zekrEntry The new ZekrEntry object.
     */
    fun updateZekrEntry(zekrEntry: ZekrEntry) {
        _viewState.update { currentState ->
            currentState.copy(zekrEntry = zekrEntry)
        }
    }
}

/**
 * Validates the input of the ZekrEntryUiState.
 *
 * @param zekrEntryUiState The UI state to validate.
 * @return True if the input is valid, false otherwise.
 */
fun validateInput(zekrEntryUiState: ZekrEntryUiState): Boolean {
    return with(zekrEntryUiState) {
        zekrEntry.text.isNotBlank()
    }
}