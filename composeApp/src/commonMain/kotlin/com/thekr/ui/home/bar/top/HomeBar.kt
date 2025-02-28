package com.thekr.ui.home.bar.top

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.ui.AppActions
import com.thekr.ui.component.bar.AppTopBar
import com.thekr.ui.home.HomeActions
import com.thekr.ui.home.HomeTab
import com.thekr.ui.home.tab.sebha.CreateCategoryDialog
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.SettingsRoute
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.viewmodel.AppState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.add_thekr
import com.thekr.resources.back
import com.thekr.resources.create_thekr_group
import com.thekr.resources.settings

@Composable
fun HomeBar(
    settingsDetails: SettingsDetails,
    pagerState: PagerState,
    appState: AppState,
) {
    val headerText= stringResource(HomeTab.entries[pagerState.currentPage].stringResource)
    assert(headerText.isNotEmpty())
    AppTopBar(
        title = {
            HeaderText(
                text = headerText,
            )
        },
        actions = {
            if (pagerState.currentPage == 0) {
                HomeBarCreateAction(appState)
            }
            // settings icon
            IconButton(onClick = {
                NavigationActions.navigate(SettingsRoute.route)
            }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(Res.string.settings),
                    tint = Color.White
                )
            }
        },
        navigationIcon = {
            if (AppActions.canNavigateToPreviousCategory(pagerState.currentPage)) {
                IconButton(onClick = {
                    AppActions.navigateToParentCategory(pagerState.currentPage)
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = stringResource(Res.string.back),
                        tint = Color.White
                    )
                }
            }
        },
        headerTabsRow = {
            HeaderTabsRow(pagerState)
        },
        secondaryHeader = {
            SearchUi(settingsDetails)
        },
        settingsDetails = settingsDetails,
    )
}

@Composable
fun HomeBarCreateAction(
    appState: AppState,
) {
    val showCreateNewCategoryDialog = remember { mutableStateOf(false) }
    IconButton(
        onClick = if (appState.userThekr.value.childCategories.isEmpty()) {
            { showCreateNewCategoryDialog.value = true }
        } else {
            { HomeActions.onThekrCategoryClick() }
        }
    ) {
        Icon(
            Icons.Outlined.Create,
            contentDescription = if (appState.userThekr.value.childCategories.isEmpty()) {
                stringResource(Res.string.create_thekr_group)
            } else stringResource(Res.string.add_thekr),
            tint = Color.White
        )
    }

    CreateCategoryDialog(
        showCreateCategoryDialog = showCreateNewCategoryDialog,
    )
}


@Preview
@Composable
fun HomeBarPreview() {
    AppTheme {
        Surface {
            LocalizedApp {
                HomeBar(
                    pagerState = rememberPagerState(pageCount = { 3 }),
                    settingsDetails = SettingsDetails(),
                    appState = AppState(),
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeBarPreviewDark() {
    AppTheme(ThemeMode.Dark) {
        Surface {
            LocalizedApp {
                HomeBar(
                    pagerState = rememberPagerState(pageCount = { 3 }),
                    settingsDetails = SettingsDetails(),
                    appState = AppState(),
                )
            }
        }
    }
}