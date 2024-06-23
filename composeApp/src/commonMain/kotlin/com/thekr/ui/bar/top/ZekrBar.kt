package com.thekr.ui.bar.top

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import com.thekr.data.settings.SettingsDetails
import com.thekr.ui.values.Dimensions.xLarge
import com.thekr.ui.home.HomeTab
import com.thekr.ui.home.bar.top.HeaderTabsRow
import com.thekr.ui.home.bar.top.HeaderText
import com.thekr.ui.home.bar.top.HomeBarCreateAction
import com.thekr.ui.home.bar.top.SearchUi
import com.thekr.ui.modifier.drawWithContentIfReady
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.SettingsRoute
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.theme.ZekrColors
import com.thekr.ui.theme.ZekrTheme
import com.thekr.ui.component.RtlView
import com.thekr.ui.viewmodel.AzkarState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.back
import com.thekr.resources.header_background
import com.thekr.resources.settings

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
    val localDensity = LocalDensity.current
    val zekrColors = ZekrTheme.colors(settingsDetails)
    var shouldDraw by remember { mutableStateOf(false) }

    // Combine header height calculation and padding logic
    var headerHeight by remember { mutableStateOf(0.dp) }
    val secondaryHeaderTopPadding =
        remember(headerHeight) {
            (headerHeight - xLarge + 4.dp).coerceIn(0.dp, headerHeight)
        }

    Box(modifier = modifier) {


        Image(
            painterResource(Res.drawable.header_background),
            contentDescription = null,
            modifier = Modifier.backgroundImageModifier(
                zekrColors = zekrColors,
                headerHeight = headerHeight,
                shouldDraw = shouldDraw,
            ),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(zekrColors.mainHeaderBackgroundImageTint)
        )

        Column(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    headerHeight = with(localDensity) { coordinates.size.height.toDp() }
                    shouldDraw = true
                }
                .padding(
                    bottom = if (headerTabsRow != null || secondaryHeader != null) xLarge
                    else 0.dp
                )
                .drawWithContentIfReady(shouldDraw),
        ) {
            // Top App Bar
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

            // Optional Header Elements
            AnimatedVisibility(
                shouldDraw,
                enter = EnterTransition.None,
            ) {
                if (headerTabsRow != null)
                    headerTabsRow?.let { it() }
            }
        }

        AnimatedVisibility(
            shouldDraw,
            enter = EnterTransition.None,
        ) {
            Box(Modifier.padding(top = secondaryHeaderTopPadding)) {
                secondaryHeader?.let { it() }
            }
        }
    }
}

// Background Image - Extract Modifier for reusability
private fun Modifier.backgroundImageModifier(
    zekrColors: ZekrColors,
    headerHeight: Dp,
    shouldDraw: Boolean,
) = this
    .fillMaxWidth()
    .height(headerHeight)
    .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                zekrColors.mainHeaderBackgroundStart,
                zekrColors.mainHeaderBackgroundEnd,
                zekrColors.mainHeaderBackgroundEnd,
            )
        )
    )
    .drawWithContentIfReady(shouldDraw) {
        drawContent()
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    zekrColors.mainHeaderBackgroundEnd.copy(alpha = 0.5f),
                    zekrColors.mainHeaderBackgroundEnd.copy(alpha = 0.7f),
                ),
            )
        )
    }

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun ZekrBarPreviewTemplate(
    azkarState: AzkarState = AzkarState(),
    pagerState: PagerState = rememberPagerState(pageCount = { HomeTab.entries.size }),
    settingsDetails: SettingsDetails = SettingsDetails(),
) {

    AppTheme {
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
