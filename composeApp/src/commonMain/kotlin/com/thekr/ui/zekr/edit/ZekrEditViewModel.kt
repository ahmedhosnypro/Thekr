package com.thekr.ui.thekr.edit


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thekr.data.thekr.thekr.ThekrEntry
import com.thekr.data.thekr.thekr.ThekrEntryUiState
import com.thekr.data.thekr.thekr.ThekrRepository
import com.thekr.model.Thekr
import com.thekr.ui.navigation.route.ThekrScreenRoute
import com.thekr.ui.thekr.entry.validateInput
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ThekrEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val thekrRepository: ThekrRepository,
) : ViewModel() {
    private val thekrId: Long =
        checkNotNull(savedStateHandle[ThekrScreenRoute.ZEKR_ID_ARG])


//    private val thekrId: Long = savedStateHandle.toRoute<ThekrScreenRoute>().thekrId

    lateinit var thekr: Thekr
    var counterEditUiState = mutableStateOf(ThekrEntryUiState())
        private set

    init {
        viewModelScope.launch {
            val thekr = thekrRepository.findById(thekrId).first()
            if (thekr != null) {
                this@ThekrEditViewModel.thekr = thekr
                initializeUiState(thekr.toThekrEntry())
            }
        }
    }


    fun updateItem() {
        if (validateInput(counterEditUiState.value)) {
            viewModelScope.launch {
                thekrRepository.update(
                    thekr.update(counterEditUiState.value.thekrEntry)
                )
            }
        }
    }

    private fun initializeUiState(thekrEntry: ThekrEntry) {
        counterEditUiState.value =
            ThekrEntryUiState(
                thekrEntry = thekrEntry
            )
    }


    fun updateLabel(label: String) {
//        updateLabelHelper(label, counterEditUiState)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}