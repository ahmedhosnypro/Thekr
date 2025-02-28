package com.thekr.ui.viewmodel

import androidx.compose.runtime.MutableState
import com.thekr.data.thekr.category.CategoryDetails
import kotlinx.coroutines.flow.update

fun AppViewModel.navigateToCategory(
    tabIndex: Int,
    categoryDetails: MutableState<CategoryDetails>,
) {
    mutableAppState.update { currentState ->
        when (tabIndex) {
            1 -> currentState.hesnAlmuslimStack.add(categoryDetails)
            2 -> currentState.knoozStack.add(categoryDetails)
            3 -> currentState.duaCategoryStack.add(categoryDetails)
        }
        currentState
    }
}

fun AppViewModel.canNavigateToPreviousCategory(tabIndex: Int): Boolean {
    return when (tabIndex) {
        1 -> appState.value.hesnAlmuslimStack.size > 1
        2 -> appState.value.knoozStack.size > 1
        3 -> appState.value.duaCategoryStack.size > 1
        else -> false
    }
}

fun AppViewModel.navigateToParentCategory(tabIndex: Int) {
    mutableAppState.update { currentState ->
        when (tabIndex) {
            1 -> currentState.hesnAlmuslimStack.removeLast()
            2 -> currentState.knoozStack.removeLast()
            3 -> currentState.duaCategoryStack.removeLast()
        }
        currentState
    }
}