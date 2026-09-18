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
 * Doubles as the lazy-fetch gate for Sebha sub-tabs: the newly viewed
 * category's flows open on first display.
 *
 * @param tabIndex The index of the selected Sebha tab.
 */
fun AppViewModel.updateCurrentSebhaViewedCategory(tabIndex: Int) {
    mutableAppState.update { currentState ->
        currentState.copy(
            currentViewedSebhaCategory = currentState.userThekr.value.childCategories.getOrNull(tabIndex),
        )
    }
    mutableAppState.value.currentViewedSebhaCategory?.let { ensureCategoryFetched(it) }
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
    val existingThekrIds = toUpdateThekrList.mapTo(HashSet()) { it.value.id }
    val updatedThekrIds = updatedThekrList.mapTo(HashSet()) { it.id }

    // Remove items that are no longer in the updated list
    toUpdateThekrList.removeIf { it.value.id !in updatedThekrIds }

    val indexById = HashMap<Long, Int>()
    toUpdateThekrList.forEachIndexed { index, item ->
        indexById.putIfAbsent(item.value.id, index)
    }

    // Update existing Thekr items if they have been updated in the database
    updatedThekrList.forEach { newThekr ->
        val index = indexById[newThekr.id]
        if (index != null && toUpdateThekrList[index].value.timeUpdated < newThekr.timeUpdated) {
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
    val existingThekrInstanceIds = toUpdateThekrInstanceList.mapTo(HashSet()) { it.value.id }
    val updatedThekrInstanceIds = updatedThekrInstanceList.mapTo(HashSet()) { it.id }

    // Remove items that are no longer in the updated list
    toUpdateThekrInstanceList.removeIf { it.value.id !in updatedThekrInstanceIds }

    val indexById = HashMap<Long, Int>()
    toUpdateThekrInstanceList.forEachIndexed { index, item ->
        indexById.putIfAbsent(item.value.id, index)
    }

    // Update existing ThekrInstanceDetails items
    updatedThekrInstanceList.forEach { newThekrInstance ->
        val index = indexById[newThekrInstance.id]
        if (index != null && toUpdateThekrInstanceList[index].value.timeUpdated < newThekrInstance.timeUpdated) {
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
 * If the item exists, it is replaced only when the emission's data is
 * fresher than the entry — an entry holding optimistic in-memory count
 * increments newer than the persisted data is never overwritten by a
 * stale Room emission. [forceUpdate] bypasses the freshness guard for
 * rollover re-derivations, where the period bounds changed even though
 * the persisted data is not newer.
 * If the item does not exist, it is added to the list.
 *
 * @param toUpdateThekrCountList The list to be updated.
 * @param updatedThekrCountItem The updated ThekrCount item.
 * @param forceUpdate Replace an existing entry regardless of timestamps.
 */
fun updateThekrCountItem(
    toUpdateThekrCountList: SnapshotStateList<MutableState<ThekrCount>>,
    updatedThekrCountItem: MutableState<ThekrCount>,
    forceUpdate: Boolean = false,
) {
    val existingItemIndex = toUpdateThekrCountList.indexOfFirst {
        it.value.thekrId == updatedThekrCountItem.value.thekrId
    }

    if (existingItemIndex != -1) {
        val existingItem = toUpdateThekrCountList[existingItemIndex]
        if (forceUpdate || existingItem.value.timeUpdated < updatedThekrCountItem.value.timeUpdated) {
            // Update the existing MutableState directly
            existingItem.value = updatedThekrCountItem.value
        }
    } else {
        // Add the new item if it doesn't exist
        toUpdateThekrCountList.add(updatedThekrCountItem)
    }
}

/**
 * Removes the count entries of instances that no longer exist in the
 * category, so countList doesn't accumulate stale ThekrCount items for every
 * instance ever created.
 *
 * @param toUpdateThekrCountList The list to be updated.
 * @param removedThekrIds The ThekrCount keys (thekrId values) whose
 *     instances are gone.
 */
fun removeThekrCountItems(
    toUpdateThekrCountList: SnapshotStateList<MutableState<ThekrCount>>,
    removedThekrIds: Set<Long>,
) {
    if (removedThekrIds.isEmpty()) return
    toUpdateThekrCountList.removeIf { it.value.thekrId in removedThekrIds }
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
    updatedCountMissList: List<CountMissDetails>,
) {
    val existingCountMissIds = toUpdateCountMissList.mapTo(HashSet()) { it.id }
    updatedCountMissList.filter { it.id !in existingCountMissIds }
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
    val existingFadlIds = toUpdateFadlList.mapTo(HashSet()) { it.id }
    val updatedFadlIds = updatedFadlList.mapTo(HashSet()) { it.id }

    // Remove items that are no longer in the updated list
    toUpdateFadlList.removeIf { it.id !in updatedFadlIds }

    val indexById = HashMap<Long, Int>()
    toUpdateFadlList.forEachIndexed { index, fadl ->
        indexById.putIfAbsent(fadl.id, index)
    }

    // Update existing FadlDetails items
    updatedFadlList.forEach { newFadl ->
        val index = indexById[newFadl.id]
        if (index != null && toUpdateFadlList[index].timeUpdated < newFadl.timeUpdated) {
            // Update the item in the list directly
            toUpdateFadlList[index] = newFadl
        }
    }

    // Add new FadlDetails items
    updatedFadlList.filter { it.id !in existingFadlIds }
        .forEach { toUpdateFadlList.add(it) }
}
