package com.thekr.ui.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.CounterEntryRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Actions that can be performed on the Home screen.
 *
 * Holds references captured from the Home entry's composition (the
 * HomeViewModel and its composition scope); [HomeScreen] registers them on
 * entry and [clearActions] on dispose, so invocations outside that window
 * safely no-op instead of launching on a cancelled scope or pinning the
 * dead ViewModel.
 */
object HomeActions {
    private var uiCoroutine: CoroutineScope? = null
    private var snackBarHostState: SnackbarHostState? = null

    var onThekrClick: (tabIndex: Int, categoryId: Long, thekrId: Long) -> Unit = { _, _, _ -> }
    var onCategoryClick: (tabIndex: Int, categoryDetails: MutableState<CategoryDetails>) -> Unit = { _, _ -> }
    var onThekrCategoryClick: () -> Unit = {}

    fun initActions(
        homeViewModel: HomeViewModel,
        uiCoroutine: CoroutineScope,
        snackBarHostState: SnackbarHostState,
    ) {
        this.uiCoroutine = uiCoroutine
        this.snackBarHostState = snackBarHostState

        onThekrClick = { tabIndex, categoryId, thekrId ->
            homeViewModel.onThekrClick(tabIndex, categoryId, thekrId)
        }

        onCategoryClick = { tabIndex, categoryDetails ->
            homeViewModel.onCategoryClick(
                tabIndex = tabIndex,
                categoryDetails = categoryDetails,
                showSnackBar = { message -> showSnackBar(message) },
            )
        }

        onThekrCategoryClick = {
            NavigationActions.navigate(CounterEntryRoute.route)
        }
    }

    private fun showSnackBar(message: String) {
        val scope = uiCoroutine
        val hostState = snackBarHostState
        // The composition scope is cancelled once Home leaves composition;
        // after that there is no host to show the snackbar in, so drop the
        // request instead of launching on the dead scope.
        if (scope != null && scope.isActive && hostState != null) {
            scope.launch { hostState.showSnackbar(message) }
        }
    }

    fun clearActions() {
        uiCoroutine = null
        snackBarHostState = null
        onThekrClick = { _, _, _ -> }
        onCategoryClick = { _, _ -> }
        onThekrCategoryClick = {}
    }
}
