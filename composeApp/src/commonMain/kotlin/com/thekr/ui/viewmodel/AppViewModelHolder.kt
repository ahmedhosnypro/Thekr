package com.thekr.ui.viewmodel

/**
 * Holds the process-lifetime [AppViewModel] so ViewModels created separately
 * (e.g. HomeViewModel) can reach the lazy-fetch gate,
 * [AppViewModel.ensureCategoryFetched].
 */
object AppViewModelHolder {
    lateinit var appViewModel: AppViewModel
        internal set

    val isAvailable: Boolean
        get() = this::appViewModel.isInitialized
}
