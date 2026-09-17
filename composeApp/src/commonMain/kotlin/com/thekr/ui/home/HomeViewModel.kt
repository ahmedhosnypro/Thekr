package com.thekr.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.model.ThekrCategoryType
import com.thekr.ui.AppActions
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.ThekrScreenRoute
import com.thekr.ui.viewmodel.AppStateHolder.appState
import com.thekr.ui.viewmodel.AppViewModelHolder
import com.thekr.values.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

/**
 * ViewModel for the Home screen. Handles navigation logic and data
 * retrieval for the Home screen UI.
 */
class HomeViewModel : ViewModel() {

    /**
     * Handles the click event on a Thekr item. Navigates to the ThekrDetails
     * screen with information about the selected Thekr.
     *
     * @param tabIndex The index of the tab where the Thekr was clicked.
     * @param categoryId The ID of the category containing the Thekr.
     * @param thekrId The ID of the clicked Thekr.
     */
    fun onThekrClick(
        tabIndex: Int,
        categoryId: Long,
        thekrId: Long,
    ) {
        val initialPage = getThekrPageIndex(tabIndex, thekrId) ?: 0
        val pageCount = getThekrPageCount(tabIndex) ?: 0

        val route =
            "${ThekrScreenRoute.route}/$categoryId/$thekrId/$initialPage/$pageCount"
        NavigationActions.navigate(route)
    }

    /**
     * Handles the click event on a Category item. Navigates to either a
     * subcategory or the ThekrDetails screen based on the category content.
     *
     * @param tabIndex The index of the tab where the Category was clicked.
     * @param categoryDetails The state holder for the clicked Category's
     *     details.
     * @param showSnackBar A function to display a Snack bar message.
     */
    fun onCategoryClick(
        tabIndex: Int,
        categoryDetails: MutableState<CategoryDetails>,
        showSnackBar: (String) -> Unit
    ) {
        // Lazy-fetch gate: opens the category's flows before its content is
        // read or navigated to (idempotent per category).
        if (AppViewModelHolder.isAvailable) {
            AppViewModelHolder.appViewModel.ensureCategoryFetched(categoryDetails)
        }
        val category = categoryDetails.value
        if (category.childCategories.isEmpty()) {
            handleEmptyCategoryClick(categoryDetails, showSnackBar)
        } else {
            AppActions.navigateToCategory(tabIndex, categoryDetails)
        }
    }

    /**
     * Handles the click event on a category that has no subcategories.
     *
     * Navigates to the ThekrDetails screen if the category has Thekrs; if its
     * flows are not open yet (first-ever click on a lazy leaf), the gate in
     * [onCategoryClick] has just opened them, so this waits for the first
     * list emission before deciding; a still-empty loaded category shows a
     * snack bar message.
     *
     * @param categoryDetails The state holder for the clicked category.
     * @param showSnackBar The function to display snack bar messages.
     */
    private fun handleEmptyCategoryClick(
        categoryDetails: MutableState<CategoryDetails>,
        showSnackBar: (String) -> Unit
    ) {
        val category = categoryDetails.value
        if (category.thekrList.isNotEmpty()) {
            navigateToThekrScreen(category)
            return
        }
        if (!AppViewModelHolder.isAvailable) {
            showSnackBar("No Thekr found in this category")
            return
        }
        val appViewModel = AppViewModelHolder.appViewModel
        viewModelScope.launch {
            if (appViewModel.isCategoryFetched(category.id)) {
                // Flows already open and the list is still empty — genuinely
                // no Thekr in this category.
                showSnackBar("No Thekr found in this category")
                return@launch
            }
            // First-ever click on this leaf: the gate in onCategoryClick just
            // opened its flows. Room emits the current query result immediately
            // on subscription, so wait for the list to settle, then decide —
            // navigate once loaded, snackbar if it stays empty past the wait.
            val loaded = withTimeoutOrNull(Constants.TIMEOUT_MILLIS) {
                snapshotFlow { categoryDetails.value.thekrList.isNotEmpty() }.first()
            }
            if (loaded != null) {
                navigateToThekrScreen(categoryDetails.value)
            } else {
                showSnackBar("No Thekr found in this category")
            }
        }
    }

    /**
     * Retrieves the index of a Thekr within its category based on the tab and
     * Thekr ID.
     *
     * @param tabIndex The index of the tab.
     * @param thekrId The ID of the Thekr.
     * @return The index of the Thekr within its category, or null if not found.
     */
    private fun getThekrPageIndex(tabIndex: Int, thekrId: Long): Int? {
        return when (tabIndex) {
            ThekrCategoryType.User.tabIndex -> appState.currentViewedSebhaCategory
                ?.value?.thekrInstanceList?.indexOfFirst { it.value.thekrId == thekrId }

            ThekrCategoryType.HesnAlMuslim.tabIndex -> appState.hesnAlmuslimStack.last().value.thekrInstanceList.indexOfFirst { it.value.thekrId == thekrId }

            ThekrCategoryType.Knooz.tabIndex -> appState.knoozStack.last().value
                .thekrInstanceList.indexOfFirst { it.value.thekrId == thekrId }

            ThekrCategoryType.Dua.tabIndex -> appState.duaCategoryStack.last().value
                .thekrInstanceList.indexOfFirst { it.value.thekrId == thekrId }

            else -> null
        }
    }

    /**
     * Retrieves the total number of Thekr pages within a category based on the
     * tab index.
     *
     * @param tabIndex The index of the tab.
     * @return The total number of Thekr pages, or null if the category is
     *     invalid.
     */
    private fun getThekrPageCount(tabIndex: Int): Int? {
        return when (tabIndex) {
            ThekrCategoryType.User.tabIndex ->
                appState.currentViewedSebhaCategory?.value?.thekrInstanceList?.size

            ThekrCategoryType.HesnAlMuslim.tabIndex ->
                appState.hesnAlmuslimStack.last().value.thekrInstanceList.size

            ThekrCategoryType.Knooz.tabIndex ->
                appState.knoozStack.last().value.thekrInstanceList.size

            ThekrCategoryType.Dua.tabIndex ->
                appState.duaCategoryStack.last().value.thekrInstanceList.size

            else -> null
        }
    }

    /**
     * Navigates to the ThekrDetails screen for the first Thekr in the given
     * category.
     *
     * @param category The CategoryDetails of the selected category.
     */
    private fun navigateToThekrScreen(
        category: CategoryDetails,
    ) {
        val thekrId = category.thekrInstanceList.firstOrNull()?.value?.thekrId ?: 0L

        val route =
            "${ThekrScreenRoute.route}/${category.id}/$thekrId/0/${category.thekrInstanceList.size}"
        NavigationActions.navigate(route)

    }
}