package com.thekr.ui.counter.viewModel.action

import androidx.lifecycle.viewModelScope
import com.thekr.data.zekr.count.ZekrCount
import com.thekr.data.zekr.instance.ZekrInstanceDetails
import com.thekr.data.zekr.zekr.ZekrDetails
import com.thekr.model.Count
import com.thekr.model.CountMiss
import com.thekr.ui.counter.CounterHelper
import com.thekr.ui.counter.viewModel.ZekrCounterViewModel
import com.thekr.ui.counter.viewModel.action.AntiSleep.restartSleepJop
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun ZekrCounterViewModel.onZekrCounterCount(
) {
    restartSleepJop()
    val zekrInstance = uiState.value.currentZekrInstance.value
    val count = getCurrentZekrCount().value
    val zekr = getCurrentZekr().value
    if (uiState.value.clickable) {
        count(
            zekr,
            zekrInstance,
            count,
            clickSound = true,
        )
        if (count.dailyCount + 1 == zekrInstance.dailyTarget) {
            //todo:
//                viewModelScope.launch {
//                    zekrRepository.update(
//                        currentViewedZekr.toZekr().copy(
//                            dailyTargetReached = true
//                        )
//                    )
//                }
            CounterHelper.scrollToNextZekr()
        }
    } else {
        miss(zekrInstance, count)
    }
}

fun ZekrCounterViewModel.repeatAudio() {
    restartSleepJop()
    val zekrInstance = uiState.value.currentZekrInstance
    val zekrInstanceVal = zekrInstance.value
    val nextZekrIndex =
        uiState.value.categoryDetails.value.zekrInstanceList.indexOf(zekrInstance) + 1
    val count = getCurrentZekrCount().value
    val zekr = getCurrentZekr().value

    val clickable = uiState.value.clickable
    if (clickable) {
        when {
            count.dailyCount + 1 < zekrInstanceVal.dailyTarget -> {
                count(zekr, zekrInstanceVal, count, clickSound = false)
//                playZekrAudio()
            }

            count.dailyCount + 1 == zekrInstanceVal.dailyTarget -> {
                count(zekr, zekrInstanceVal, count, clickSound = false)
                updateCurrentZekrInstance(nextZekrIndex)
                CounterHelper.scrollToNextZekr()
//                playZekrAudio()
            }

            count.dailyCount + 1 > zekrInstanceVal.dailyTarget -> {

                updateCurrentZekrInstance(nextZekrIndex)
                CounterHelper.scrollToNextZekr()
//                playZekrAudio()
            }
        }
    }
}

private fun ZekrCounterViewModel.count(
    zekr: ZekrDetails,
    zekrInstance: ZekrInstanceDetails,
    count: ZekrCount,
    clickSound: Boolean = true,
) {
    // feedback
    viewModelScope.launch {
//        counterClickFeedBack(
//            context = appContext(),
//            label = zekr.text,
//            count = count.dailyCount + 1,
//            clickSound = clickSound,
//        )
    }

    // clickable
    mutableUiState.update {
        it.copy(
            clickable = false,
        )
    }
    viewModelScope.launch {
        delay(zekr.coolDown)
        mutableUiState.update {
            it.copy(
                clickable = true,
            )
        }
    }

    // push updates to db
    viewModelScope.launch {
        runBlocking {
            countRepository.insert(
                Count(
                    zekrInstanceId = zekrInstance.id,
                    zekrCategoryId = categoryId,
                    value = count.totalCount + 1,
                    timeCreated = System.currentTimeMillis()
                )
            )
        }

        CounterHelper.updateOnCount()
    }
}

private fun ZekrCounterViewModel.miss(
    zekrInstance: ZekrInstanceDetails,
    count: ZekrCount
) {
    viewModelScope.launch {
        countMissRepository.insert(
            CountMiss(
                zekrInstanceId = zekrInstance.id,
                zekrCategoryId = categoryId,
                value = count.totalCount + 1,
                timeCreated = System.currentTimeMillis()
            )
        )
    }
}