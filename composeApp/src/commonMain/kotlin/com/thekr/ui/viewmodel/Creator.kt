package com.thekr.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.thekr.model.Category
import com.thekr.ui.home.tab.sebha.isValidCategoryName
import com.thekr.ui.viewmodel.Fetcher.fetchCategory
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

suspend fun AppViewModel.createNewUserCategory(categoryName: String): Int {
    if (!isValidCategoryName(categoryName)) return -1

    val savedCategoryID = categoryRepository.insert(
        Category(
            name = categoryName,
            parent = 1,
        )
    )
    if (savedCategoryID == -1L) return -1

    val savedCategory = categoryRepository.findById(savedCategoryID).firstOrNull()
        ?.toCategoryDetails() ?: return -1

    val category = mutableStateOf(savedCategory)
    mutableAppState.value.userThekr.value.childCategories.add(category)
    mutableAppState.value.categoryList.add(category)
    viewModelScope.launch {
        fetchCategory(category)
    }

    return mutableAppState.value.userThekr.value.childCategories.indexOfFirst {
        it.value.id == savedCategoryID
    }
}
