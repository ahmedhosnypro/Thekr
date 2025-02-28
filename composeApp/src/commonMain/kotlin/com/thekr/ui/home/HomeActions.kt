package com.thekr.ui.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.CounterEntryRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** Represents actions that can be performed on the Home screen. */

object HomeActions {
    lateinit var onThekrClick: (tabIndex: Int, categoryId: Long, thekrId: Long) -> Unit
    lateinit var onCategoryClick: (tabIndex: Int, categoryDetails: MutableState<CategoryDetails>) -> Unit
    lateinit var onThekrCategoryClick: () -> Unit

    fun initActions(
        homeViewModel: HomeViewModel,
        uiCoroutine: CoroutineScope,
        snackBarHostState: SnackbarHostState
    ) {
        onThekrClick = { tabIndex, categoryId, thekrId ->
            homeViewModel.onThekrClick(tabIndex, categoryId, thekrId)
        }

        onCategoryClick = { tabIndex, categoryDetails ->
            homeViewModel.onCategoryClick(
                tabIndex = tabIndex,
                categoryDetails = categoryDetails,
                showSnackBar = { message ->
                    uiCoroutine.launch {
                        snackBarHostState.showSnackbar(message)
                    }
                }
            )
        }

        onThekrCategoryClick = {
//            NavigationActions.navigate(CounterEntryRoute)
            NavigationActions.navigate(CounterEntryRoute.route)
        }
    }

}
