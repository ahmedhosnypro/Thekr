package com.thekr.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.thekr.ThekrApplication
import com.thekr.ui.counter.viewModel.ZekrCounterViewModel
import com.thekr.ui.zekr.edit.ZekrEditViewModel
import com.thekr.ui.zekr.entry.ZekrEntryViewModel

actual class AppViewModelFactory {
    actual val factory = viewModelFactory {
        /** Initializer for [AzkarViewModel] */
        initializer {
            AzkarViewModel(
                counterApplication().container.zekrRepository,
                counterApplication().container.zekrInstanceRepository,
                counterApplication().container.countRepository,
                counterApplication().container.countMissRepository,
                counterApplication().container.categoryRepository,
                counterApplication().container.fadlRepository,
            )
        }


        /** Initializer for [ZekrEditViewModel] */
        initializer {
            ZekrEditViewModel(
                this.createSavedStateHandle(),
                counterApplication().container.zekrRepository,
            )
        }

        /** Initializer for [ZekrEntryViewModel] */
        initializer {
            ZekrEntryViewModel(
                counterApplication().container.zekrRepository,
                counterApplication().container.zekrInstanceRepository,
            )
        }

        /** Initializer for [ZekrCounterViewModel] */
        initializer {
            ZekrCounterViewModel(
                this.createSavedStateHandle(),
                counterApplication().container.zekrRepository,
                counterApplication().container.countRepository,
                counterApplication().container.countMissRepository,
            )
        }
    }
}


/**
 * Extension function to query for [Application] object and returns an
 * instance of [ThekrApplication].
 */
fun CreationExtras.counterApplication(): ThekrApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ThekrApplication)