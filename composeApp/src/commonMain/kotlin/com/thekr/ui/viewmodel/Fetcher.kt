package com.thekr.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.thekr.data.count.count.CountPeriodBounds
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.data.thekr.count.ThekrCount
import com.thekr.util.TimeHelper.midnight
import com.thekr.util.TimeHelper.monthEnd
import com.thekr.util.TimeHelper.monthStart
import com.thekr.util.TimeHelper.nextMidnight
import com.thekr.util.TimeHelper.now
import com.thekr.util.TimeHelper.weekEnd
import com.thekr.util.TimeHelper.weekStart
import com.thekr.util.TimeHelper.yearEnd
import com.thekr.util.TimeHelper.yearStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

object Fetcher {
    /**
     * Fetches and updates data for a specific category in the ViewModel.
     * Only called through [AppViewModel.ensureCategoryFetched], so a
     * category's flows open once, on first display.
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
            thekrRepository.findByCategoryId(categoryDetails.value.id)
                .map { thekrList -> thekrList.map { thekr -> thekr.toThekrDetails() } }
                .collect { thekrDetailsList ->
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
     * The per-instance count collectors live in a Map keyed by instance id:
     * when an instance vanishes from an emission (deleted), its collector Job
     * is cancelled and its countList entry is dropped, so neither collectors
     * nor stale ThekrCount entries outlive their instance.
     *
     * @param categoryDetails The MutableState holding the [CategoryDetails] to
     *     be updated.
     */
    private fun AppViewModel.fetchThekrInstanceListAndCounts(categoryDetails: MutableState<CategoryDetails>) {
        viewModelScope.launch(ioDispatcher) {
            val countCollectorJobs = mutableMapOf<Long, Job>()
            val instanceThekrIds = mutableMapOf<Long, Long>()

            thekrInstanceRepository.findByCategoryId(categoryDetails.value.id)
                .map { thekrInstanceList ->
                    thekrInstanceList.map { thekrInstance -> thekrInstance.toThekrInstanceDetails() }
                }
                .collect { thekrInstanceDetailsList ->
                    updateThekrInstanceList(
                        toUpdateThekrInstanceList = categoryDetails.value.thekrInstanceList,
                        updatedThekrInstanceList = thekrInstanceDetailsList
                    )

                    val currentInstanceIds = thekrInstanceDetailsList.mapTo(HashSet()) { it.id }

                    // Launch one count collector per newly seen instance id.
                    thekrInstanceDetailsList.forEach { thekrInstanceDetails ->
                        if (thekrInstanceDetails.id !in countCollectorJobs) {
                            instanceThekrIds[thekrInstanceDetails.id] = thekrInstanceDetails.thekrId
                            countCollectorJobs[thekrInstanceDetails.id] = fetchThekrCounts(
                                thekrId = thekrInstanceDetails.thekrId,
                                thekrInstanceId = thekrInstanceDetails.id,
                                categoryDetails = categoryDetails,
                            )
                        }
                    }

                    // Cancel the collectors of vanished instances and drop
                    // their countList entries — but only when no surviving
                    // instance shares the entry key.
                    val vanishedInstanceIds = countCollectorJobs.keys - currentInstanceIds
                    if (vanishedInstanceIds.isNotEmpty()) {
                        val survivingThekrIds =
                            thekrInstanceDetailsList.mapTo(HashSet()) { it.thekrId }
                        val removedThekrIds = mutableSetOf<Long>()
                        vanishedInstanceIds.forEach { instanceId ->
                            countCollectorJobs.remove(instanceId)?.cancel()
                            val thekrId = instanceThekrIds.remove(instanceId)
                            if (thekrId != null && thekrId !in survivingThekrIds) {
                                removedThekrIds.add(thekrId)
                            }
                        }
                        removeThekrCountItems(categoryDetails.value.countList, removedThekrIds)
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
     * @return The collector [Job], so the caller can cancel it when the
     *     instance is deleted.
     */
    private fun AppViewModel.fetchThekrCounts(
        thekrId: Long,
        thekrInstanceId: Long,
        categoryDetails: MutableState<CategoryDetails>
    ): Job = viewModelScope.launch(ioDispatcher) {
        midnightTick()
            .flatMapLatest {
                // Bounds are bound at Flow creation, so re-create the
                // collector at each midnight (week/month/year windows
                // all roll over at a midnight too) to keep the SQL
                // period filters in step with the calendar.
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
            }
            .collect { updatedCountTotals ->
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

    /**
     * Emits immediately and then once at each local midnight, so the counts
     * collector is re-created with freshly derived period bounds when the
     * day rolls over — without re-running the aggregation query more often
     * than the count table itself is written to.
     */
    private fun midnightTick(): Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay((nextMidnight() - now()).coerceAtLeast(1))
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
            fadlRepository.findByCategoryId(categoryDetails.value.id).map { fadlList ->
                fadlList.map { fadl -> fadl.toFadlDetails() }
            }.collect { updatedFadlList ->
                updateFadlList(
                    toUpdateFadlList = categoryDetails.value.fadlList,
                    updatedFadlList = updatedFadlList,
                )
            }
        }
    }
}
