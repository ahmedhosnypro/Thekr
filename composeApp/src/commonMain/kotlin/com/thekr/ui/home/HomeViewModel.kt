package com.thekr.ui.home

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.model.ZekrCategoryType
import com.thekr.ui.AzkarActions
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.ZekrScreenRoute
import com.thekr.ui.viewmodel.AzkarStateHelper.azkarState

/**
 * ViewModel for the Home screen. Handles navigation logic and data
 * retrieval for the Home screen UI.
 */
class HomeViewModel : ViewModel() {

    /**
     * Handles the click event on a Zekr item. Navigates to the ZekrDetails
     * screen with information about the selected Zekr.
     *
     * @param tabIndex The index of the tab where the Zekr was clicked.
     * @param categoryId The ID of the category containing the Zekr.
     * @param zekrId The ID of the clicked Zekr.
     */
    fun onZekrClick(
        tabIndex: Int,
        categoryId: Long,
        zekrId: Long,
    ) {
        val initialPage = getZekrPageIndex(tabIndex, zekrId) ?: 0
        val pageCount = getZekrPageCount(tabIndex) ?: 0

       NavigationActions.navigate(
            ZekrScreenRoute(
                categoryId = categoryId,
                zekrId = zekrId,
                initialPage = initialPage,
                pageCount = pageCount
            )
        )
    }

    /**
     * Handles the click event on a Category item. Navigates to either a
     * subcategory or the ZekrDetails screen based on the category content.
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
            AzkarActions.navigateToCategory(tabIndex, categoryDetails)
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
        if (category.zekrList.isNotEmpty()) {
            navigateToZekrScreen(category)
        } else {
            showSnackBar("No Zekr found in this category")
        }
    }

    /**
     * Retrieves the index of a Zekr within its category based on the tab and
     * Zekr ID.
     *
     * @param tabIndex The index of the tab.
     * @param zekrId The ID of the Zekr.
     * @return The index of the Zekr within its category, or null if not found.
     */
    private fun getZekrPageIndex(tabIndex: Int, zekrId: Long): Int? {
        return when (tabIndex) {
            ZekrCategoryType.User.tabIndex -> azkarState.currentViewedSebhaCategory
                ?.value?.zekrInstanceList?.indexOfFirst { it.value.zekrId == zekrId }

            ZekrCategoryType.HesnAlMuslim.tabIndex -> azkarState.hesnAlmuslimStack.last().value.zekrInstanceList.indexOfFirst { it.value.zekrId == zekrId }

            ZekrCategoryType.Knooz.tabIndex -> azkarState.knoozStack.last().value
                .zekrInstanceList.indexOfFirst { it.value.zekrId == zekrId }

            ZekrCategoryType.Dua.tabIndex -> azkarState.duaCategoryStack.last().value
                .zekrInstanceList.indexOfFirst { it.value.zekrId == zekrId }

            else -> null
        }
    }

    /**
     * Retrieves the total number of Zekr pages within a category based on the
     * tab index.
     *
     * @param tabIndex The index of the tab.
     * @return The total number of Zekr pages, or null if the category is
     *     invalid.
     */
    private fun getZekrPageCount(tabIndex: Int): Int? {
        return when (tabIndex) {
            ZekrCategoryType.User.tabIndex ->
                azkarState.currentViewedSebhaCategory?.value?.zekrList?.size

            ZekrCategoryType.HesnAlMuslim.tabIndex ->
                azkarState.hesnAlmuslimStack.last().value.zekrList.size

            ZekrCategoryType.Knooz.tabIndex ->
                azkarState.knoozStack.last().value.zekrList.size

            ZekrCategoryType.Dua.tabIndex ->
                azkarState.duaCategoryStack.last().value.zekrList.size

            else -> null
        }
    }

    /**
     * Navigates to the ZekrDetails screen for the first Zekr in the given
     * category.
     *
     * @param category The CategoryDetails of the selected category.
     */
    private fun navigateToZekrScreen(
        category: CategoryDetails,
    ) {
        val zekrId = category.zekrInstanceList.firstOrNull()?.value?.zekrId ?: 0L

        NavigationActions.navigate(
            ZekrScreenRoute(
                categoryId = category.id,
                zekrId = zekrId,
                pageCount = category.zekrInstanceList.size
            )
        )
    }
}