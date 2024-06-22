package com.thekr.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.thekr.data.count.miss.CountMissDetails
import com.thekr.data.zekr.count.ZekrCount
import com.thekr.data.zekr.fadl.FadlDetails
import com.thekr.data.zekr.instance.ZekrInstanceDetails
import com.thekr.data.zekr.zekr.ZekrDetails
import kotlinx.coroutines.flow.update

/**
 * Updates the current viewed Sebha category in the AzkarViewModel's state.
 *
 * @param tabIndex The index of the selected Sebha tab.
 */
fun AzkarViewModel.updateCurrentSebhaViewedCategory(tabIndex: Int) {
    mutableAzkarState.update { currentState ->
        currentState.copy(
            currentViewedSebhaCategory = currentState.userAzkar.value.childCategories.getOrNull(tabIndex)
        )
    }
}

/**
 * Updates a SnapshotStateList of ZekrDetails MutableStates with a new list of ZekrDetails.
 * This function ensures that only the changed items are updated in the list,
 * preventing unnecessary recomposition of unchanged items.
 *
 * @param toUpdateZekrList The SnapshotStateList to be updated.
 * @param updatedZekrList The new list of ZekrDetails.
 */
fun updateZekrList(
    toUpdateZekrList: SnapshotStateList<MutableState<ZekrDetails>>,
    updatedZekrList: List<ZekrDetails>,
) {
    val existingZekrIds = toUpdateZekrList.map { it.value.id }

    // Remove items that are no longer in the updated list
    toUpdateZekrList.removeIf { it.value.id !in updatedZekrList.map { zekr -> zekr.id } }

    // Update existing Zekr items if they have been updated in the database
    updatedZekrList.forEach { newZekr ->
        val index = existingZekrIds.indexOf(newZekr.id)
        if (index != -1 && toUpdateZekrList[index].value.timeUpdated < newZekr.timeUpdated) {
            // Update the MutableState directly
            toUpdateZekrList[index].value = newZekr
        }
    }

    // Add new Zekr items
    updatedZekrList.filter { it.id !in existingZekrIds }
        .forEach { toUpdateZekrList.add(mutableStateOf(it)) }
}

/**
 * Updates a SnapshotStateList of ZekrInstanceDetails MutableStates with a new list.
 * Ensures only changed items are updated, minimizing recomposition.
 *
 * @param toUpdateZekrInstanceList The list to be updated.
 * @param updatedZekrInstanceList The new list of ZekrInstanceDetails.
 */
fun updateZekrInstanceList(
    toUpdateZekrInstanceList: SnapshotStateList<MutableState<ZekrInstanceDetails>>,
    updatedZekrInstanceList: List<ZekrInstanceDetails>,
) {
    val existingZekrInstanceIds = toUpdateZekrInstanceList.map { it.value.id }

    // Remove items that are no longer in the updated list
    toUpdateZekrInstanceList.removeIf { it.value.id !in updatedZekrInstanceList.map { it.id } }

    // Update existing ZekrInstanceDetails items
    updatedZekrInstanceList.forEach { newZekrInstance ->
        val index = existingZekrInstanceIds.indexOf(newZekrInstance.id)
        if (index != -1 && toUpdateZekrInstanceList[index].value.timeUpdated < newZekrInstance.timeUpdated) {
            // Update the MutableState directly
            toUpdateZekrInstanceList[index].value = newZekrInstance
        }
    }

    // Add new ZekrInstanceDetails items
    updatedZekrInstanceList.filter { it.id !in existingZekrInstanceIds }
        .forEach { toUpdateZekrInstanceList.add(mutableStateOf(it)) }
}

/**
 * Updates a ZekrCount item in a SnapshotStateList.
 * If the item exists and has an older timestamp, it's updated.
 * Otherwise, the item is added to the list.
 *
 * @param toUpdateZekrCountList The list to be updated.
 * @param updatedZekrCountItem The updated ZekrCount item.
 */
fun updateZekrCountItem(
    toUpdateZekrCountList: SnapshotStateList<MutableState<ZekrCount>>,
    updatedZekrCountItem: MutableState<ZekrCount>,
) {
    val existingItemIndex = toUpdateZekrCountList.indexOfFirst {
        it.value.zekrInstanceId == updatedZekrCountItem.value.zekrInstanceId
    }

    if (existingItemIndex != -1) {
        val existingItem = toUpdateZekrCountList[existingItemIndex]
        if (existingItem.value.timeUpdated < updatedZekrCountItem.value.timeUpdated) {
            // Update the existing MutableState directly
            existingItem.value = updatedZekrCountItem.value
        }
    } else {
        // Add the new item if it doesn't exist
        toUpdateZekrCountList.add(updatedZekrCountItem)
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