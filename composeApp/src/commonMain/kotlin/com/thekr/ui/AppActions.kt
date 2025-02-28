package com.thekr.ui

import androidx.compose.runtime.MutableState
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.ui.viewmodel.AppViewModel
import com.thekr.ui.viewmodel.canNavigateToPreviousCategory
import com.thekr.ui.viewmodel.createNewUserCategory
import com.thekr.ui.viewmodel.navigateToCategory
import com.thekr.ui.viewmodel.navigateToParentCategory
import com.thekr.ui.viewmodel.updateCurrentSebhaViewedCategory

object AppActions {
    var canNavigateToPreviousCategory: (tabIndex: Int) -> Boolean = { false }

    lateinit var navigateToCategory: (
        tabIndex: Int,
        categoryDetails: MutableState<CategoryDetails>,
    ) -> Unit

    lateinit var navigateToParentCategory: (tabIndex: Int) -> Unit

    lateinit var createNewUserCategory: (categoryName: String) -> Int

    lateinit var updateCurrentSebhaViewedCategory: (tabIndex: Int) -> Unit

    var sebhaTabSubTabsInitialPage: () -> Int = { 0 }

    fun initActions(
        appViewModel: AppViewModel
    ) {
        canNavigateToPreviousCategory = appViewModel::canNavigateToPreviousCategory
        navigateToCategory = appViewModel::navigateToCategory
        navigateToParentCategory = appViewModel::navigateToParentCategory
        createNewUserCategory = appViewModel::createNewUserCategory
        updateCurrentSebhaViewedCategory = appViewModel::updateCurrentSebhaViewedCategory
        sebhaTabSubTabsInitialPage = appViewModel::sebhaTabSubTabsInitialPage
    }
}
