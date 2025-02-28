package com.thekr.ui.home

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.model.ThekrCategoryType
import com.thekr.ui.AppActions
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.ThekrScreenRoute
import com.thekr.ui.viewmodel.AppStateHolder.appState

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
            "${ThekrScreenRoute.route}/${categoryId.toInt()}/${thekrId.toInt()}/$initialPage/$pageCount"
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
        val category = categoryDetails.value
        if (category.childCategories.isEmpty()) {
            handleEmptyCategoryClick(category, showSnackBar)
        } else {
            AppActions.navigateToCategory(tabIndex, categoryDetails)
        }
    }

    /**
     * Handles the click event on a category that has no subcategories.
     *
     * @param category The category details.
     * @param showSnackBar The function to display snack bar messages.
     */
    private fun handleEmptyCategoryClick(
        category: CategoryDetails,
        showSnackBar: (String) -> Unit
    ) {
        if (category.thekrList.isNotEmpty()) {
            navigateToThekrScreen(category)
        } else {
            showSnackBar("No Thekr found in this category")
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
                appState.currentViewedSebhaCategory?.value?.thekrList?.size

            ThekrCategoryType.HesnAlMuslim.tabIndex ->
                appState.hesnAlmuslimStack.last().value.thekrList.size

            ThekrCategoryType.Knooz.tabIndex ->
                appState.knoozStack.last().value.thekrList.size

            ThekrCategoryType.Dua.tabIndex ->
                appState.duaCategoryStack.last().value.thekrList.size

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
            "${ThekrScreenRoute.route}/${category.id.toInt()}/${thekrId.toInt()}/0/${category.thekrInstanceList.size}"
        NavigationActions.navigate(route)

    }
}