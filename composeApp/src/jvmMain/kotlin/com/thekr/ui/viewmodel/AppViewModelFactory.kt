package com.thekr.ui.viewmodel

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.thekr.JvmApplication
import com.thekr.ui.counter.viewmodel.ZekrCounterViewModel
import com.thekr.ui.zekr.edit.ZekrEditViewModel
import com.thekr.ui.zekr.entry.ZekrEntryViewModel

actual class AppViewModelFactory {
    actual val factory = viewModelFactory {

        /** Initializer for [AzkarViewModel] */
        initializer {
            AzkarViewModel(
               JvmApplication.container.zekrRepository,
               JvmApplication.container.zekrInstanceRepository,
               JvmApplication.container.countRepository,
               JvmApplication.container.countMissRepository,
               JvmApplication.container.categoryRepository,
               JvmApplication.container.fadlRepository,
            )
        }


        /** Initializer for [ZekrEditViewModel] */
        initializer {
            ZekrEditViewModel(
                this.createSavedStateHandle(),
               JvmApplication.container.zekrRepository,
            )
        }


        /** Initializer for [ZekrEntryViewModel] */
        initializer {
            ZekrEntryViewModel(
               JvmApplication.container.zekrRepository,
               JvmApplication.container.zekrInstanceRepository,
            )
        }

        /** Initializer for [ZekrCounterViewModel] */
        initializer {
            ZekrCounterViewModel(
                this.createSavedStateHandle(),
               JvmApplication.container.zekrRepository,
               JvmApplication.container.countRepository,
               JvmApplication.container.countMissRepository,
            )
        }
    }
}