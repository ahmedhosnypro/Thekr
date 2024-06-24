@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.thekr.ui.counter

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.settings.SettingsHelper.settingViewModel
import com.thekr.data.settings.SettingsHelper.settingsDetails
import com.thekr.data.zekr.count.ZekrCount
import com.thekr.data.zekr.instance.ZekrInstanceDetails
import com.thekr.data.zekr.zekr.ZekrDetails
import com.thekr.ui.counter.viewModel.CounterUiState
import com.thekr.ui.counter.viewModel.ZekrCounterViewModel
import com.thekr.ui.counter.viewModel.action.onZekrCounterCount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Helper object for managing actions and data related to the Counter
 * screen.
 */
object CounterHelper {
    var onCount: () -> Unit = {}
    var onClickFadl: () -> Unit = {}

    // Settings
    var onSettingUpdate: (SettingsDetails) -> Unit = {}
    var onClickThemeMode: () -> Unit = {}
    var onClickIncreaseFontSize: () -> Unit = {}
    var onClickDecreaseFontSize: () -> Unit = {}
    var onChangeSheikh: (String) -> Unit = {}
    var onClickCountVisibility: () -> Unit = {}
    var toggleDailyCountVisibility: () -> Unit = {}
    var toggleWeeklyCountVisibility: () -> Unit = {}
    var toggleMonthlyCountVisibility: () -> Unit = {}
    var toggleYearlyCountVisibility: () -> Unit = {}
    var toggleTotalCountVisibility: () -> Unit = {}
    var toggleSessionCountVisibility: () -> Unit = {}
    var showSheikhSelectorList: () -> Unit = {}

    // Statistics
//    var getDayStatistics: (Long, DayStatisticsType) -> CountStatistics =
//        { _, _ -> CountStatistics() }
//    var getWeekStatistics: (Long) -> CountStatistics = { CountStatistics() }
//    var getMonthStatistics: (Long) -> CountStatistics = { CountStatistics() }

    // Navigation and UI
    var onCounterDispose: () -> Unit = {}
    var scrollToZekr: (Int) -> Unit = {}
    var scrollToNextZekr: () -> Unit = {}
    var onEditClick: () -> Unit = {}
    var onNavigateUp: () -> Unit = {}
    var showCategoryZekrListMenu: () -> Unit = {}
    var showZekrStatistics: () -> Unit = {}
    var onClickSound: () -> Unit = {}
    var isPlayingSound: () -> Boolean = { false }
    var updateUiState: (CounterUiState) -> Unit = {}
    var updateOnCount: () -> Unit = {}

    // Data access
    var getCurrentZekrInstance: () -> MutableState<ZekrInstanceDetails> =
        { mutableStateOf(ZekrInstanceDetails()) }
    var getZekrInstance: (Int) -> MutableState<ZekrInstanceDetails> =
        { mutableStateOf(ZekrInstanceDetails()) }
    var getZekrCount: (Int) -> MutableState<ZekrCount> = { mutableStateOf(ZekrCount()) }
    var getCurrentZekrCount: () -> MutableState<ZekrCount> = { mutableStateOf(ZekrCount()) }
    var getZekr: (Int) -> MutableState<ZekrDetails> = { mutableStateOf(ZekrDetails()) }


    /**
     * Initializes actions for the Counter screen, connecting UI events to
     * ViewModel functions.
     *
     * @param actionComponents [CounterActionComponents] components related to
     *     the Counter screen.
     */
    fun initActions(actionComponents: CounterActionComponents) {
        initCountingActions(actionComponents.counterViewModel)
        initBottomSheetActions(actionComponents)
        initSettingsActions()
        initStatisticsActions(actionComponents.counterViewModel)
        initNavigationAndUiActions(actionComponents)
        initDataAccess(actionComponents.counterViewModel)
    }

    // --- Counting Actions ---
    private fun initCountingActions(counterViewModel: ZekrCounterViewModel) {
        onCount = { counterViewModel.onZekrCounterCount() }
        updateOnCount = counterViewModel::updateOnCount
    }

