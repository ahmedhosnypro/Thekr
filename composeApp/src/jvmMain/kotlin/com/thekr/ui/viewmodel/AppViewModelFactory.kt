package com.thekr.ui.viewmodel

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.thekr.JvmApplication
import com.thekr.ui.counter.viewModel.ZekrCounterViewModel
import com.thekr.ui.settings.SettingViewModel
import com.thekr.ui.zekr.edit.ZekrEditViewModel
import com.thekr.ui.zekr.entry.ZekrEntryViewModel

actual class AppViewModelFactory {
    actual val factory = viewModelFactory {

        /** Initializer for [AzkarViewModel] */
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


        /** Initializer for [ZekrEditViewModel] */
        initializer {
            ZekrEditViewModel(
                this.createSavedStateHandle(),
               JvmApplication.container.zekrRepository,
            )
        }

        /** Initializer for [ZekrEntryViewModel] */

        /** Initializer for [ZekrEntryViewModel] */
        initializer {
            ZekrEntryViewModel(
               JvmApplication.container.zekrRepository,
               JvmApplication.container.zekrInstanceRepository,
            )
        }

        /** Initializer for [ZekrCounterViewModel] */

        /** Initializer for [ZekrCounterViewModel] */
        initializer {
            ZekrCounterViewModel(
                this.createSavedStateHandle(),
               JvmApplication.container.zekrRepository,
               JvmApplication.container.countRepository,
               JvmApplication.container.countMissRepository,
            )
        }

        /** Initializer for [SettingViewModel] */

        /** Initializer for [SettingViewModel] */
        initializer {
            SettingViewModel()
        }
    }
}