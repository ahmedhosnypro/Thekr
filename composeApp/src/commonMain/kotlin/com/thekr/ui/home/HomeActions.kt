package com.thekr.ui.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.CounterEntryRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** Represents actions that can be performed on the Home screen. */

object HomeActions {
    lateinit var onThekrClick: (tabIndex: Int, categoryId: Long, zekrId: Long) -> Unit
    lateinit var onCategoryClick: (tabIndex: Int, categoryDetails: MutableState<CategoryDetails>) -> Unit
    lateinit var onThekrCategoryClick: () -> Unit

    fun initActions(
        homeViewModel: HomeViewModel,
        uiCoroutine: CoroutineScope,
        snackBarHostState: SnackbarHostState
    ) {
        println("HomeActions.initActions")
        onThekrClick = { tabIndex, categoryId, zekrId ->
            homeViewModel.onZekrClick(tabIndex, categoryId, zekrId)
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
