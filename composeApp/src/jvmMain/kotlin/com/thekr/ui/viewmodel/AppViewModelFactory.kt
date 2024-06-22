package com.thekr.ui.viewmodel

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.thekr.ThekrApplication
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
               ThekrApplication.container.zekrRepository,
               ThekrApplication.container.zekrInstanceRepository,
               ThekrApplication.container.countRepository,
               ThekrApplication.container.countMissRepository,
               ThekrApplication.container.categoryRepository,
               ThekrApplication.container.fadlRepository,
            )
        }


        /** Initializer for [ZekrEditViewModel] */


        /** Initializer for [ZekrEditViewModel] */
        initializer {
            ZekrEditViewModel(
                this.createSavedStateHandle(),
               ThekrApplication.container.zekrRepository,
            )
        }

        /** Initializer for [ZekrEntryViewModel] */

        /** Initializer for [ZekrEntryViewModel] */
        initializer {
            ZekrEntryViewModel(
               ThekrApplication.container.zekrRepository,
               ThekrApplication.container.zekrInstanceRepository,
            )
        }

        /** Initializer for [ZekrCounterViewModel] */

        /** Initializer for [ZekrCounterViewModel] */
        initializer {
            ZekrCounterViewModel(
                this.createSavedStateHandle(),
               ThekrApplication.container.zekrRepository,
               ThekrApplication.container.countRepository,
               ThekrApplication.container.countMissRepository,
            )
        }

        /** Initializer for [SettingViewModel] */

        /** Initializer for [SettingViewModel] */
        initializer {
            SettingViewModel()
        }
    }
}