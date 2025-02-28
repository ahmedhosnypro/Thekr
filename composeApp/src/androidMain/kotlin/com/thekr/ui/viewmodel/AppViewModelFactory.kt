package com.thekr.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.thekr.ThekrApplication
import com.thekr.ui.counter.viewmodel.ThekrCounterViewModel
import com.thekr.ui.thekr.edit.ThekrEditViewModel
import com.thekr.ui.thekr.entry.ThekrEntryViewModel

actual class AppViewModelFactory {
    actual val factory = viewModelFactory {
        /** Initializer for [AppViewModel] */
        initializer {
            AppViewModel(
                counterApplication().container.thekrRepository,
                counterApplication().container.thekrInstanceRepository,
                counterApplication().container.countRepository,
                counterApplication().container.countMissRepository,
                counterApplication().container.categoryRepository,
                counterApplication().container.fadlRepository,
            )
        }


        /** Initializer for [ThekrEditViewModel] */
        initializer {
            ThekrEditViewModel(
                this.createSavedStateHandle(),
                counterApplication().container.thekrRepository,
            )
        }

        /** Initializer for [ThekrEntryViewModel] */
        initializer {
            ThekrEntryViewModel(
                counterApplication().container.thekrRepository,
                counterApplication().container.thekrInstanceRepository,
            )
        }

        /** Initializer for [ThekrCounterViewModel] */
        initializer {
            ThekrCounterViewModel(
                this.createSavedStateHandle(),
                counterApplication().container.thekrRepository,
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