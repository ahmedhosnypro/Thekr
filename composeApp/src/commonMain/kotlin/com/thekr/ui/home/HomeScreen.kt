@file:OptIn(InternalVoyagerApi::class)
// @Composable functions are PascalCase per the Compose API guidelines (detekt
// exempts them via naming.FunctionNaming ignoreAnnotated; ktlint's
// function-naming rule has no working equivalent in this setup).
@file:Suppress("ktlint:standard:function-naming")

package com.thekr.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.internal.BackHandler
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.ui.AppActions
import com.thekr.ui.AppActions.canNavigateToPreviousCategory
import com.thekr.ui.AppActions.navigateToParentCategory
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.home.header.HomeTopBar
import com.thekr.ui.home.list.DuaTab
import com.thekr.ui.home.tab.ThekrTab
import com.thekr.ui.home.tab.sebha.SebhaTab
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.viewmodel.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// stack of cat nav
@Composable
fun HomeScreen(
    appState: StateFlow<AppState>,
    settingsDetails: SettingsDetails,
    homeViewModel: HomeViewModel = viewModel { HomeViewModel() },
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val uiCoroutine = rememberCoroutineScope()

    // HomeActions hold composition-scoped references for the lifetime of
    // this entry: registered before the content composes and cleared on
    // dispose, so a post-dispose invocation (e.g. a snackbar delivered by an
    // in-flight HomeViewModel coroutine) no-ops instead of launching on the
    // cancelled composition scope, and the HomeViewModel is not pinned past
    // its owner.
    DisposableEffect(homeViewModel) {
        HomeActions.initActions(
            homeViewModel = homeViewModel,
            uiCoroutine = uiCoroutine,
            snackBarHostState = snackBarHostState,
        )
        onDispose { HomeActions.clearActions() }
    }

    HomeContent(
        appState = appState,
        settingsDetails = settingsDetails,
        snackBarHostState = snackBarHostState,
    )
}

@OptIn(ExperimentalMaterial3Api::class, InternalVoyagerApi::class)
@Composable
private fun HomeContent(
    settingsDetails: SettingsDetails,
    appState: StateFlow<AppState>,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    // Leaf-scope read: the flow is deliberately NOT collected here. Every
    // AppState field this subtree renders (userThekr, the tab stacks) is a
    // snapshot-state member mutated in place, so reads below stay live via
    // the snapshot system and a new AppState instance — emitted on the sebha
    // sub-tab swipe — recomposes nothing. The only emission-carried field,
    // currentViewedSebhaCategory, is consumed by event handlers through
    // AppStateHolder, which CounterApp feeds every emission directly.
    val appStateValue = appState.value
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val pagerState = rememberPagerState(pageCount = { HomeTab.entries.size })

    // BackHandler moved inside HomeContent composable
    if (canNavigateToPreviousCategory(pagerState.currentPage)) {
        BackHandler(true) {
            navigateToParentCategory(pagerState.currentPage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            HomeTopBar(
                pagerState = pagerState,
                settingsDetails = settingsDetails,
                appState = appStateValue,
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                state = pagerState,
                userScrollEnabled = true,
            ) { tabIndex ->
                // Display the appropriate tab content based on the selected tab
                when (HomeTab.entries[tabIndex]) {
                    HomeTab.Mesbaha -> SebhaTab(
                        userAzkar = appStateValue.userThekr.value.childCategories,
                        settingsDetails = settingsDetails,
                    )

                    HomeTab.HesnAlMuslim -> ThekrTab(
                        categoryDetails = appStateValue.hesnAlmuslimStack.last(),
                        onCategoryClick = { HomeActions.onCategoryClick(tabIndex, it) },
                        settingsDetails = settingsDetails,
                        tabIndex = tabIndex,
                    )

                    HomeTab.Knooz -> ThekrTab(
                        categoryDetails = appStateValue.knoozStack.last(),
                        onCategoryClick = { HomeActions.onCategoryClick(tabIndex, it) },
                        settingsDetails = settingsDetails,
                        tabIndex = tabIndex,
                    )

                    HomeTab.Dua -> DuaTab(
                        duaStack = appStateValue.duaCategoryStack,
                        onCategoryClick = { HomeActions.onCategoryClick(tabIndex, it) },
                        settingsDetails = settingsDetails,
                        tabIndex = tabIndex,
                    )
                }
            }
        }
    }
}

// Preview functions...
@Preview
@Composable
fun HomeScreenPreview() {
    AppTheme(themeMode = ThemeMode.Dark) {
        Surface {
            LocalizedApp {
                HomeContent(
                    settingsDetails = SettingsDetails(
                        themeMode = ThemeMode.Dark,
                    ),
                    snackBarHostState = SnackbarHostState(),
                    appState = MutableStateFlow(AppState()),
                )
            }
        }
    }
}

@Preview
@Composable
fun EmptyHomeScreenPreview() {
    AppTheme(ThemeMode.Dark) {
        LocalizedApp {
            Surface {
                with(AppActions) {
                    canNavigateToPreviousCategory = { true }
                }
                HomeContent(
                    settingsDetails = SettingsDetails(
                        themeMode = ThemeMode.Dark,
                    ),
                    snackBarHostState = SnackbarHostState(),
                    appState = MutableStateFlow(AppState()),
                )
            }
        }
    }
}
