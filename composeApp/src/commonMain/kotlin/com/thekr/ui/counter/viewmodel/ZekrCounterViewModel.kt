package com.thekr.ui.counter.viewmodel


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thekr.data.count.count.CountRepository
import com.thekr.data.count.miss.CountMissRepository
import com.thekr.data.thekr.count.ThekrCount
import com.thekr.data.thekr.instance.ThekrInstanceDetails
import com.thekr.data.thekr.thekr.ThekrDetails
import com.thekr.data.thekr.thekr.ThekrRepository
import com.thekr.ui.counter.viewmodel.action.AntiSleep.killDetectSleepingJob
import com.thekr.ui.counter.viewmodel.action.AntiSleep.stopDetectSleepingJob
import com.thekr.ui.counter.viewmodel.action.ThekrSoundPlayer
import com.thekr.ui.counter.viewmodel.action.configSleepJop

import com.thekr.ui.counter.viewmodel.init.initCoolDown
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.HomeRoute
import com.thekr.ui.navigation.route.ThekrScreenRoute
import com.thekr.ui.viewmodel.AppStateHolder.appState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThekrCounterViewModel(
    savedStateHandle: SavedStateHandle,
    val thekrRepository: ThekrRepository,
    val countRepository: CountRepository,
    val countMissRepository: CountMissRepository,
) : ViewModel() {
    val categoryId: Long = checkNotNull(savedStateHandle[ThekrScreenRoute.CATEGORY_ID_ARG])

    val thekrId: Long = checkNotNull(savedStateHandle[ThekrScreenRoute.ZEKR_ID_ARG])
    val initialPage: Int = checkNotNull(
        savedStateHandle[ThekrScreenRoute.INITIAL_PAGE_ARG]
    )
    val pageCount: Int = checkNotNull(
        savedStateHandle[ThekrScreenRoute.PAGE_COUNT_ARG]
    )

//    val destination = savedStateHandle.toRoute<ThekrScreenRoute>()
//    val categoryId: Long = destination.categoryId
//    val thekrId: Long = destination.thekrId
//    val initialPage: Int = destination.initialPage
//    val pageCount: Int = destination.pageCount

    val mutableUiState = MutableStateFlow(CounterUiState(
        categoryDetails = appState.categoryList.first { it.value.id == categoryId }
    ))
    val uiState = mutableUiState.asStateFlow()

    var firstTime = true

    init {
        initCoolDown(this)
        // todo: add validation for count items
    }


    fun delete() {
        if (uiState.value.currentThekrInstance.value.isProtected.not()) {
            viewModelScope.launch {
                thekrRepository.deleteIfNotProtected(uiState.value.currentThekrInstance.value.id)
            }
        }
    }

    fun onCounterDispose() {
        stopDetectSleepingJob()
        ThekrSoundPlayer.stopPlayer()
    }

    fun onNavigateUp() {
//        NavigationActions.navigateUp(HomeRoute::class)
        NavigationActions.navigateUp(HomeRoute.route)
        killDetectSleepingJob()
    }

    fun updateCurrentThekrInstance(index: Int) {
        val thekrInstanceDetails =
            uiState.value.categoryDetails.value.thekrInstanceList.getOrNull(index)
        if (thekrInstanceDetails != null) {
            mutableUiState.update { currentState ->
                currentState.copy(
                    currentThekrInstance = thekrInstanceDetails
                )
            }
        }
    }


    fun showCategoryThekrListMenu() {
        mutableUiState.update { currentState ->
            currentState.copy(
                showCategoryThekrListMenu = true
            )
        }
        stopDetectSleepingJob()
    }

    fun hideCategoryThekrListMenu() {
        mutableUiState.update { currentState ->
            currentState.copy(
                showCategoryThekrListMenu = false
            )
        }
        configSleepJop(this)
    }

    fun getCurrentThekrInstance(): MutableState<ThekrInstanceDetails> =
        uiState.value.currentThekrInstance

    fun getThekrCount(tabIndex: Int): MutableState<ThekrCount> {
        val thekrInstance = uiState.value.categoryDetails.value.thekrInstanceList.getOrNull(tabIndex)
        return uiState.value.categoryDetails.value.countList.firstOrNull { it.value.thekrInstanceId == thekrInstance?.value?.thekrId }
            ?: mutableStateOf(ThekrCount())
    }

    fun getCurrentThekrCount(): MutableState<ThekrCount> {
        val currentThekrInstance = uiState.value.currentThekrInstance
        return uiState.value.categoryDetails.value.countList.firstOrNull { it.value.thekrInstanceId == currentThekrInstance.value.thekrId }
            ?: throw IllegalStateException("getCurrentThekrCount: can't find one")
    }

    fun getThekr(tabIndex: Int): MutableState<ThekrDetails> {
        val thekrInstance = uiState.value.categoryDetails.value.thekrInstanceList.getOrNull(tabIndex)
        return uiState.value.categoryDetails.value.thekrList.firstOrNull { it.value.id == thekrInstance?.value?.thekrId }
            ?: mutableStateOf(ThekrDetails())
    }

    fun getCurrentThekr(): MutableState<ThekrDetails> {
        val currentThekrInstance = uiState.value.currentThekrInstance
        return uiState.value.categoryDetails.value.thekrList.firstOrNull { it.value.id == currentThekrInstance.value.thekrId }
            ?: mutableStateOf(ThekrDetails())
    }

    fun getThekrInstance(tabIndex: Int): MutableState<ThekrInstanceDetails> {
        return uiState.value.categoryDetails.value.thekrInstanceList.getOrNull(tabIndex)
            ?: mutableStateOf(ThekrInstanceDetails())
    }

    fun tabIndexOf(thekrInstanceId: Long): Int {
        return uiState.value.categoryDetails.value.thekrInstanceList.indexOfFirst {
            it.value.id == thekrInstanceId
        }
    }

    fun updateUiState(counterUiState: CounterUiState) {
        mutableUiState.update {
            counterUiState
        }
    }

    fun hideStatistics() {
        mutableUiState.update {
            it.copy(
                showStatistics = false
            )
        }
        configSleepJop(this)
    }

    fun showStatistics() {
        mutableUiState.update {
            it.copy(
                showStatistics = true
            )
        }
        stopDetectSleepingJob()
    }

    fun updateOnCount() {
        var count by getCurrentThekrCount()
        count = count.copy(
            dailyCount = count.dailyCount + 1,
            weeklyCount = count.weeklyCount + 1,
            monthlyCount = count.monthlyCount + 1,
            yearlyCount = count.yearlyCount + 1,
            totalCount = count.totalCount + 1,
            timeUpdated = System.currentTimeMillis()
        )
    }
}

fun interface SuspendRunnable {
    suspend fun run()
}
