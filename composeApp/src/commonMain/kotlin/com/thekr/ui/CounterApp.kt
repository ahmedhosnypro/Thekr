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
import com.thekr.ui.viewmodel.AzkarViewModel
import androidx.compose.runtime.getValue
import com.thekr.data.proto.Settings
import com.thekr.data.settingsStore
import com.thekr.ui.component.LoadScreen
import com.thekr.ui.component.MultiLang
import com.thekr.ui.viewmodel.AppViewModelProvider
import com.thekr.ui.viewmodel.AzkarStateHelper

@Composable
fun CounterApp(
    settingViewModel: SettingViewModel = viewModel(factory = AppViewModelProvider.Factory),
    azkarViewModel: AzkarViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    SettingsHelper.setViewModel(settingViewModel)
    val settings by settingsStore.updates.collectAsState(Settings())
    if (settings == null) {
        LoadScreen()
        return
    }
    val azkarState by azkarViewModel.azkarState.collectAsState()
    LaunchedEffect(Unit) {
        AzkarActions.initActions(azkarViewModel)
    }
    val settingsDetails = settings!!.toSettingsDetails()
    LaunchedEffect(settings) {
        SettingsHelper.updateState(settingsDetails)
    }
    LaunchedEffect(azkarState) {
        AzkarStateHelper.updateState(azkarState)
    }

    // Apply settings changes using a LaunchedEffect
    LaunchedEffect(settings) {
        SettingsHelper.updateState(settingsDetails)
    }

    if (settingsDetails.initialized.not()) {
        LoadScreen()
    } else {
        val language = settings!!.language
        AppTheme(
            themeMode = settings!!.themeMode,
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