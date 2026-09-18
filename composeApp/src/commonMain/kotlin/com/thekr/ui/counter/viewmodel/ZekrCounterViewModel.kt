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
import com.thekr.data.thekr.instance.ThekrInstanceRepository
import com.thekr.data.thekr.thekr.ThekrDetails
import com.thekr.data.thekr.thekr.ThekrRepository
import com.thekr.fingerprint.Fingerprint.clearFingerprintListener
import com.thekr.ui.counter.viewmodel.action.AntiSleep.killDetectSleepingJob
import com.thekr.ui.counter.viewmodel.action.AntiSleep.stopDetectSleepingJob
import com.thekr.ui.counter.viewmodel.action.ThekrSoundPlayer
import com.thekr.ui.counter.viewmodel.action.configSleepJop
import com.thekr.ui.counter.viewmodel.init.initCoolDown
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.HomeRoute
import com.thekr.ui.navigation.route.ThekrScreenRoute
import com.thekr.ui.viewmodel.AppStateHolder.appState
import com.thekr.util.TimeHelper.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

// Shared read-only fallbacks for the getters below: a fresh mutableStateOf
// per miss would allocate a throwaway state object on every recomposition
// lookup. They are only returned when the list lookup misses (the entry is
// absent from the list — an already-inconsistent state). Never write these:
// the single write site (updateOnCount) skips the fallback, so a mutated
// value cannot leak into another instance's fallback reads.
private val emptyThekrCountState = mutableStateOf(ThekrCount())
private val emptyThekrDetailsState = mutableStateOf(ThekrDetails())
private val emptyThekrInstanceState = mutableStateOf(ThekrInstanceDetails())

