package com.thekr.ui.bar.top

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.resources.Res
import com.thekr.resources.back
import com.thekr.resources.header_background
import com.thekr.resources.settings
import com.thekr.ui.component.RtlView
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
import com.thekr.ui.theme.ZekrTheme
import com.thekr.ui.values.SDimensions.sTiny
import com.thekr.ui.viewmodel.AzkarState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ZekrBar(
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    navigationIcon: @Composable () -> Unit = {},
    headerTabsRow: @Composable (() -> Unit)? = null,
    secondaryHeader: @Composable (() -> Unit)? = null,
) {
    val zekrColors = ZekrTheme.colors(settingsDetails)
    val localDensity = LocalDensity.current
    var secondaryHeaderHeight by remember { mutableStateOf(0.dp) }


    ConstraintLayout(modifier = modifier) {
        val (content, trdRow) = createRefs()
        val sTiny = sTiny

        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            zekrColors.mainHeaderBackgroundStart,
                            zekrColors.mainHeaderBackgroundEnd,
                            zekrColors.mainHeaderBackgroundEnd,
                        )
                    )
                )
                .paint(
                    painterResource(Res.drawable.header_background),
                    colorFilter = ColorFilter.tint(zekrColors.mainHeaderBackgroundImageTint),
                    contentScale = ContentScale.Inside,
                    repeat = PaintingRepeat.Repeat,
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            zekrColors.mainHeaderBackgroundEnd.copy(alpha = 0.5f),
                            zekrColors.mainHeaderBackgroundEnd.copy(alpha = 0.7f),
                        ),
                    )
                )
                .constrainAs(content) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            CenterAlignedTopAppBar(
                navigationIcon = navigationIcon,
                actions = actions,
                title = title,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = zekrColors.onMainHeader,
                    titleContentColor = zekrColors.onMainHeader,
                    actionIconContentColor = zekrColors.onMainHeader
                ),
            )
            if (headerTabsRow != null) headerTabsRow()
        }

        Box(
            modifier = Modifier
                .constrainAs(trdRow) {
                    top.linkTo(content.bottom, margin = sTiny)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .onGloballyPositioned { coordinates ->
                    secondaryHeaderHeight = with(localDensity) { coordinates.size.height.toDp() }
                }
        ) {
            secondaryHeader?.let { it() }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ZekrBarPreviewTemplate(
    azkarState: AzkarState = AzkarState(),
    pagerState: PagerState = rememberPagerState(pageCount = { HomeTab.entries.size }),
    settingsDetails: SettingsDetails = SettingsDetails(),
) {
    AppTheme(ThemeMode.Dark) {
        Surface {
            RtlView {
                ZekrBar(
                    title = {
                        HeaderText(
                            text = "أذكار",
                        )
                    },
                    actions = {
                        HomeBarCreateAction(azkarState)
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
    }
}