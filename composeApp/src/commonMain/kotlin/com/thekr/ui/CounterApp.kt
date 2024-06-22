package com.thekr.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thekr.data.settings.SettingsHelper
import com.thekr.ui.navigation.CounterNavyHost
import com.thekr.ui.settings.SettingViewModel
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.util.MultiLang
import com.thekr.ui.viewmodel.AzkarViewModel
import androidx.compose.runtime.getValue
import com.thekr.ui.component.LoadScreen
import com.thekr.ui.viewmodel.AppViewModelProvider
import com.thekr.ui.viewmodel.AzkarStateHelper

@Composable
fun CounterApp(
    settingViewModel: SettingViewModel = viewModel(factory = AppViewModelProvider.Factory),
    azkarViewModel: AzkarViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    SettingsHelper.setViewModel(settingViewModel)
    val settingsDetails by settingViewModel.viewState
    val azkarState by azkarViewModel.azkarState.collectAsState()

    LaunchedEffect(Unit) {
        AzkarActions.initActions(azkarViewModel)
    }

    LaunchedEffect(azkarState) {
        AzkarStateHelper.updateState(azkarState)
    }

    // Apply settings changes using a LaunchedEffect
    LaunchedEffect(settingsDetails) {
        SettingsHelper.updateState(settingsDetails)
    }

    if (settingsDetails.initialized.not()) {
        LoadScreen()
    } else {
        val language = settingsDetails.language
        AppTheme(
            themeMode = settingsDetails.themeMode,
        ) {
            MultiLang(
                language = language,
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CounterNavyHost(
                        settingsDetails = settingsDetails,
                        azkarState = azkarState,
                    )
                }
            }
        }
    }
}