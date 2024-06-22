package com.thekr.ui

import androidx.compose.runtime.MutableState
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.ui.viewmodel.AzkarViewModel
import com.thekr.ui.viewmodel.canNavigateToPreviousCategory
import com.thekr.ui.viewmodel.createNewUserCategory
import com.thekr.ui.viewmodel.navigateToCategory
import com.thekr.ui.viewmodel.navigateToParentCategory
import com.thekr.ui.viewmodel.updateCurrentSebhaViewedCategory

object AzkarActions {
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
        azkarViewModel: AzkarViewModel
    ) {
        canNavigateToPreviousCategory = azkarViewModel::canNavigateToPreviousCategory
        navigateToCategory = azkarViewModel::navigateToCategory
        navigateToParentCategory = azkarViewModel::navigateToParentCategory
        createNewUserCategory = azkarViewModel::createNewUserCategory
        updateCurrentSebhaViewedCategory = azkarViewModel::updateCurrentSebhaViewedCategory
        sebhaTabSubTabsInitialPage = azkarViewModel::sebhaTabSubTabsInitialPage
    }
}
