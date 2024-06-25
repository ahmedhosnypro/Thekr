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
import com.thekr.data.zekr.count.ZekrCount
import com.thekr.data.zekr.instance.ZekrInstanceDetails
import com.thekr.data.zekr.zekr.ZekrDetails
import com.thekr.ui.counter.viewmodel.CounterUiState
import com.thekr.ui.counter.viewmodel.ZekrCounterViewModel
import com.thekr.ui.counter.viewmodel.action.ThekrSoundPlayer
import com.thekr.ui.counter.viewmodel.action.ThekrSoundPlayer.playZekrAudio
import com.thekr.ui.counter.viewmodel.action.onZekrCounterCount
import com.thekr.ui.settings.SettingActions
import com.thekr.ui.settings.SettingActions.settingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Helper object for managing actions and data related to the Counter
 * screen.
 */
object CounterHelper {
    lateinit var onCount: () -> Unit
    lateinit var onClickFadl: () -> Unit

    // Settings
    lateinit var onSettingUpdate: (SettingsDetails) -> Unit
    lateinit var onClickThemeMode: () -> Unit
    lateinit var onClickIncreaseFontSize: () -> Unit
    lateinit var onClickDecreaseFontSize: () -> Unit
    lateinit var onChangeSheikh: (String) -> Unit
    lateinit var onClickCountVisibility: () -> Unit
    lateinit var toggleDailyCountVisibility: () -> Unit
    lateinit var toggleWeeklyCountVisibility: () -> Unit
    lateinit var toggleMonthlyCountVisibility: () -> Unit
    lateinit var toggleYearlyCountVisibility: () -> Unit
    lateinit var toggleTotalCountVisibility: () -> Unit
    lateinit var toggleSessionCountVisibility: () -> Unit
    lateinit var showSheikhSelectorList: () -> Unit

    // Statistics
//    lateinit var getDayStatistics: (Long, DayStatisticsType) -> CountStatistics =
//        { _, _ -> CountStatistics() }
//    lateinit var getWeekStatistics: (Long) -> CountStatistics = { CountStatistics() }
//    lateinit var getMonthStatistics: (Long) -> CountStatistics = { CountStatistics() }

    // Navigation and UI
    lateinit var onCounterDispose: () -> Unit
    lateinit var scrollToZekr: (Int) -> Unit
    lateinit var scrollToNextZekr: () -> Unit
    lateinit var onEditClick: () -> Unit
    lateinit var onNavigateUp: () -> Unit
    lateinit var showCategoryZekrListMenu: () -> Unit
    lateinit var showZekrStatistics: () -> Unit
    lateinit var onClickSound: () -> Unit
    lateinit var updateUiState: (CounterUiState) -> Unit
    lateinit var updateOnCount: () -> Unit

    // Data access
    lateinit var getCurrentZekrInstance: () -> MutableState<ZekrInstanceDetails>
    lateinit var getZekrInstance: (Int) -> MutableState<ZekrInstanceDetails>
    lateinit var getZekrCount: (Int) -> MutableState<ZekrCount>
    lateinit var getCurrentZekrCount: () -> MutableState<ZekrCount>
    lateinit var getZekr: (Int) -> MutableState<ZekrDetails>


    var initialized: MutableState<Boolean> = mutableStateOf(false)

    /**
     * Initializes actions for the Counter screen, connecting UI events to
     * ViewModel functions.
     *
     * @param actionComponents [CounterActionComponents] components related to
     *     the Counter screen.
     */
    fun initActions(actionComponents: CounterActionComponents) {
        initDataAccess(actionComponents.counterViewModel)
        initCountingActions(actionComponents.counterViewModel)
        initBottomSheetActions(actionComponents)
        initSettingsActions()
        initStatisticsActions(actionComponents.counterViewModel)
        initNavigationAndUiActions(actionComponents)
        initialized.value = true
    }

    // --- Counting Actions ---
    private fun initCountingActions(counterViewModel: ZekrCounterViewModel) {
        onCount = { counterViewModel.onZekrCounterCount() }
        updateOnCount = counterViewModel::updateOnCount
    }

    // --- Bottom Sheet and Count Visibility Actions ---
    private fun initBottomSheetActions(components: CounterActionComponents) {

        val settingState = settingState.value

        onClickCountVisibility = {
            SettingActions.changeCountVisibility()
            handleBottomSheetExpansion(components)
        }

        toggleDailyCountVisibility = {
            SettingActions.update(settingState.copy(showDailyCount = !settingState.showDailyCount))
        }

        toggleWeeklyCountVisibility = {
            SettingActions.update(settingState.copy(showWeeklyCount = !settingState.showWeeklyCount))
        }

        toggleMonthlyCountVisibility = {
            SettingActions.update(settingState.copy(showMonthlyCount = !settingState.showMonthlyCount))
        }

        toggleYearlyCountVisibility = {
            SettingActions.update(settingState.copy(showYearlyCount = !settingState.showYearlyCount))
        }

        toggleTotalCountVisibility = {
            SettingActions.update(settingState.copy(showTotalCount = !settingState.showTotalCount))
        }

        toggleSessionCountVisibility = {
            SettingActions.update(settingState.copy(showSessionCount = !settingState.showSessionCount))
        }
    }

    // --- Settings Actions ---
    private fun initSettingsActions() {
        onClickThemeMode = { SettingActions.changeThemeMode() }
        onClickIncreaseFontSize = { SettingActions.increaseFontSize() }
        onClickDecreaseFontSize = { SettingActions.decreaseFontSize() }
        onSettingUpdate = { newSettings -> SettingActions.update(newSettings) }
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
            if (ThekrSoundPlayer.isPlaying) {
                counterViewModel.playZekrAudio()
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

        onNavigateUp = { counterViewModel.onNavigateUp() }
        showCategoryZekrListMenu = { counterViewModel.showCategoryZekrListMenu() }
        showZekrStatistics = { counterViewModel.showStatistics() }

        onEditClick = {
            //todo: initialize
        }

        onClickSound = {
            counterViewModel.playZekrAudio()
        }

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
        val countVisible = settingState.value.showCount
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