package com.thekr.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thekr.ui.navigation.CounterNavyHost
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.viewmodel.AzkarViewModel
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.thekr.data.proto.Settings
import com.thekr.data.settingsStore
import com.thekr.ui.component.LoadScreen
import com.thekr.ui.component.MultiLang
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.viewmodel.AppViewModelProvider
import com.thekr.ui.viewmodel.AzkarStateHelper

@Composable
fun CounterApp(
    azkarViewModel: AzkarViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val settings by settingsStore.updates.collectAsState(Settings())
    val azkarState by azkarViewModel.azkarState.collectAsState()

    LaunchedEffect(Unit) {
        AzkarActions.initActions(azkarViewModel)
    }

    LaunchedEffect(azkarState) {
        AzkarStateHelper.updateState(azkarState)
    }

    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        NavigationActions.initNavController(navController)
    }

    // todo:
    val initialized = azkarViewModel.initialized
    if (settings == null ||
        settings!!.initialized.not() ||
        settings!!.dbInitialized.not() ||
        initialized.value.not()
    ) {
        LoadScreen()
        return
    }

    val settingsDetails = settings!!.toSettingsDetails()

    AppTheme(
        themeMode = settingsDetails.themeMode,
    ) {
        MultiLang(
            language = settingsDetails.language,
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                CounterNavyHost(
                    settingsDetails = settingsDetails,
                    azkarState = azkarState,
                    navController = navController,
                )
            }
        }
    }
}