package com.thekr.ui.zekr.edit


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.thekr.data.zekr.zekr.ZekrEntry
import com.thekr.data.zekr.zekr.ZekrEntryUiState
import com.thekr.data.zekr.zekr.ZekrRepository
import com.thekr.model.Zekr
import com.thekr.ui.navigation.route.ZekrScreenRoute
import com.thekr.ui.zekr.entry.validateInput
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ZekrEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val zekrRepository: ZekrRepository,
) : ViewModel() {
    private val zekrId: Long = savedStateHandle.toRoute<ZekrScreenRoute>().zekrId

    lateinit var zekr: Zekr
    var counterEditUiState = mutableStateOf(ZekrEntryUiState())
        private set

    init {
        viewModelScope.launch {
            val zekr = zekrRepository.findById(zekrId).first()
            if (zekr != null) {
                this@ZekrEditViewModel.zekr = zekr
                initializeUiState(zekr.toZekrEntry())
            }
        }
    }


    fun updateItem() {
        if (validateInput(counterEditUiState.value)) {
            viewModelScope.launch {
                zekrRepository.update(
                    zekr.update(counterEditUiState.value.zekrEntry)
                )
            }
        }
    }

    private fun initializeUiState(zekrEntry: ZekrEntry) {
        counterEditUiState.value =
            ZekrEntryUiState(
                zekrEntry = zekrEntry
            )
    }


    fun updateLabel(label: String) {
//        updateLabelHelper(label, counterEditUiState)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}