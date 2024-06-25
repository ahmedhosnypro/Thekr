package com.thekr.ui.counter.viewmodel.init

import androidx.lifecycle.viewModelScope
import com.thekr.ui.counter.viewmodel.ZekrCounterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

fun initCoolDown(
    viewModel: ZekrCounterViewModel,
) {
    with(viewModel) {
        viewModelScope.launch {
            if (firstTime) {
                val zekr = zekrRepository.findById(zekrId).firstOrNull()
                val countItem = countRepository.getLastCountByZekrInstanceId(zekrId).first()
                // if a user exits the view, and returned in less than cooldown time,
                // then cooldown
                if (countItem != null && zekr != null) {
                    val lastCountItemTime = countItem.timeCreated
                    val cooldown = zekr.coolDown
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