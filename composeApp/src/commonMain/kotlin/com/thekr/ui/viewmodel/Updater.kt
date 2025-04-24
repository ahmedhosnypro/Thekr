package com.thekr.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.thekr.data.count.miss.CountMissDetails
import com.thekr.data.thekr.count.ThekrCount
import com.thekr.data.thekr.fadl.FadlDetails
import com.thekr.data.thekr.instance.ThekrInstanceDetails
import com.thekr.data.thekr.thekr.ThekrDetails
import kotlinx.coroutines.flow.update

/**
 * Updates the current viewed Sebha category in the AzkarViewModel's state.
 *
 * @param tabIndex The index of the selected Sebha tab.
 */
fun AppViewModel.updateCurrentSebhaViewedCategory(tabIndex: Int) {
    mutableAppState.update { currentState ->
        currentState.copy(
            currentViewedSebhaCategory = currentState.userThekr.value.childCategories.getOrNull(tabIndex)
        )
    }
}

/**
 * Updates a SnapshotStateList of ThekrDetails MutableStates with a new list of ThekrDetails.
 * This function ensures that only the changed items are updated in the list,
 * preventing unnecessary recomposition of unchanged items.
 *
 * @param toUpdateThekrList The SnapshotStateList to be updated.
 * @param updatedThekrList The new list of ThekrDetails.
 */
fun updateThekrList(
    toUpdateThekrList: SnapshotStateList<MutableState<ThekrDetails>>,
    updatedThekrList: List<ThekrDetails>,
) {
    val existingThekrIds = toUpdateThekrList.map { it.value.id }

    // Remove items that are no longer in the updated list
    toUpdateThekrList.removeIf { it.value.id !in updatedThekrList.map { thekr -> thekr.id } }

    // Update existing Thekr items if they have been updated in the database
    updatedThekrList.forEach { newThekr ->
        println("newThekr: ${newThekr.id} - ${newThekr.timeUpdated}")
        val index = existingThekrIds.indexOf(newThekr.id)
        if (index != -1 && toUpdateThekrList[index].value.timeUpdated < newThekr.timeUpdated) {
            // Update the MutableState directly
            toUpdateThekrList[index].value = newThekr
        }
    }

    // Add new Thekr items
    updatedThekrList.filter { it.id !in existingThekrIds }
        .forEach { toUpdateThekrList.add(mutableStateOf(it)) }
}

/**
 * Updates a SnapshotStateList of ThekrInstanceDetails MutableStates with a new list.
 * Ensures only changed items are updated, minimizing recomposition.
 *
 * @param toUpdateThekrInstanceList The list to be updated.
 * @param updatedThekrInstanceList The new list of ThekrInstanceDetails.
 */
fun updateThekrInstanceList(
    toUpdateThekrInstanceList: SnapshotStateList<MutableState<ThekrInstanceDetails>>,
    updatedThekrInstanceList: List<ThekrInstanceDetails>,
) {
    val existingThekrInstanceIds = toUpdateThekrInstanceList.map { it.value.id }

    // Remove items that are no longer in the updated list
    toUpdateThekrInstanceList.removeIf { it.value.id !in updatedThekrInstanceList.map { it.id } }

    // Update existing ThekrInstanceDetails items
    updatedThekrInstanceList.forEach { newThekrInstance ->
        println("newThekrInstance: ${newThekrInstance.id} - ${newThekrInstance.timeUpdated}")
        val index = existingThekrInstanceIds.indexOf(newThekrInstance.id)
        if (index != -1 && toUpdateThekrInstanceList[index].value.timeUpdated < newThekrInstance.timeUpdated) {
            // Update the MutableState directly
            toUpdateThekrInstanceList[index].value = newThekrInstance
        }
    }

    // Add new ThekrInstanceDetails items
    updatedThekrInstanceList.filter { it.id !in existingThekrInstanceIds }
        .forEach { toUpdateThekrInstanceList.add(mutableStateOf(it)) }
}

/**
 * Updates a ThekrCount item in a SnapshotStateList.
 * If the item exists and has an older timestamp, it's updated.
 * Otherwise, the item is added to the list.
 *
 * @param toUpdateThekrCountList The list to be updated.
 * @param updatedThekrCountItem The updated ThekrCount item.
 */
fun updateThekrCountItem(
    toUpdateThekrCountList: SnapshotStateList<MutableState<ThekrCount>>,
    updatedThekrCountItem: MutableState<ThekrCount>,
) {
    val existingItemIndex = toUpdateThekrCountList.indexOfFirst {
        it.value.thekrInstanceId == updatedThekrCountItem.value.thekrInstanceId
    }

    if (existingItemIndex != -1) {
        val existingItem = toUpdateThekrCountList[existingItemIndex]
        if (existingItem.value.timeUpdated < updatedThekrCountItem.value.timeUpdated) {
            // Update the existing MutableState directly
            existingItem.value = updatedThekrCountItem.value
        }
    } else {
        // Add the new item if it doesn't exist
        toUpdateThekrCountList.add(updatedThekrCountItem)
    }
}

/**
 * Updates a SnapshotStateList of CountMissDetails with a new list.
 * Only adds new items that are not already present in the list.
 *
 * @param toUpdateCountMissList The list to be updated.
 * @param updatedCountMissList The new list of CountMissDetails.
 */
fun updateCountMissList(
    toUpdateCountMissList: SnapshotStateList<CountMissDetails>,
    updatedCountMissList: List<CountMissDetails>
) {
    updatedCountMissList.filter { it.id !in toUpdateCountMissList.map { countMiss -> countMiss.id } }
        .forEach { toUpdateCountMissList.add(it) }
}

/**
 * Updates a SnapshotStateList of FadlDetails with a new list.
 * Ensures only changed items are updated, minimizing recomposition.
 *
 * @param toUpdateFadlList The list to be updated.
 * @param updatedFadlList The new list of FadlDetails.
 */
fun updateFadlList(
    toUpdateFadlList: SnapshotStateList<FadlDetails>,
    updatedFadlList: List<FadlDetails>,
) {
    val existingFadlIds = toUpdateFadlList.map { it.id }

    // Remove items that are no longer in the updated list
    toUpdateFadlList.removeIf { it.id !in updatedFadlList.map { fadl -> fadl.id } }

    // Update existing FadlDetails items
    updatedFadlList.forEach { newFadl ->
        val index = existingFadlIds.indexOf(newFadl.id)
        if (index != -1 && toUpdateFadlList[index].timeUpdated < newFadl.timeUpdated) {
            // Update the item in the list directly
            toUpdateFadlList[index] = newFadl
        }
    }

    // Add new FadlDetails items
    updatedFadlList.filter { it.id !in existingFadlIds }
        .forEach { toUpdateFadlList.add(it) }
}