class ThekrCounterViewModel(
    savedStateHandle: SavedStateHandle,
    val thekrRepository: ThekrRepository,
    val countRepository: CountRepository,
    val countMissRepository: CountMissRepository,
    private val thekrInstanceRepository: ThekrInstanceRepository,
) : ViewModel() {
    val categoryId: Long = checkNotNull(savedStateHandle[ThekrScreenRoute.CATEGORY_ID_ARG])

    val thekrId: Long = checkNotNull(savedStateHandle[ThekrScreenRoute.ZEKR_ID_ARG])
    val initialPage: Int = checkNotNull(
        savedStateHandle[ThekrScreenRoute.INITIAL_PAGE_ARG],
    )
    val pageCount: Int = checkNotNull(
        savedStateHandle[ThekrScreenRoute.PAGE_COUNT_ARG],
    )

//    val destination = savedStateHandle.toRoute<ThekrScreenRoute>()
//    val categoryId: Long = destination.categoryId
//    val thekrId: Long = destination.thekrId
//    val initialPage: Int = destination.initialPage
//    val pageCount: Int = destination.pageCount

    val mutableUiState = MutableStateFlow(
        CounterUiState(
            categoryDetails = appState.categoryList.first { it.value.id == categoryId },
        ),
    )
    val uiState = mutableUiState.asStateFlow()

    var firstTime = true

    @Volatile
    var isClickable = true

    init {
        initCoolDown(this)
        // todo: add validation for count items
    }

    fun delete() {
        if (uiState.value.currentThekrInstance.value.isProtected.not()) {
            viewModelScope.launch {
                thekrInstanceRepository.deleteIfNotProtected(uiState.value.currentThekrInstance.value.id)
            }
        }
    }

    fun onCounterDispose() {
        killDetectSleepingJob()
        ThekrSoundPlayer.stopPlayer()
        ThekrSoundPlayer.releaseCache()
        mutableUiState.update { it.copy(isAudioPlaying = false) }
        clearFingerprintListener()
    }
    fun onNavigateUp() {
//        NavigationActions.navigateUp(HomeRoute::class)
        NavigationActions.navigateUp(HomeRoute.route)
        killDetectSleepingJob()
    }

    fun updateCurrentThekrInstance(index: Int) {
        val thekrInstanceList = uiState.value.categoryDetails.value.thekrInstanceList
        // Re-validate after list shifts: a deletion can shrink the list
        // below the requested page, so clamp the index into the survivors
        // instead of leaving currentThekrInstance dangling on a deleted
        // row. The pager's settle gate re-runs this on every list-size
        // change; the identity check skips the no-op copy when the pager
        // re-syncs to the instance already held.
        val validatedIndex = index.coerceIn(0, (thekrInstanceList.size - 1).coerceAtLeast(0))
        val thekrInstanceDetails = thekrInstanceList.getOrNull(validatedIndex)
        if (thekrInstanceDetails != null && uiState.value.currentThekrInstance !== thekrInstanceDetails) {
            mutableUiState.update { currentState ->
                currentState.copy(
                    currentThekrInstance = thekrInstanceDetails,
                )
            }
        }
    }

    fun showCategoryThekrListMenu() {
        mutableUiState.update { currentState ->
            currentState.copy(
                showCategoryThekrListMenu = true,
            )
        }
        stopDetectSleepingJob()
    }

    fun hideCategoryThekrListMenu() {
        mutableUiState.update { currentState ->
            currentState.copy(
                showCategoryThekrListMenu = false,
            )
        }
        configSleepJop(this)
    }

    fun getCurrentThekrInstance(): MutableState<ThekrInstanceDetails> =
        uiState.value.currentThekrInstance

    fun getThekrCount(tabIndex: Int): MutableState<ThekrCount> {
        val thekrInstance = uiState.value.categoryDetails.value.thekrInstanceList.getOrNull(tabIndex)
        return uiState.value.categoryDetails.value.countList.firstOrNull { it.value.instanceId == thekrInstance?.value?.id }
            ?: emptyThekrCountState
    }

    fun getCurrentThekrCount(): MutableState<ThekrCount> {
        val currentThekrInstance = uiState.value.currentThekrInstance
        return uiState.value.categoryDetails.value.countList.firstOrNull { it.value.instanceId == currentThekrInstance.value.id }
            ?: emptyThekrCountState
    }

    fun getThekr(tabIndex: Int): MutableState<ThekrDetails> {
        val thekrInstance = uiState.value.categoryDetails.value.thekrInstanceList.getOrNull(tabIndex)
        return uiState.value.categoryDetails.value.thekrList.firstOrNull { it.value.id == thekrInstance?.value?.thekrId }
            ?: emptyThekrDetailsState
    }

    fun getCurrentThekr(): MutableState<ThekrDetails> {
        val currentThekrInstance = uiState.value.currentThekrInstance
        return uiState.value.categoryDetails.value.thekrList.firstOrNull { it.value.id == currentThekrInstance.value.thekrId }
            ?: emptyThekrDetailsState
    }

    fun getThekrInstance(tabIndex: Int): MutableState<ThekrInstanceDetails> =
        uiState.value.categoryDetails.value.thekrInstanceList.getOrNull(
            tabIndex,
        )
            ?: emptyThekrInstanceState

    fun tabIndexOf(thekrInstanceId: Long): Int = uiState.value.categoryDetails.value.thekrInstanceList.indexOfFirst {
        it.value.id == thekrInstanceId
    }

    fun updateUiState(counterUiState: CounterUiState) {
        mutableUiState.update {
            counterUiState
        }
    }

    fun hideStatistics() {
        mutableUiState.update {
            it.copy(
                showStatistics = false,
            )
        }
        configSleepJop(this)
    }

    fun showStatistics() {
        mutableUiState.update {
            it.copy(
                showStatistics = true,
            )
        }
        stopDetectSleepingJob()
    }

    fun updateOnCount() {
        val countState = getCurrentThekrCount()
        // On a countList miss the getter returns the shared read-only
        // fallback; writing it would leak the increment into other
        // instances' fallback reads. The DB tap row is already inserted
        // by the caller, and the per-instance collector re-derives the
        // missing entry, so skipping the optimistic update loses nothing.
        if (countState === emptyThekrCountState) return
        var count by countState
        count = count.copy(
            dailyCount = count.dailyCount + 1,
            weeklyCount = count.weeklyCount + 1,
            monthlyCount = count.monthlyCount + 1,
            yearlyCount = count.yearlyCount + 1,
            totalCount = count.totalCount + 1,
            timeUpdated = now(),
        )
    }
}

fun interface SuspendRunnable {
    suspend fun run()
}
