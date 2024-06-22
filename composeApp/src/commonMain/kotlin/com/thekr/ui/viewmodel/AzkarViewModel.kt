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
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.data.zekr.category.CategoryRepository
import com.thekr.data.zekr.fadl.FadlRepository
import com.thekr.data.zekr.instance.ZekrInstanceRepository
import com.thekr.data.zekr.zekr.ZekrRepository
import com.thekr.model.ZekrCategoryType
import com.thekr.ui.viewmodel.Fetcher.fetchCategory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AzkarViewModel(
    val zekrRepository: ZekrRepository,
    val zekrInstanceRepository: ZekrInstanceRepository,
    val countRepository: CountRepository,
    val countMissRepository: CountMissRepository,
    val categoryRepository: CategoryRepository,
    val fadlRepository: FadlRepository,
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    val mutableAzkarState = MutableStateFlow(AzkarState())
    val azkarState = mutableAzkarState.asStateFlow()

    init {
        intiCategoryList()
    }

    private fun intiCategoryList() {
        viewModelScope.launch(ioDispatcher) {
            val childCategories = categoryRepository.findAll().first().map { category ->
                mutableStateOf(category.toCategoryDetails())
            }

            childCategories.sortedBy {
                when (it.value.parent) {
                    ZekrCategoryType.User.id -> 0L
                    ZekrCategoryType.HesnAlMuslim.id -> 1L
                    ZekrCategoryType.Knooz.id -> 2L
                    ZekrCategoryType.Dua.id -> 3L
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

            mutableAzkarState.update { currentState ->
                currentState.copy(
                    categoryList = childCategories.toMutableStateList(),
                    userAzkar = childCategories.firstOrNull { it.value.id == ZekrCategoryType.User.id }
                        ?: mutableStateOf(CategoryDetails()),
                    hesnAlmuslimStack = mutableStateListOf(childCategories.firstOrNull { it.value.id == ZekrCategoryType.HesnAlMuslim.id }
                        ?: mutableStateOf(CategoryDetails())),
                    knoozStack = mutableStateListOf(childCategories.firstOrNull { it.value.id == ZekrCategoryType.Knooz.id }
                        ?: mutableStateOf(CategoryDetails())),
                    duaCategoryStack = mutableStateListOf(childCategories.firstOrNull { it.value.id == ZekrCategoryType.Dua.id }
                        ?: mutableStateOf(CategoryDetails())),
                    currentViewedSebhaCategory = childCategories.firstOrNull { it.value.id == ZekrCategoryType.User.id }
                        ?.value?.childCategories?.firstOrNull()
                        ?: mutableStateOf(CategoryDetails()),
                )
            }
        }
    }

    fun sebhaTabSubTabsInitialPage(): Int {
        val index = mutableAzkarState.value.userAzkar.value.childCategories.indexOfFirst {
            it.value.id == mutableAzkarState.value.currentViewedSebhaCategory?.value?.id
        }
        return if (index == -1) 0 else index
    }
}

@Stable
@Immutable
data class AzkarState(
    val categoryList: SnapshotStateList<MutableState<CategoryDetails>> = mutableStateListOf(),

    val userAzkar: MutableState<CategoryDetails> = mutableStateOf(CategoryDetails()),
    val knoozStack: SnapshotStateList<MutableState<CategoryDetails>> = mutableStateListOf(),
    val hesnAlmuslimStack: SnapshotStateList<MutableState<CategoryDetails>> = mutableStateListOf(),
    val duaCategoryStack: SnapshotStateList<MutableState<CategoryDetails>> = mutableStateListOf(),

    val currentViewedSebhaCategory: MutableState<CategoryDetails>? = null,
)

object AzkarStateHelper{
    /**
     * use for background tasks only
     */
    lateinit var azkarState: AzkarState

    fun updateState(azkarState: AzkarState){
        this.azkarState = azkarState
    }
}