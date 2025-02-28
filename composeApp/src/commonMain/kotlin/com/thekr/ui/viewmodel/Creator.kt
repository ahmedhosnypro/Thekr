package com.thekr.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.thekr.model.Category
import com.thekr.ui.home.tab.sebha.isValidCategoryName
import com.thekr.ui.viewmodel.Fetcher.fetchCategory
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun AppViewModel.createNewUserCategory(categoryName: String): Int {
    var savedCategoryID: Long
    var savedCategoryTabIndex = -1
    if (isValidCategoryName(categoryName)) {
        viewModelScope.launch {
            runBlocking {
                savedCategoryID = categoryRepository.insert(
                    Category(
                        name = categoryName,
                        parent = 1,
                    )
                )
            }

            runBlocking {
                if (savedCategoryID != -1L) {
                    val savedCategory =
                        categoryRepository.findById(savedCategoryID).firstOrNull()
                            ?.toCategoryDetails()
                    mutableAppState.update {
                        val category = mutableStateOf(savedCategory!!)
                        it.userThekr.value.childCategories.add(category)
                        it.categoryList.add(category)
                        viewModelScope.launch {
                            fetchCategory(category)
                        }
                        it
                    }
                }
            }
            savedCategoryTabIndex =
                mutableAppState.value.userThekr.value.childCategories.indexOfFirst {
                    it.value.id == savedCategoryID
                }
        }
    }
    return savedCategoryTabIndex
}