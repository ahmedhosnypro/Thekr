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
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.viewmodel.AppViewModelProvider
import com.thekr.ui.viewmodel.AzkarStateHelper
import com.thekr.util.changeLang
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun CounterApp(
    azkarViewModel: AzkarViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    // important for the first render
    var currentLang by rememberSaveable { mutableStateOf("") }

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

    LaunchedEffect(settingsDetails) {
        changeLang(settingsDetails.language)
        if (currentLang != settingsDetails.language) {
            currentLang = settingsDetails.language
        }
    }

    if (currentLang.isEmpty()) {
        LoadScreen()
        return
    }

    AppTheme(
        themeMode = settingsDetails.themeMode,
    ) {
        LocalizedApp(
            language = currentLang,
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