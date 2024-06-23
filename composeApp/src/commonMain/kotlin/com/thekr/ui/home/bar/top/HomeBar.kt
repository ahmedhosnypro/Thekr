@file:OptIn(ExperimentalFoundationApi::class)

package com.thekr.ui.home.bar.top

import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.thekr.ui.AzkarActions
import com.thekr.ui.bar.top.ZekrBar
import com.thekr.ui.home.HomeActions
import com.thekr.ui.home.HomeTab
import com.thekr.ui.home.tab.sebha.CreateCategoryDialog
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.SettingsRoute
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.component.RtlView
import com.thekr.ui.viewmodel.AzkarState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.add_zekr
import com.thekr.resources.back
import com.thekr.resources.create_zekr_group
import com.thekr.resources.settings

@Composable
fun HomeBar(
    settingsDetails: SettingsDetails,
    pagerState: PagerState,
    azkarState: AzkarState,
) {
    ZekrBar(
        title = {
            HeaderText(
                text = stringResource(HomeTab.entries[pagerState.currentPage].stringResource),
            )
        },
        actions = {
            if (pagerState.currentPage == 0) {
                HomeBarCreateAction(azkarState)
            }
            // settings icon
            IconButton(onClick = {
                NavigationActions.navigate(SettingsRoute.route)
//                NavigationActions.navigate(SettingsRoute)
            }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(Res.string.settings),
                    tint = Color.White
                )
            }
        },
        navigationIcon = {
            if (AzkarActions.canNavigateToPreviousCategory(pagerState.currentPage)) {
                IconButton(onClick = {
                    AzkarActions.navigateToParentCategory(pagerState.currentPage)
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
    azkarState: AzkarState,
) {
    val showCreateNewCategoryDialog = remember { mutableStateOf(false) }
    IconButton(
        onClick = if (azkarState.userAzkar.value.childCategories.isEmpty()) {
            { showCreateNewCategoryDialog.value = true }
        } else HomeActions.onCreateZekrClick
    ) {
        Icon(
            Icons.Outlined.Create,
            contentDescription = if (azkarState.userAzkar.value.childCategories.isEmpty()) {
                stringResource(Res.string.create_zekr_group)
            } else stringResource(Res.string.add_zekr),
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
            RtlView {
                HomeBar(
                    pagerState = rememberPagerState(pageCount = { 3 }),
                    settingsDetails = SettingsDetails(),
                    azkarState = AzkarState(),
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
            RtlView {
                HomeBar(
                    pagerState = rememberPagerState(pageCount = { 3 }),
                    settingsDetails = SettingsDetails(),
                    azkarState = AzkarState(),
                )
            }
        }
    }
}