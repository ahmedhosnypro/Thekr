package com.thekr.ui.viewmodel

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.thekr.JvmApplication
import com.thekr.ui.counter.viewmodel.ThekrCounterViewModel
import com.thekr.ui.thekr.edit.ThekrEditViewModel
import com.thekr.ui.thekr.entry.ThekrEntryViewModel

actual class AppViewModelFactory {
    actual val factory = viewModelFactory {

        /** Initializer for [AppViewModel] */
        initializer {
            AppViewModel(
               JvmApplication.container.thekrRepository,
               JvmApplication.container.thekrInstanceRepository,
               JvmApplication.container.countRepository,
               JvmApplication.container.countMissRepository,
               JvmApplication.container.categoryRepository,
               JvmApplication.container.fadlRepository,
            )
        }


        /** Initializer for [ThekrEditViewModel] */
        initializer {
            ThekrEditViewModel(
                this.createSavedStateHandle(),
               JvmApplication.container.thekrRepository,
            )
        }


        /** Initializer for [ThekrEntryViewModel] */
        initializer {
            ThekrEntryViewModel(
               JvmApplication.container.thekrRepository,
               JvmApplication.container.thekrInstanceRepository,
            )
        }

        /** Initializer for [ThekrCounterViewModel] */
        initializer {
            ThekrCounterViewModel(
                this.createSavedStateHandle(),
               JvmApplication.container.thekrRepository,
               JvmApplication.container.countRepository,
               JvmApplication.container.countMissRepository,
            )
        }
    }
}