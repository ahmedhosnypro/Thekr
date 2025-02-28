package com.thekr.ui.component.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.thekr.data.settings.SettingsDetails
import com.thekr.resources.Res
import com.thekr.resources.back
import com.thekr.resources.header_background
import com.thekr.resources.settings
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.home.HomeTab
import com.thekr.ui.home.bar.top.HeaderTabsRow
import com.thekr.ui.home.bar.top.HeaderText
import com.thekr.ui.home.bar.top.HomeBarCreateAction
import com.thekr.ui.home.bar.top.SearchUi
import com.thekr.ui.modifier.PaintingRepeat
import com.thekr.ui.modifier.paint
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.SettingsRoute
import com.thekr.ui.theme.AppTheme
import com.thekr.values.SDimensions.sTiny
import com.thekr.ui.viewmodel.AppState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppTopBar(
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    navigationIcon: @Composable () -> Unit = {},
    headerTabsRow: @Composable (() -> Unit)? = null,
    secondaryHeader: @Composable (() -> Unit)? = null,
) {
    val appColors = AppTheme.colors(settingsDetails)
    val localDensity = LocalDensity.current
    var secondaryHeaderHeight by remember { mutableStateOf(0.dp) }

    val sTiny = sTiny

    Column(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        appColors.mainHeaderBackgroundStart,
                        appColors.mainHeaderBackgroundEnd,
                        appColors.mainHeaderBackgroundEnd,
                    )
                )
            )
            .paint(
                painterResource(Res.drawable.header_background),
                colorFilter = ColorFilter.tint(appColors.mainHeaderBackgroundImageTint),
                contentScale = ContentScale.Inside,
                repeat = PaintingRepeat.Repeat,
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        appColors.mainHeaderBackgroundEnd.copy(alpha = 0.5f),
                        appColors.mainHeaderBackgroundEnd.copy(alpha = 0.7f),
                    ),
                )
            )
    ) {
        CenterAlignedTopAppBar(
            navigationIcon = navigationIcon,
            actions = actions,
            title = title,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                navigationIconContentColor = appColors.onMainHeader,
                titleContentColor = appColors.onMainHeader,
                actionIconContentColor = appColors.onMainHeader
            ),
        )
        if (headerTabsRow != null) headerTabsRow()
        Box(
            modifier = Modifier.padding(top = sTiny)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = secondaryHeaderHeight / 2)
                    .heightIn(min = secondaryHeaderHeight / 2, max = secondaryHeaderHeight / 2)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                    )
            )
            Box(
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        secondaryHeaderHeight =
                            with(localDensity) { coordinates.size.height.toDp() }
                    }
            ) {
                secondaryHeader?.let { it() }
            }
        }
    }
}

@Composable
fun AppBarPreviewTemplate(
    appState: AppState = AppState(),
    pagerState: PagerState = rememberPagerState(pageCount = { HomeTab.entries.size }),
    settingsDetails: SettingsDetails = SettingsDetails(),
) {
    LocalizedApp(
        language = Locale.current.language
    ) {
        AppTopBar(
            title = {
                HeaderText(
                    text = "أذكار",
                )
            },
            actions = {
                HomeBarCreateAction(appState)
                // settings icon
                IconButton(onClick = {
//                            NavigationActions.navigate(SettingsRoute)
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
                IconButton(onClick = {}) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = stringResource(Res.string.back),
                        tint = Color.White
                    )
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
}