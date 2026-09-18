package com.thekr.ui
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.thekr.data.proto.Settings
import com.thekr.data.settingsStore
import com.thekr.ui.component.LoadScreen
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.navigation.CounterNavyHost
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.viewmodel.AppStateHolder
import com.thekr.ui.viewmodel.AppViewModel
import com.thekr.ui.viewmodel.AppViewModelProvider
import com.thekr.util.changeLang
import com.thekr.util.reportFullyDrawnAnchor

@Composable
fun CounterApp(
    appViewModel: AppViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    // important for the first render
    var currentLang by rememberSaveable { mutableStateOf("") }

    val settings by settingsStore.updates.collectAsState(Settings())

    LaunchedEffect(Unit) {
        AppActions.initActions(appViewModel)
    }

    // AppState is not read in composition here: every field the UI renders
    // is a snapshot-state member of AppState mutated in place, and the only
    // emission-carried field (currentViewedSebhaCategory) is consumed by
    // event handlers through AppStateHolder. This Unit-keyed collector
    // delivers every emission to the holder immediately — no root
    // recomposition per push, and no one-frame holder lag.
    LaunchedEffect(appViewModel) {
        appViewModel.appState.collect { state ->
            AppStateHolder.updateState(state)
        }
    }

    // Belt-and-suspenders durability for the batched count-tap writes: when
    // the app stops, flush the buffer so counts tapped in the final debounce
    // window are durable before the process can be killed. The buffer also
    // self-flushes on its debounce/batch-limit/tick triggers.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, appViewModel) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                appViewModel.flushCountWrites()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        NavigationActions.initNavController(navController)
    }

    val initialized = appViewModel.initialized
    if (settings == null ||
        settings!!.initialized.not() ||
        settings!!.dbInitialized.not() ||
        initialized.value.not()
    ) {
        LoadScreen()
        return
    }

    val settingsDetails = settings!!.toSettingsDetails()

    // Re-run the locale switch only when the language changes — not on
    // every settings emission (font size, theme, etc.).
    LaunchedEffect(settingsDetails.language) {
        changeLang(settingsDetails.language)
        if (currentLang != settingsDetails.language) {
            currentLang = settingsDetails.language
        }
    }

    if (currentLang.isEmpty()) {
        LoadScreen()
        return
    }

    // TTFD anchor: fires exactly once when the LoadScreen -> content
    // transition first composes (both the initialized/db gates and the
    // language gate have passed). Android-only effect — Activity.reportFullyDrawn.
    reportFullyDrawnAnchor()

    AppTheme(
        themeMode = settingsDetails.themeMode,
    ) {
        LocalizedApp(
            language = currentLang,
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                CounterNavyHost(
                    settingsDetails = settingsDetails,
                    appState = appViewModel.appState,
                    navController = navController,
                )
            }
        }
    }
}
