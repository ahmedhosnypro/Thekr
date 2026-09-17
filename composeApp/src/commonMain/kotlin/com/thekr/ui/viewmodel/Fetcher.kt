package com.thekr.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.thekr.data.count.count.CountPeriodBounds
import com.thekr.data.count.count.ThekrInstanceCountTotals
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.data.thekr.count.ThekrCount
import com.thekr.util.TimeHelper.calcMidnight
import com.thekr.util.TimeHelper.calcMonthEnd
import com.thekr.util.TimeHelper.calcMonthStart
import com.thekr.util.TimeHelper.calcWeekEnd
import com.thekr.util.TimeHelper.calcWeekStart
import com.thekr.util.TimeHelper.calcYearEnd
import com.thekr.util.TimeHelper.calcYearStart
import com.thekr.util.TimeHelper.midnight
import com.thekr.util.TimeHelper.monthEnd
import com.thekr.util.TimeHelper.monthStart
import com.thekr.util.TimeHelper.nextMidnight
import com.thekr.util.TimeHelper.now
import com.thekr.util.TimeHelper.weekEnd
import com.thekr.util.TimeHelper.weekStart
import com.thekr.util.TimeHelper.yearEnd
import com.thekr.util.TimeHelper.yearStart
import com.thekr.values.Constants
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

object Fetcher {
    /**
     * Fetches and updates data for a specific category in the ViewModel.
     *
     * @param categoryDetails The MutableState holding the [CategoryDetails] to
     *     be updated.
     */
    fun AppViewModel.fetchCategory(
        categoryDetails: MutableState<CategoryDetails>,
    ) {
        fetchThekrList(categoryDetails)
        fetchThekrInstanceListAndCounts(categoryDetails)
        fetchFadlList(categoryDetails)
    }

    /**
     * Fetches and updates the Thekr list for the given category.
     *
     * @param categoryDetails The MutableState holding the [CategoryDetails] to
     *     be updated.
     */
    private fun AppViewModel.fetchThekrList(categoryDetails: MutableState<CategoryDetails>) {
        viewModelScope.launch(ioDispatcher) {
            val thekrList = thekrRepository.findByCategoryId(categoryDetails.value.id)
                .map { thekrList -> thekrList.map { thekr -> thekr.toThekrDetails() } }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
                    initialValue = mutableStateListOf()
                )

            thekrList.collect { thekrDetailsList ->
                updateThekrList(
                    toUpdateThekrList = categoryDetails.value.thekrList,
                    updatedThekrList = thekrDetailsList,
                )
            }
        }
    }

    /**
     * Fetches and updates the Thekr instance list and their associated counts
     * for the given category.
     *
     * @param categoryDetails The MutableState holding the [CategoryDetails] to
     *     be updated.
     */
    private fun AppViewModel.fetchThekrInstanceListAndCounts(categoryDetails: MutableState<CategoryDetails>) {
        val launchedCountInstanceIds = mutableSetOf<Long>()
        viewModelScope.launch(ioDispatcher) {
            val thekrInstanceList = thekrInstanceRepository.findByCategoryId(categoryDetails.value.id)
                .map { thekrInstanceList ->
                    thekrInstanceList.map { thekrInstance -> thekrInstance.toThekrInstanceDetails() }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
                    initialValue = mutableStateListOf()
                )

            thekrInstanceList.collect { thekrInstanceDetailsList ->
                updateThekrInstanceList(
                    toUpdateThekrInstanceList = categoryDetails.value.thekrInstanceList,
                    updatedThekrInstanceList = thekrInstanceDetailsList
                )

                // Launch the per-instance count collector only once per instance id:
                // Room invalidation re-emits this list on any write, and re-launching
                // would pile up redundant collectors for the lifetime of the ViewModel.
                thekrInstanceDetailsList.forEach { thekrInstanceDetails ->
                    if (launchedCountInstanceIds.add(thekrInstanceDetails.id)) {
                        fetchThekrCounts(thekrInstanceDetails.thekrId, thekrInstanceDetails.id, categoryDetails)
                    }
                }
            }
        }
    }

    /**
     * Fetches and updates the counts for a specific Thekr instance.
     *
     * @param thekrId The ID of the Thekr definition; kept as the exposed
     *     [ThekrCount] key that consumers look counts up by.
     * @param thekrInstanceId The ID of the Thekr instance the counts belong
     *     to.
     * @param categoryDetails The MutableState holding the [CategoryDetails]
     *     to be updated.
     */
    private fun AppViewModel.fetchThekrCounts(
        thekrId: Long,
        thekrInstanceId: Long,
        categoryDetails: MutableState<CategoryDetails>
    ) {
        viewModelScope.launch(ioDispatcher) {
            val countTotals: StateFlow<ThekrInstanceCountTotals> =
                countRepository.getCountTotalsByThekrInstanceId(
                    thekrInstanceId = thekrInstanceId,
                    periods = CountPeriodBounds(
                        dailyStart = midnight(),
                        dailyEnd = nextMidnight(),
                        weeklyStart = weekStart(),
                        weeklyEnd = weekEnd(),
                        monthlyStart = monthStart(),
                        monthlyEnd = monthEnd(),
                        yearlyStart = yearStart(),
                        yearlyEnd = yearEnd(),
                    ),
                )
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
                        initialValue = ThekrInstanceCountTotals(
                            dailyCount = 0,
                            weeklyCount = 0,
                            monthlyCount = 0,
                            yearlyCount = 0,
                            totalCount = 0,
                        )
                    )

            countTotals.collect { updatedCountTotals ->
                // Update ThekrCount item
                updateThekrCountItem(
                    toUpdateThekrCountList = categoryDetails.value.countList,
                    updatedThekrCountItem = mutableStateOf(
                        ThekrCount(
                            thekrInstanceId = thekrId,
                            dailyCount = updatedCountTotals.dailyCount,
                            weeklyCount = updatedCountTotals.weeklyCount,
                            monthlyCount = updatedCountTotals.monthlyCount,
                            yearlyCount = updatedCountTotals.yearlyCount,
                            totalCount = updatedCountTotals.totalCount,
                            timeUpdated = now()
                        )
                    )
                )
            }
        }
    }

    /**
     * Fetches and updates the Fadl list for the given category.
     *
     * @param categoryDetails The MutableState holding the [CategoryDetails] to
     *     be updated.
     */
    private fun AppViewModel.fetchFadlList(categoryDetails: MutableState<CategoryDetails>) {
        viewModelScope.launch(ioDispatcher) {
            val fadlList =
                fadlRepository.findByCategoryId(categoryDetails.value.id).map { fadlList ->
                    fadlList.map { fadl -> fadl.toFadlDetails() }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
                    initialValue = mutableStateListOf()
                )

            fadlList.collect { updatedFadlList ->
                updateFadlList(
                    toUpdateFadlList = categoryDetails.value.fadlList,
                    updatedFadlList = updatedFadlList,
                )
            }
        }
    }
}


//  todo: countMiss List
//                    viewModelScope.launch {
//                        val countMissList = countMissRepository.findByThekrInstanceId(thekrInstanceId).map {
//                            it.map { countMiss ->
//                                countMiss.toCountMissDetails()
//                            }
//                        }.stateIn(
//                            scope = viewModelScope,
//                            started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
//                            initialValue = mutableStateListOf()
//                        )
//
//                        countMissList.collect { updatedCountMissList ->
//                            updateCountMissList(
//                                toUpdateCountMissList = categoryDetails.value.countMissList,
//                                updatedCountMissList = updatedCountMissList,
//                            )
//                        }
//                    }