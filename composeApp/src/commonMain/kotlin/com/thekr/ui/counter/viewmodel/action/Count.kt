package com.thekr.ui.counter.viewmodel.action

import androidx.lifecycle.viewModelScope
import com.thekr.data.thekr.count.ThekrCount
import com.thekr.data.thekr.instance.ThekrInstanceDetails
import com.thekr.data.thekr.thekr.ThekrDetails
import com.thekr.model.Count
import com.thekr.model.CountMiss
import com.thekr.ui.counter.CounterHelper
import com.thekr.ui.counter.viewmodel.ThekrCounterViewModel
import com.thekr.ui.counter.viewmodel.action.AntiSleep.restartSleepJop
import com.thekr.ui.counter.viewmodel.action.ThekrSoundPlayer.onPlayAudio
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun ThekrCounterViewModel.onThekrCounterCount() {
    restartSleepJop()
    val thekrInstance = uiState.value.currentThekrInstance.value
    val count = getCurrentThekrCount().value
    val thekr = getCurrentThekr().value
    if (uiState.value.clickable) {
        count(
            thekr,
            thekrInstance,
            count,
            clickSound = true,
        )
        if (count.dailyCount + 1 == thekrInstance.dailyTarget) {
            //todo:
//                viewModelScope.launch {
//                    thekrRepository.update(
//                        currentViewedThekr.toThekr().copy(
//                            dailyTargetReached = true
//                        )
//                    )
//                }
            CounterHelper.scrollToNextThekr()
        }
    } else {
        miss(thekrInstance, count)
    }
}

fun ThekrCounterViewModel.repeatAudio() {
    restartSleepJop()
    val thekrInstance = uiState.value.currentThekrInstance
    val thekrInstanceVal = thekrInstance.value
    val nextThekrIndex =
        uiState.value.categoryDetails.value.thekrInstanceList.indexOf(thekrInstance) + 1
    val count = getCurrentThekrCount().value
    val thekr = getCurrentThekr().value

    val clickable = uiState.value.clickable
    if (clickable) {
        when {
            count.dailyCount + 1 < thekrInstanceVal.dailyTarget -> {
                count(thekr, thekrInstanceVal, count, clickSound = false)
                onPlayAudio()
            }

            count.dailyCount + 1 == thekrInstanceVal.dailyTarget -> {
                count(thekr, thekrInstanceVal, count, clickSound = false)
                updateCurrentThekrInstance(nextThekrIndex)
                CounterHelper.scrollToNextThekr()
                onPlayAudio()
            }

            count.dailyCount + 1 > thekrInstanceVal.dailyTarget -> {

                updateCurrentThekrInstance(nextThekrIndex)
                CounterHelper.scrollToNextThekr()
                onPlayAudio()
            }
        }
    }
}

private fun ThekrCounterViewModel.count(
    thekr: ThekrDetails,
    thekrInstance: ThekrInstanceDetails,
    count: ThekrCount,
    clickSound: Boolean = true,
) {
    // feedback
    viewModelScope.launch {
        counterClickFeedBack(
            label = thekr.text,
            count = count.dailyCount + 1,
            clickSound = clickSound,
        )
    }

    // clickable
    mutableUiState.update {
        it.copy(
            clickable = false,
        )
    }
    viewModelScope.launch {
        delay(thekr.coolDown)
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
                    thekrInstanceId = thekrInstance.id,
                    thekrCategoryId = categoryId,
                    value = count.totalCount + 1,
                    timeCreated = System.currentTimeMillis()
                )
            )
        }

        CounterHelper.updateOnCount()
    }
}

private fun ThekrCounterViewModel.miss(
    thekrInstance: ThekrInstanceDetails,
    count: ThekrCount
) {
    viewModelScope.launch {
        countMissRepository.insert(
            CountMiss(
                thekrInstanceId = thekrInstance.id,
                thekrCategoryId = categoryId,
                value = count.totalCount + 1,
                timeCreated = System.currentTimeMillis()
            )
        )
    }
}