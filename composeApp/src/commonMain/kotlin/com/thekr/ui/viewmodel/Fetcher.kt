package com.thekr.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.thekr.Constants
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.data.zekr.count.ZekrCount
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
import com.thekr.util.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

object Fetcher {
    /**
     * Fetches and updates data for a specific category in the ViewModel.
     *
     * @param categoryDetails The MutableState holding the [CategoryDetails] to
     *     be updated.
     */
    fun AzkarViewModel.fetchCategory(
        categoryDetails: MutableState<CategoryDetails>,
    ) {
        fetchZekrList(categoryDetails)
        fetchZekrInstanceListAndCounts(categoryDetails)
        fetchFadlList(categoryDetails)
    }

    /**
     * Fetches and updates the Zekr list for the given category.
     *
     * @param categoryDetails The MutableState holding the [CategoryDetails] to
     *     be updated.
     */
    private fun AzkarViewModel.fetchZekrList(categoryDetails: MutableState<CategoryDetails>) {
        viewModelScope.launch(ioDispatcher) {
            val zekrList = zekrRepository.findByCategoryId(categoryDetails.value.id)
                .map { zekrList -> zekrList.map { zekr -> zekr.toZekrDetails() } }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
                    initialValue = mutableStateListOf()
                )

            zekrList.collect { zekrDetailsList ->
                updateZekrList(
                    toUpdateZekrList = categoryDetails.value.zekrList,
                    updatedZekrList = zekrDetailsList,
                )
            }
        }
    }

    /**
     * Fetches and updates the Zekr instance list and their associated counts
     * for the given category.
     *
     * @param categoryDetails The MutableState holding the [CategoryDetails] to
     *     be updated.
     */
    private fun AzkarViewModel.fetchZekrInstanceListAndCounts(categoryDetails: MutableState<CategoryDetails>) {
        viewModelScope.launch(ioDispatcher) {
            val zekrInstanceList = zekrInstanceRepository.findByCategoryId(categoryDetails.value.id)
                .map { zekrInstanceList ->
                    zekrInstanceList.map { zekrInstance -> zekrInstance.toZekrInstanceDetails() }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
                    initialValue = mutableStateListOf()
                )

            zekrInstanceList.collect { zekrInstanceDetailsList ->
                updateZekrInstanceList(
                    toUpdateZekrInstanceList = categoryDetails.value.zekrInstanceList,
                    updatedZekrInstanceList = zekrInstanceDetailsList
                )

                zekrInstanceDetailsList.forEach { zekrInstanceDetails ->
                    fetchZekrCounts(zekrInstanceDetails.zekrId, categoryDetails)
                }
            }
        }
    }

    /**
     * Fetches and updates the counts for a specific Zekr instance.
     *
     * @param zekrInstanceId The ID of the Zekr instance.
     * @param categoryDetails The MutableState holding the [CategoryDetails] to
     *     be updated.
     */
    private fun AzkarViewModel.fetchZekrCounts(
        zekrInstanceId: Long,
        categoryDetails: MutableState<CategoryDetails>
    ) {
        viewModelScope.launch {
            val countList: StateFlow<List<Count>> =
                countRepository.findAllByZekrInstanceSync(zekrInstanceId)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
                        initialValue = emptyList()
                    )

            countList.collect { updatedCountList ->
                // Update ZekrCount item
                updateZekrCountItem(
                    toUpdateZekrCountList = categoryDetails.value.countList,
                    updatedZekrCountItem = mutableStateOf(
                        ZekrCount(
                            zekrInstanceId = zekrInstanceId,
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
    private fun AzkarViewModel.fetchFadlList(categoryDetails: MutableState<CategoryDetails>) {
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

object TimeHelper {
    data class TimeHelper(
        val midnight: Long = calcMidnight(Clock.System.now().toEpochMilliseconds()),
        val nextMidnight: Long = midnight + 24 * 60 * 60 * 1000,
        val weekStart: Long = calcWeekStart(midnight),
        val weekEnd: Long = calcWeekEnd(midnight),
        val monthStart: Long = calcMonthStart(midnight),
        val monthEnd: Long = calcMonthEnd(midnight),
        val yearStart: Long = calcYearStart(midnight),
        val yearEnd: Long = calcYearEnd(midnight),
    )

    private var timeHelper = TimeHelper()
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
//                        val countMissList = countMissRepository.findByZekrInstanceId(zekrInstanceId).map {
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