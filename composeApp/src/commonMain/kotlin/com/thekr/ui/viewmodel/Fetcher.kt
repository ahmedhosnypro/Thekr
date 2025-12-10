package com.thekr.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.thekr.values.Constants
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.data.thekr.count.ThekrCount
import com.thekr.model.Count
import com.thekr.ui.viewmodel.TimeHelper.midnight
import com.thekr.ui.viewmodel.TimeHelper.monthEnd
import com.thekr.ui.viewmodel.TimeHelper.monthStart
import com.thekr.ui.viewmodel.TimeHelper.nextMidnight
import com.thekr.ui.viewmodel.TimeHelper.now
import com.thekr.ui.viewmodel.TimeHelper.weekEnd
import com.thekr.ui.viewmodel.TimeHelper.weekStart
import com.thekr.ui.viewmodel.TimeHelper.yearEnd
import com.thekr.ui.viewmodel.TimeHelper.yearStart
import com.thekr.util.TimeHelper.calcMidnight
import com.thekr.util.TimeHelper.calcMonthEnd
import com.thekr.util.TimeHelper.calcMonthStart
import com.thekr.util.TimeHelper.calcWeekEnd
import com.thekr.util.TimeHelper.calcWeekStart
import com.thekr.util.TimeHelper.calcYearEnd
import com.thekr.util.TimeHelper.calcYearStart
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

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
                println("thekrDetailsList: $thekrDetailsList")
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
                println("thekrInstanceDetailsList: $thekrInstanceDetailsList")
                updateThekrInstanceList(
                    toUpdateThekrInstanceList = categoryDetails.value.thekrInstanceList,
                    updatedThekrInstanceList = thekrInstanceDetailsList
                )

                thekrInstanceDetailsList.forEach { thekrInstanceDetails ->
                    fetchThekrCounts(thekrInstanceDetails.thekrId, categoryDetails)
                }
            }
        }
    }

    /**
     * Fetches and updates the counts for a specific Thekr instance.
     *
     * @param thekrInstanceId The ID of the Thekr instance.
     * @param categoryDetails The MutableState holding the [CategoryDetails] to
     *     be updated.
     */
    private fun AppViewModel.fetchThekrCounts(
        thekrInstanceId: Long,
        categoryDetails: MutableState<CategoryDetails>
    ) {
        viewModelScope.launch {
            val countList: StateFlow<List<Count>> =
                countRepository.findAllByThekrInstanceSync(thekrInstanceId)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
                        initialValue = emptyList()
                    )

            countList.collect { updatedCountList ->
                println("updatedCountList: $updatedCountList")
                // Update ThekrCount item
                updateThekrCountItem(
                    toUpdateThekrCountList = categoryDetails.value.countList,
                    updatedThekrCountItem = mutableStateOf(
                        ThekrCount(
                            thekrInstanceId = thekrInstanceId,
                            dailyCount = updatedCountList.count { it.timeCreated in midnight() until nextMidnight() }
                                .toLong(),
                            weeklyCount = updatedCountList.count { it.timeCreated in weekStart() until weekEnd() }
                                .toLong(),
                            monthlyCount = updatedCountList.count { it.timeCreated in monthStart() until monthEnd() }
                                .toLong(),
                            yearlyCount = updatedCountList.count { it.timeCreated in yearStart() until yearEnd() }
                                .toLong(),
                            totalCount = updatedCountList.size.toLong(),
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
                println("updatedFadlList: $updatedFadlList")
                updateFadlList(
                    toUpdateFadlList = categoryDetails.value.fadlList,
                    updatedFadlList = updatedFadlList,
                )
            }
        }
    }
}

object TimeHelper {
    data class TimeHelper(
        val midnight: Long = calcMidnight(),
        val nextMidnight: Long = midnight + 24 * 60 * 60 * 1000,
        val weekStart: Long = calcWeekStart(midnight),
        val weekEnd: Long = calcWeekEnd(midnight),
        val monthStart: Long = calcMonthStart(midnight),
        val monthEnd: Long = calcMonthEnd(midnight),
        val yearStart: Long = calcYearStart(midnight),
        val yearEnd: Long = calcYearEnd(midnight),
    )

    private var timeHelper = TimeHelper()
    @OptIn(ExperimentalTime::class)
    fun now() = Clock.System.now().toEpochMilliseconds()
    fun midnight() = timeHelper.midnight
    fun nextMidnight() = timeHelper.nextMidnight
    fun weekStart() = timeHelper.weekStart
    fun weekEnd() = timeHelper.weekEnd
    fun monthStart() = timeHelper.monthStart
    fun monthEnd() = timeHelper.monthEnd
    fun yearStart() = timeHelper.yearStart
    fun yearEnd() = timeHelper.yearEnd


    // todo: update timeHelper on next midnight
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