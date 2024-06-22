package com.thekr.ui.home

import android.annotation.SuppressLint
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.CounterEntryRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** Represents actions that can be performed on the Home screen. */

object HomeActions {
    var onZekrClick: (tabIndex: Int, categoryId: Long, zekrId: Long) -> Unit = { _, _, _ -> }
    var onCategoryClick: (tabIndex: Int, categoryDetails: MutableState<CategoryDetails>) -> Unit =
        { _, _ -> }
    var onCreateZekrClick: () -> Unit = {}

    @SuppressLint("RestrictedApi")
    fun initActions(
        homeViewModel: HomeViewModel,
        uiCoroutine: CoroutineScope,
        snackBarHostState: SnackbarHostState
    ) {
        onZekrClick = { tabIndex, categoryId, zekrId ->
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

        onCreateZekrClick = {
            NavigationActions.navigate(CounterEntryRoute)
        }
    }

}
