package com.thekr.ui.counter.viewmodel.init

import androidx.lifecycle.viewModelScope
import com.thekr.ui.counter.viewmodel.ThekrCounterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

fun initCoolDown(
    viewModel: ThekrCounterViewModel,
) {
    with(viewModel) {
        viewModelScope.launch {
            if (firstTime) {
                val thekr = thekrRepository.findById(thekrId).firstOrNull()
                val instanceId = uiState.value.categoryDetails.value.thekrInstanceList
                    .getOrNull(initialPage)?.value?.id
                val countItem = instanceId?.let {
                    countRepository.getLastCountByThekrInstanceId(it).first()
                }
                // if a user exits the view, and returned in less than cooldown time,
                // then cooldown
                if (countItem != null && thekr != null) {
                    val lastCountItemTime = countItem.timeCreated
                    val cooldown = thekr.coolDown
                    val now = System.currentTimeMillis()
                    val timeDiff = now - lastCountItemTime
                    if (timeDiff < cooldown - 500) {
                        isClickable = false
                        delay(cooldown - 500 - timeDiff)
                        isClickable = true
                    }
                }
                firstTime = false
            }
        }
    }
}