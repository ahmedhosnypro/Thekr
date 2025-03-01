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
import com.thekr.data.thekr.count.ThekrCount
import com.thekr.data.thekr.instance.ThekrInstanceDetails
import com.thekr.data.thekr.thekr.ThekrDetails
import com.thekr.ui.counter.viewmodel.CounterUiState
import com.thekr.ui.counter.viewmodel.ThekrCounterViewModel
import com.thekr.ui.counter.viewmodel.action.ThekrSoundPlayer
import com.thekr.ui.counter.viewmodel.action.ThekrSoundPlayer.onPlayAudio
import com.thekr.ui.counter.viewmodel.action.onThekrCounterCount
import com.thekr.ui.settings.SettingActions
import com.thekr.ui.settings.SettingActions.currentSettings
import com.thekr.ui.stats.DayStatisticsType
import com.thekr.ui.stats.data.DailyStatisticsData.calcDayStatistics
import com.thekr.ui.stats.data.StatisticsData
import com.thekr.ui.stats.data.WeeklyStatisticsData.calcWeekStatistics
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
    lateinit var dayStatisticsData: (Long, DayStatisticsType) -> StatisticsData
    lateinit var weekStatisticsData: (Long) -> StatisticsData
//    lateinit var monthStatistics: (Long) -> CountStatistics = { CountStatistics() }

    // Navigation and UI
    lateinit var onCounterDispose: () -> Unit
    lateinit var scrollToThekr: (Int) -> Unit
    lateinit var scrollToNextThekr: () -> Unit
    lateinit var onEditClick: () -> Unit
    lateinit var onNavigateUp: () -> Unit
    lateinit var showCategoryThekrListMenu: () -> Unit
    lateinit var showThekrStatistics: () -> Unit
    lateinit var onPlayAudio: () -> Unit
    lateinit var updateUiState: (CounterUiState) -> Unit
    lateinit var updateOnCount: () -> Unit

    // Data access
    lateinit var getCurrentThekrInstance: () -> MutableState<ThekrInstanceDetails>
    lateinit var getThekrInstance: (Int) -> MutableState<ThekrInstanceDetails>
    lateinit var getThekrCount: (Int) -> MutableState<ThekrCount>
    lateinit var getCurrentThekrCount: () -> MutableState<ThekrCount>
    lateinit var getThekr: (Int) -> MutableState<ThekrDetails>

    var tabIndexOf: (thekrInstanceId: Long) -> Int = { 0 }

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
    private fun initCountingActions(counterViewModel: ThekrCounterViewModel) {
        onCount = { counterViewModel.onThekrCounterCount() }
        updateOnCount = counterViewModel::updateOnCount
    }

    // --- Bottom Sheet and Count Visibility Actions ---
    private fun initBottomSheetActions(components: CounterActionComponents) {

        onClickCountVisibility = {
            SettingActions.changeCountVisibility()
            handleBottomSheetExpansion(components)
        }

        toggleDailyCountVisibility = {
            val settingState = currentSettings()
            SettingActions.update(settingState.copy(showDailyCount = !settingState.showDailyCount))
        }

        toggleWeeklyCountVisibility = {
            val settingState = currentSettings()
            SettingActions.update(settingState.copy(showWeeklyCount = !settingState.showWeeklyCount))
        }

        toggleMonthlyCountVisibility = {
            val settingState = currentSettings()
            SettingActions.update(settingState.copy(showMonthlyCount = !settingState.showMonthlyCount))
        }

        toggleYearlyCountVisibility = {
            val settingState = currentSettings()
            SettingActions.update(settingState.copy(showYearlyCount = !settingState.showYearlyCount))
        }

        toggleTotalCountVisibility = {
            val settingState = currentSettings()
            SettingActions.update(settingState.copy(showTotalCount = !settingState.showTotalCount))
        }

        toggleSessionCountVisibility = {
            val settingState = currentSettings()
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
    private fun initStatisticsActions(counterViewModel: ThekrCounterViewModel) {
        dayStatisticsData = { midnight, dayStatisticsType ->
            calcDayStatistics(counterViewModel, midnight, dayStatisticsType)
        }
        weekStatisticsData = { time -> calcWeekStatistics(counterViewModel, time) }
        // TODO: Implement month statistics
        // getMonthStatistics = { time -> getMonthStatistics(counterViewModel, time) }
    }

    // --- Navigation and UI Actions ---
    @OptIn(ExperimentalFoundationApi::class)
    private fun initNavigationAndUiActions(components: CounterActionComponents) {
        val counterViewModel = components.counterViewModel
        val pagerState = components.pagerState

        onCounterDispose = counterViewModel::onCounterDispose

        scrollToThekr = { index ->
            counterViewModel.updateCurrentThekrInstance(index)
        }

        scrollToNextThekr = {
            if (pagerState.currentPage < pagerState.pageCount - 1) {
                components.coroutineScope.launch {
                    pagerState.animateScrollToPage(
                        pagerState.currentPage + 1,
                        animationSpec = tween(100)
                    )
                }
                if (ThekrSoundPlayer.isPlaying) {
                    counterViewModel.onPlayAudio()
                }
            }
        }

        onNavigateUp = { counterViewModel.onNavigateUp() }
        showCategoryThekrListMenu = { counterViewModel.showCategoryThekrListMenu() }
        showThekrStatistics = { counterViewModel.showStatistics() }

        onEditClick = {
            //todo: initialize
        }

        onPlayAudio = {
            counterViewModel.onPlayAudio()
        }

        updateUiState = counterViewModel::updateUiState
    }

    // --- Data Access ---
    private fun initDataAccess(counterViewModel: ThekrCounterViewModel) {
        getCurrentThekrInstance = counterViewModel::getCurrentThekrInstance
        getThekrInstance = counterViewModel::getThekrInstance
        getThekrCount = counterViewModel::getThekrCount
        getCurrentThekrCount = counterViewModel::getCurrentThekrCount
        getThekr = counterViewModel::getThekr
        //    val tabIndex = category.value.thekrInstanceList.indexOf(thekrDetails)
        tabIndexOf = counterViewModel::tabIndexOf
    }

    // --- Helper Functions ---

    /**
     * Handles the expansion/collapse behavior of the bottom sheet based on
     * count visibility settings.
     */
    private fun handleBottomSheetExpansion(components: CounterActionComponents) {
        val countVisible = currentSettings().showCount
        components.coroutineScope.launch {
            val sheetState = components.thekrCountSheetState.bottomSheetState
            when {
                countVisible -> sheetState.expand()

                components.counterViewModel.mutableUiState.value
                    .categoryDetails.value.thekrList.size > 1 -> sheetState.partialExpand()

                else -> sheetState.hide()
            }
        }
    }

    /** Data class to hold components related to counter actions. */
    @Stable
    @Immutable
    data class CounterActionComponents(
        val counterViewModel: ThekrCounterViewModel,
        val coroutineScope: CoroutineScope,
        val thekrCountSheetState: BottomSheetScaffoldState,
        val pagerState: PagerState,
    )
}