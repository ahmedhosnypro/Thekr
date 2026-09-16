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
import kotlin.concurrent.Volatile
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
        viewModelScope.launch {
            val countList: StateFlow<List<Count>> =
                countRepository.findAllByThekrInstanceSync(thekrInstanceId)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
                        initialValue = emptyList()
                    )

            countList.collect { updatedCountList ->
                // Update ThekrCount item
                updateThekrCountItem(
                    toUpdateThekrCountList = categoryDetails.value.countList,
                    updatedThekrCountItem = mutableStateOf(
                        ThekrCount(
                            thekrInstanceId = thekrId,
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

    @Volatile
    private var timeHelper = TimeHelper()
    @OptIn(ExperimentalTime::class)
    fun now() = Clock.System.now().toEpochMilliseconds()
    private fun refreshIfStale() {
        if (now() >= timeHelper.nextMidnight) {
            timeHelper = TimeHelper()
        }
    }
    fun midnight() = refreshIfStale().let { timeHelper.midnight }
    fun nextMidnight() = refreshIfStale().let { timeHelper.nextMidnight }
    fun weekStart() = refreshIfStale().let { timeHelper.weekStart }
    fun weekEnd() = refreshIfStale().let { timeHelper.weekEnd }
    fun monthStart() = refreshIfStale().let { timeHelper.monthStart }
    fun monthEnd() = refreshIfStale().let { timeHelper.monthEnd }
    fun yearStart() = refreshIfStale().let { timeHelper.yearStart }
    fun yearEnd() = refreshIfStale().let { timeHelper.yearEnd }
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