    // --- Bottom Sheet and Count Visibility Actions ---
    private fun initBottomSheetActions(components: CounterActionComponents) {

        onClickCountVisibility = {
            settingViewModel.changeCountVisibility()
            handleBottomSheetExpansion(components)
        }

        toggleDailyCountVisibility = {
            settingViewModel.update(settingsDetails.copy(showDailyCount = !settingsDetails.showDailyCount))
        }

        toggleWeeklyCountVisibility = {
            settingViewModel.update(settingsDetails.copy(showWeeklyCount = !settingsDetails.showWeeklyCount))
        }

        toggleMonthlyCountVisibility = {
            settingViewModel.update(settingsDetails.copy(showMonthlyCount = !settingsDetails.showMonthlyCount))
        }

        toggleYearlyCountVisibility = {
            settingViewModel.update(settingsDetails.copy(showYearlyCount = !settingsDetails.showYearlyCount))
        }

        toggleTotalCountVisibility = {
            settingViewModel.update(settingsDetails.copy(showTotalCount = !settingsDetails.showTotalCount))
        }

        toggleSessionCountVisibility = {
            settingViewModel.update(settingsDetails.copy(showSessionCount = !settingsDetails.showSessionCount))
        }
    }

    // --- Settings Actions ---
    private fun initSettingsActions() {
        onClickThemeMode = { settingViewModel.changeThemeMode() }
        onClickIncreaseFontSize = { settingViewModel.increaseFontSize() }
        onClickDecreaseFontSize = { settingViewModel.decreaseFontSize() }
        onSettingUpdate = { newSettings -> settingViewModel.update(newSettings) }
    }

    // --- Statistics Actions ---
    private fun initStatisticsActions(counterViewModel: ZekrCounterViewModel) {
//        getDayStatistics = { midnight, dayStatisticsType ->
//            getDayStatistics(counterViewModel, midnight, dayStatisticsType)
//        }
//        getWeekStatistics = { time -> getWeekStatistics(counterViewModel, time) }
        // TODO: Implement month statistics
        // getMonthStatistics = { time -> getMonthStatistics(counterViewModel, time) }
    }

    // --- Navigation and UI Actions ---
    @OptIn(ExperimentalFoundationApi::class)
    private fun initNavigationAndUiActions(components: CounterActionComponents) {
        val counterViewModel = components.counterViewModel
        val pagerState = components.pagerState

        onCounterDispose = counterViewModel::onCounterDispose

        scrollToZekr = { index ->
            counterViewModel.updateCurrentZekrInstance(index)
            if (counterViewModel.isPlayerPlaying()) {
//                counterViewModel.playZekrAudio()
            }
        }

        scrollToNextZekr = {
            if (pagerState.currentPage < pagerState.pageCount - 1) {
                components.coroutineScope.launch {
                    pagerState.animateScrollToPage(
                        pagerState.currentPage + 1,
                        animationSpec = tween(100)
                    )
                }
            }
        }

        onNavigateUp = counterViewModel::onNavigateUp
        showCategoryZekrListMenu = { counterViewModel.showCategoryZekrListMenu() }
        showZekrStatistics = { counterViewModel.showStatistics() }

        onClickSound = {
//            if (counterViewModel.isPlayerPlaying()) {
//                stopPlayer(counterViewModel)
//            } else {
//                counterViewModel.playZekrAudio()
//            }
        }

        isPlayingSound = { counterViewModel.isPlayerPlaying() }
        updateUiState = counterViewModel::updateUiState
    }

    // --- Data Access ---
    private fun initDataAccess(counterViewModel: ZekrCounterViewModel) {
        getCurrentZekrInstance = counterViewModel::getCurrentZekrInstance
        getZekrInstance = counterViewModel::getZekrInstance
        getZekrCount = counterViewModel::getZekrCount
        getCurrentZekrCount = counterViewModel::getCurrentZekrCount
        getZekr = counterViewModel::getZekr
    }

    // --- Helper Functions ---

    /**
     * Handles the expansion/collapse behavior of the bottom sheet based on
     * count visibility settings.
     */
    private fun handleBottomSheetExpansion(components: CounterActionComponents) {
        val countVisible = settingsDetails.showCount
        components.coroutineScope.launch {
            val sheetState = components.zekrCountSheetState.bottomSheetState
            when {
                countVisible -> sheetState.expand()

                components.counterViewModel.mutableUiState.value
                    .categoryDetails.value.zekrList.size > 1 -> sheetState.partialExpand()

                else -> sheetState.hide()
            }
        }
    }

    /** Data class to hold components related to counter actions. */
    @Stable
    @Immutable
    data class CounterActionComponents(
        val counterViewModel: ZekrCounterViewModel,
        val coroutineScope: CoroutineScope,
        val zekrCountSheetState: BottomSheetScaffoldState,
        val pagerState: PagerState,
    )
}