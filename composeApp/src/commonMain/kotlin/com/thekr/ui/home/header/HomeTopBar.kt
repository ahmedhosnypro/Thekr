package com.thekr.ui.home.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
import androidx.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.add_thekr
import com.thekr.resources.app_name
import com.thekr.resources.back
import com.thekr.resources.create_thekr_group
import com.thekr.resources.settings
import com.thekr.ui.component.bar.HeaderText
import com.thekr.ui.theme.hacenTunisia
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeTopBar(
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
                HomeTopBarCreateAction(appState)
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
        secondRow = {
            HeaderTabsRow(pagerState)
        },
        thirdRow = {
            AppName(settingsDetails)
        },
        settingsDetails = settingsDetails,
    )
}

@Composable
fun HomeTopBarCreateAction(
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

@Composable
fun AppName(
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
) {
    TopBarHeaderControls(
        settingsDetails = settingsDetails,
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            fontFamily = hacenTunisia(),
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun PagerTabsIconOnly(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    HomeTab.entries.forEachIndexed { index, tab ->
        val selected = (index == pagerState.currentPage)
        Surface(
            modifier = modifier,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }, color = Color.Transparent
        ) {
            Icon(
                painterResource(tab.iconRes),
                contentDescription = null, tint = if (selected) Color.White
                else Color.White.copy(alpha = .7f), modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun HeaderTabsRow(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val width = maxWidth
        ScrollableTabRow(selectedTabIndex = minOf(HomeTab.entries.size, pagerState.currentPage),
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .background(
                    color = Color(0x0DFFFFFF)
                ),
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(
                        tabPositions[pagerState.currentPage]
                    ),
                    color = Color.White
                )
            },
            divider = {}) {
            PagerTabsIconOnly(
                pagerState, modifier = Modifier.width(width / HomeTab.entries.size)
            )
        }
    }
}

@Preview
@Composable
fun HomeBarPreview() {
    AppTheme {
        Surface {
            LocalizedApp {
                HomeTopBar(
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
                HomeTopBar(
                    pagerState = rememberPagerState(pageCount = { 3 }),
                    settingsDetails = SettingsDetails(),
                    appState = AppState(),
                )
            }
        }
    }
}