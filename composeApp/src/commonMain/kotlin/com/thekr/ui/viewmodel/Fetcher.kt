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

            thekrInstanceRepository.findByCategoryId(categoryDetails.value.id)
                .map { thekrInstanceList ->
                    thekrInstanceList.map { thekrInstance -> thekrInstance.toThekrInstanceDetails() }
                }
                .collect { thekrInstanceDetailsList ->
                    updateThekrInstanceList(
                        toUpdateThekrInstanceList = categoryDetails.value.thekrInstanceList,
                        updatedThekrInstanceList = thekrInstanceDetailsList,
                    )

                    val currentInstanceIds = thekrInstanceDetailsList.mapTo(HashSet()) { it.id }

                    // Launch one count collector per newly seen instance id.
                    thekrInstanceDetailsList.forEach { thekrInstanceDetails ->
                        if (thekrInstanceDetails.id !in countCollectorJobs) {
                            countCollectorJobs[thekrInstanceDetails.id] = fetchThekrCounts(
                                thekrId = thekrInstanceDetails.thekrId,
                                thekrInstanceId = thekrInstanceDetails.id,
                                categoryDetails = categoryDetails,
                            )
                        }
                    }

                    // Cancel the collectors of vanished instances and drop
                    // their countList entries — each instance owns its own
                    // per-instance entry, so removal is direct and
                    // independent of surviving siblings.
                    val vanishedInstanceIds = countCollectorJobs.keys - currentInstanceIds
                    if (vanishedInstanceIds.isNotEmpty()) {
                        vanishedInstanceIds.forEach { instanceId ->
                            countCollectorJobs.remove(instanceId)?.cancel()
                        }
                        removeThekrCountItems(
                            categoryDetails.value.countList,
                            vanishedInstanceIds,
                        )
                    }
                }
        }
    }

    /**
     * Fetches and updates the counts for a specific Thekr instance.
     *
     * The aggregation query runs against the instance's own id (true
     * per-instance keying); the exposed [ThekrCount] carries that id in
     * [ThekrCount.instanceId], the key its countList entry is matched by —
     * one entry per instance, so concurrent instances of the same Thekr
     * each display their own counts. A re-derivation point flushes the
     * count batch buffer first so the new bounds are computed over complete
     * Room data, and its first emission bypasses the freshness guard so
     * period rollovers always land. Subsequent emissions carry the newest
     * persisted row time as their data freshness, so an emission never
     * overwrites an entry holding optimistic in-memory increments newer
     * than the persisted data.
     *
     * @param thekrId The ID of the Thekr definition; carried as entry
     *     provenance metadata.
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
        categoryDetails: MutableState<CategoryDetails>,
    ): Job = viewModelScope.launch(ioDispatcher) {
        midnightTick()
            .flatMapLatest {
                // Each tick is a re-derivation point: flush the batch
                // buffer so Room is complete, then re-create the collector
                // with freshly derived period bounds (week/month/year
                // windows all roll over at a midnight too), keeping the
                // SQL period filters in step with the calendar without
                // re-running the aggregation query more often than the
                // count table itself is written to.
                countRepository.flush()
                var firstEmission = true
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
                ).map { updatedCountTotals ->
                    val isReDerivation = firstEmission
                    firstEmission = false
                    updatedCountTotals to isReDerivation
                }
            }
            .collect { (updatedCountTotals, isReDerivation) ->
                // Update ThekrCount item
                updateThekrCountItem(
                    toUpdateThekrCountList = categoryDetails.value.countList,
                    updatedThekrCountItem = mutableStateOf(
                        ThekrCount(
                            thekrId = thekrId,
                            instanceId = thekrInstanceId,
                            dailyCount = updatedCountTotals.dailyCount,
                            weeklyCount = updatedCountTotals.weeklyCount,
                            monthlyCount = updatedCountTotals.monthlyCount,
                            yearlyCount = updatedCountTotals.yearlyCount,
                            totalCount = updatedCountTotals.totalCount,
                            timeUpdated = updatedCountTotals.maxTimeCreated,
                        ),
                    ),
                    forceUpdate = isReDerivation,
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
