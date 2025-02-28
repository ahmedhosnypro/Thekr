package com.thekr.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thekr.data.count.count.CountRepository
import com.thekr.data.count.miss.CountMissRepository
import com.thekr.data.initAppData
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.data.thekr.category.CategoryRepository
import com.thekr.data.thekr.fadl.FadlRepository
import com.thekr.data.thekr.instance.ThekrInstanceRepository
import com.thekr.data.thekr.thekr.ThekrRepository
import com.thekr.model.ThekrCategoryType
import com.thekr.ui.viewmodel.Fetcher.fetchCategory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    val thekrRepository: ThekrRepository,
    val thekrInstanceRepository: ThekrInstanceRepository,
    val countRepository: CountRepository,
    val countMissRepository: CountMissRepository,
    val categoryRepository: CategoryRepository,
    val fadlRepository: FadlRepository,
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    val mutableAppState = MutableStateFlow(AppState())
    val appState = mutableAppState.asStateFlow()

    init {
        intiCategoryList()
    }

    val initialized = mutableStateOf(false)
    private fun intiCategoryList() {
        viewModelScope.launch(ioDispatcher) {
            initAppData()
            val childCategories = categoryRepository.findAll().first().map { category ->
                mutableStateOf(category.toCategoryDetails())
            }

            childCategories.sortedBy {
                when (it.value.parent) {
                    ThekrCategoryType.User.id -> 0L
                    ThekrCategoryType.HesnAlMuslim.id -> 1L
                    ThekrCategoryType.Knooz.id -> 2L
                    ThekrCategoryType.Dua.id -> 3L
                    else -> 4
                }
            }.forEach { categoryDetails ->
                viewModelScope.launch(ioDispatcher) {
                    fetchCategory(categoryDetails)
                }
                // childCategories
                categoryDetails.value.childCategories.addAll(childCategories.filter { childCategory ->
                    childCategory.value.parent == categoryDetails.value.id
                })
            }

            mutableAppState.update { currentState ->
                currentState.copy(
                    categoryList = childCategories.toMutableStateList(),
                    userThekr = childCategories.firstOrNull { it.value.id == ThekrCategoryType.User.id }
                        ?: mutableStateOf(CategoryDetails()),
                    hesnAlmuslimStack = mutableStateListOf(childCategories.firstOrNull { it.value.id == ThekrCategoryType.HesnAlMuslim.id }
                        ?: mutableStateOf(CategoryDetails())),
                    knoozStack = mutableStateListOf(childCategories.firstOrNull { it.value.id == ThekrCategoryType.Knooz.id }
                        ?: mutableStateOf(CategoryDetails())),
                    duaCategoryStack = mutableStateListOf(childCategories.firstOrNull { it.value.id == ThekrCategoryType.Dua.id }
                        ?: mutableStateOf(CategoryDetails())),
                    currentViewedSebhaCategory = childCategories.firstOrNull { it.value.id == ThekrCategoryType.User.id }
                        ?.value?.childCategories?.firstOrNull()
                        ?: mutableStateOf(CategoryDetails()),
                )
            }

            initialized.value = true
        }
    }

    fun sebhaTabSubTabsInitialPage(): Int {
        val index = mutableAppState.value.userThekr.value.childCategories.indexOfFirst {
            it.value.id == mutableAppState.value.currentViewedSebhaCategory?.value?.id
        }
        return if (index == -1) 0 else index
    }
}

@Stable
@Immutable
data class AppState(
    val categoryList: SnapshotStateList<MutableState<CategoryDetails>> = mutableStateListOf(),

    val userThekr: MutableState<CategoryDetails> = mutableStateOf(CategoryDetails()),
    val knoozStack: SnapshotStateList<MutableState<CategoryDetails>> = mutableStateListOf(),
    val hesnAlmuslimStack: SnapshotStateList<MutableState<CategoryDetails>> = mutableStateListOf(),
    val duaCategoryStack: SnapshotStateList<MutableState<CategoryDetails>> = mutableStateListOf(),

    val currentViewedSebhaCategory: MutableState<CategoryDetails>? = null,
)

object AppStateHolder {
    /** use for background tasks only */
    lateinit var appState: AppState

    fun updateState(appState: AppState) {
        this.appState = appState
    }
}