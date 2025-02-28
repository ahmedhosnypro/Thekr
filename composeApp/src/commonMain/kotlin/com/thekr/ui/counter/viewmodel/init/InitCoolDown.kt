package com.thekr.ui.counter.viewmodel.init

import androidx.lifecycle.viewModelScope
import com.thekr.ui.counter.viewmodel.ThekrCounterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

fun initCoolDown(
    viewModel: ThekrCounterViewModel,
) {
    with(viewModel) {
        viewModelScope.launch {
            if (firstTime) {
                val thekr = thekrRepository.findById(thekrId).firstOrNull()
                val countItem = countRepository.getLastCountByThekrInstanceId(thekrId).first()
                // if a user exits the view, and returned in less than cooldown time,
                // then cooldown
                if (countItem != null && thekr != null) {
                    val lastCountItemTime = countItem.timeCreated
                    val cooldown = thekr.coolDown
                    val now = System.currentTimeMillis()
                    val timeDiff = now - lastCountItemTime
                    if (timeDiff < cooldown - 500) {
                        mutableUiState.update { currentState ->
                            currentState.copy(
                                clickable = false
                            )
                        }
                        delay(timeDiff - 500)
                        mutableUiState.update { currentState ->
                            currentState.copy(
                                clickable = true
                            )
                        }
                    }
                }
                firstTime = false
            }
        }
    }
}