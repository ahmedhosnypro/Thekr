@file:OptIn(ExperimentalFoundationApi::class, InternalVoyagerApi::class)

package com.thekr.ui.home


import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.internal.BackHandler
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.ui.AzkarActions
import com.thekr.ui.AzkarActions.canNavigateToPreviousCategory
import com.thekr.ui.AzkarActions.navigateToParentCategory
import com.thekr.ui.home.bar.top.HomeBar
import com.thekr.ui.home.list.DuaTab
import com.thekr.ui.home.tab.ZekrTab
import com.thekr.ui.home.tab.sebha.SebhaTab
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.util.RtlView
import com.thekr.ui.viewmodel.AzkarState
import org.jetbrains.compose.ui.tooling.preview.Preview


// stack of cat nav
@Composable
fun HomeScreen(
    azkarState: AzkarState,
    settingsDetails: SettingsDetails,
    // todo: fun MyComposable(viewModel: MyViewModel = viewModel { MyViewModel() })
    homeViewModel: HomeViewModel = viewModel(),
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val uiCoroutine = rememberCoroutineScope()

    // Define HomeActions within HomeScreen composable
    LaunchedEffect(Unit) {
        HomeActions.initActions(
            homeViewModel = homeViewModel,
            uiCoroutine = uiCoroutine,
            snackBarHostState = snackBarHostState
        )
    }

    HomeContent(
        azkarState = azkarState,
        settingsDetails = settingsDetails,
        snackBarHostState = snackBarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    settingsDetails: SettingsDetails,
    azkarState: AzkarState,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
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
            HomeBar(
                pagerState = pagerState,
                settingsDetails = settingsDetails,
                azkarState = azkarState,
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                state = pagerState,
                userScrollEnabled = true
            ) { tabIndex ->
                // Display the appropriate tab content based on the selected tab
                when (HomeTab.entries[tabIndex]) {
                    HomeTab.Mesbaha -> SebhaTab(
                        userAzkar = azkarState.userAzkar.value.childCategories,
                        settingsDetails = settingsDetails,
                    )

                    HomeTab.HesnAlMuslim -> ZekrTab(
                        categoryDetails = azkarState.hesnAlmuslimStack.last(),
                        onZekrClick = HomeActions.onZekrClick,
                        onCategoryClick = { HomeActions.onCategoryClick(tabIndex, it) },
                        settingsDetails = settingsDetails,
                        tabIndex = tabIndex,
                    )

                    HomeTab.Knooz -> ZekrTab(
                        categoryDetails = azkarState.knoozStack.last(),
                        onZekrClick = HomeActions.onZekrClick,
                        onCategoryClick = { HomeActions.onCategoryClick(tabIndex, it) },
                        settingsDetails = settingsDetails,
                        tabIndex = tabIndex,
                    )

                    HomeTab.Dua -> DuaTab(
                        duaStack = azkarState.duaCategoryStack,
                        onCategoryClick = { HomeActions.onCategoryClick(tabIndex, it) },
                        onZekrClick = HomeActions.onZekrClick,
                        settingsDetails = settingsDetails,
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
            RtlView {
                HomeContent(
                    settingsDetails = SettingsDetails(
                        themeMode = ThemeMode.Dark,
                    ),
                    snackBarHostState = SnackbarHostState(),
                    azkarState = AzkarState(),
                )
            }
        }
    }
}


@Preview
@Composable
fun EmptyHomeScreenPreview() {
    AppTheme(ThemeMode.Dark) {
        RtlView {
            Surface {
                with(AzkarActions) {
                    canNavigateToPreviousCategory = { true }
                }
                HomeContent(
                    settingsDetails = SettingsDetails(
                        themeMode = ThemeMode.Dark,
                    ),
                    snackBarHostState = SnackbarHostState(),
                    azkarState = AzkarState(),
                )
            }
        }
    }
}