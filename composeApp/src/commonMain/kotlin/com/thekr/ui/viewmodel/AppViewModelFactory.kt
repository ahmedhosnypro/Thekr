package com.thekr.ui.viewmodel

import androidx.lifecycle.ViewModelProvider

expect class AppViewModelFactory() {
    val factory: ViewModelProvider.Factory
}