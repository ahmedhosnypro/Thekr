@file:OptIn(ExperimentalFoundationApi::class)

package com.thekr.ui.counter.header

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Popup
import com.thekr.data.proto.SwapDirection
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.data.zekr.zekr.ZekrDetails
import com.thekr.model.ZekrTargetStatus
import com.thekr.ui.bar.top.ZekrBar
import com.thekr.ui.counter.CounterHelper
import com.thekr.ui.counter.viewModel.CounterUiState
import com.thekr.ui.values.Dimensions.normal
import com.thekr.ui.values.Dimensions.small
import com.thekr.ui.values.Dimensions.xLarge
import com.thekr.ui.values.Dimensions.xxLarge
import com.thekr.ui.home.bar.top.HeaderControlCard
import com.thekr.ui.home.bar.top.HeaderText
import com.thekr.ui.home.bar.top.TopBarHeaderControls
import com.thekr.ui.home.list.categoryDetailsPreviewState
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.theme.ZekrTheme
import com.thekr.ui.component.RtlView
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.about_zekr
import com.thekr.resources.back
import com.thekr.resources.counter_visibility
import com.thekr.resources.daily
import com.thekr.resources.listen_to_zekr
import com.thekr.resources.monthly
import com.thekr.resources.pause_sound
import com.thekr.resources.play_sound
import com.thekr.resources.session
import com.thekr.resources.settings
import com.thekr.resources.total
import com.thekr.resources.weekly
import com.thekr.resources.yearly
import com.thekr.resources.zekr_list

/** Represents the top app bar for the Zekr counter-screen. */
@Composable
fun ZekrCounterTopBar(
    settingsDetails: SettingsDetails,
    categoryDetails: MutableState<CategoryDetails>,
    counterUiState: CounterUiState,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    if (counterUiState.lockEnabled) {
        UnlockAppBar(counterUiState, settingsDetails)
    } else {
        MainZekrAppBar(settingsDetails, categoryDetails, counterUiState, pagerState, modifier)
    }
}

/**
 * Displays the main Zekr app bar with navigation, actions, and secondary
 * header.
 */
@Composable
private fun MainZekrAppBar(
    settingsDetails: SettingsDetails,
    categoryDetails: MutableState<CategoryDetails>,
    counterUiState: CounterUiState,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    ZekrBar(
        modifier = modifier,
        title = { HeaderText(text = categoryDetails.value.name) },
        actions = { ZekrAppBarActions(counterUiState, categoryDetails) },
        navigationIcon = { ZekrAppBarNavigationIcon() },
        secondaryHeader = {
            ZekrTopBarFeatures(
                settingsDetails = settingsDetails,
                counterUiState = counterUiState,
                pagerState = pagerState
            )
        },
        settingsDetails = settingsDetails,
    )
}

/** Displays the actions within the Zekr app bar. */
@Composable
private fun ZekrAppBarActions(
    counterUiState: CounterUiState,
    categoryDetails: MutableState<CategoryDetails>
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (counterUiState.currentZekrInstance.value.editable) {
            IconButton(onClick = { CounterHelper.onEditClick() }) {
                Icon(
                    imageVector = Icons.Filled.EditNote,
                    contentDescription = stringResource(Res.string.settings),
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        IconButton(onClick = { CounterHelper.showZekrStatistics() }) {
            Icon(
                imageVector = Icons.Filled.StackedBarChart,
                contentDescription = "Statistics"
            )
        }

        if (categoryDetails.value.zekrList.size > 1) {
            IconButton(onClick = {CounterHelper.showCategoryZekrListMenu()}) {
                Icon(
                    imageVector = Icons.Filled.MoreHoriz,
                    contentDescription = stringResource(Res.string.zekr_list),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

/** Displays the navigation icon (back button) in the Zekr app bar. */
@Composable
private fun ZekrAppBarNavigationIcon() {
    IconButton(onClick = { CounterHelper.onNavigateUp() }) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBackIos,
            contentDescription = stringResource(Res.string.back),
        )
    }
}

/** Displays the app bar in a locked state. */
@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun UnlockAppBar(
    counterUiState: CounterUiState,
    settingsDetails: SettingsDetails,
) {
    ZekrBar(
        settingsDetails = settingsDetails,
        title = { },
        actions = {
            Icon(
                Icons.Default.Lock,
                contentDescription = "Lock",
                modifier = Modifier
                    .padding(horizontal = small)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            CounterHelper.updateUiState(
                                counterUiState.copy(lockEnabled = false)
                            )
                        }
                    )
            )
        }
    )
}

/** Groups the features and controls available in the Zekr top bar. */
@Composable
fun ZekrTopBarFeatures(
    settingsDetails: SettingsDetails,
    counterUiState: CounterUiState,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    TopBarHeaderControls(
        settingsDetails = settingsDetails,
        modifier = modifier,
    ) {
        ZekrControlHeader(
            settingsDetails = settingsDetails,
            counterUiState = counterUiState,
            pagerState = pagerState
        )
    }
}

/** Displays the controls and features within the Zekr top bar header. */
@Composable
fun ZekrControlHeader(
    pagerState: PagerState,
    settingsDetails: SettingsDetails,
    counterUiState: CounterUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        BoxWithConstraints(
            modifier = modifier.fillMaxWidth()
        ) {
            val maxWidth = maxWidth
            val width = maxWidth / 6

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val tint = ZekrTheme.colors(settingsDetails).onSecondaryHeader
                CountVisibility(settingsDetails, width, tint)
                SwapDirection(settingsDetails, width, counterUiState)
                Lock(counterUiState, width)
                FontSizeControl(settingsDetails, width)
                SoundPlayer(pagerState, width, counterUiState, tint)
                ThemeMode(settingsDetails, width, tint)
            }
        }
    }
}

/**
 * Displays a button to control the visibility of count settings, and a
 * dropdown menu with options for count visibility customization.
 *
 * @param settingsDetails The settings for the application.
 * @param width The width of the button.
 * @param tint The tint color for the icon.
 */
@Composable
private fun CountVisibility(
    settingsDetails: SettingsDetails,
    width: Dp,
    tint: Color
) {
    val showCountSettingsMenu = remember { mutableStateOf(false) }
    var secondaryHeaderSize by remember { mutableStateOf(DpSize.Zero) }
    val localDensity = LocalDensity.current

    Box {
        IconButton(
            onClick = { showCountSettingsMenu.value = true },
            modifier = Modifier
                .requiredWidth(width)
                .onGloballyPositioned { coordinates ->
                    secondaryHeaderSize = with(localDensity) {
                        coordinates.size.toSize().toDpSize()
                    }
                }
        ) {
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = stringResource(Res.string.counter_visibility),
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }

        AnimatedVisibility(visible = showCountSettingsMenu.value) {
            CountVisibilityDropdownMenu(
                showCountSettingsMenu = showCountSettingsMenu,
                secondaryHeaderSize = secondaryHeaderSize,
                settingsDetails = settingsDetails
            )
        }
    }
}


/**
 * Displays a dropdown menu with options for customizing count visibility.
 *
 * @param showCountSettingsMenu State variable indicating whether the menu
 *     is expanded.
 * @param secondaryHeaderSize The size of the secondary header, used to
 *     position the menu.
 * @param settingsDetails The settings for the application.
 */
@Composable
private fun CountVisibilityDropdownMenu(
    showCountSettingsMenu: MutableState<Boolean>,
    secondaryHeaderSize: DpSize,
    settingsDetails: SettingsDetails
) {
    DropdownMenu(
        expanded = showCountSettingsMenu.value,
        onDismissRequest = { showCountSettingsMenu.value = false },
        offset = DpOffset(x = secondaryHeaderSize.width / 2, y = secondaryHeaderSize.height)
    ) {
        CountVisibilityOptions(settingsDetails)
    }
}

/**
 * Displays the options for customizing count visibility within the
 * dropdown menu.
 *
 * @param settingsDetails The settings for the application.
 */
@Composable
private fun CountVisibilityOptions(settingsDetails: SettingsDetails) {
    val zekrInstanceDetails = CounterHelper.getCurrentZekrInstance().value

    RadioMenuItem(
        text = stringResource(Res.string.counter_visibility),
        onClick = CounterHelper.onClickCountVisibility,
        selected = settingsDetails.showCount
    )

    // Display checkboxes for each count type

    if (zekrInstanceDetails.dailyTargetStatus == ZekrTargetStatus.Enabled) {
        CountVisibilityCheckbox(
            Res.string.daily,
            settingsDetails.showDailyCount,
            settingsDetails.showCount,
            CounterHelper.toggleDailyCountVisibility
        )
    }

    if (zekrInstanceDetails.weeklyTargetStatus == ZekrTargetStatus.Enabled) {
        CountVisibilityCheckbox(
            Res.string.weekly,
            settingsDetails.showWeeklyCount,
            settingsDetails.showCount,
            CounterHelper.toggleWeeklyCountVisibility
        )
    }

    if (zekrInstanceDetails.monthlyTargetStatus == ZekrTargetStatus.Enabled) {
        CountVisibilityCheckbox(
            Res.string.monthly,
            settingsDetails.showMonthlyCount,
            settingsDetails.showCount,
            CounterHelper.toggleMonthlyCountVisibility
        )
    }

    if (zekrInstanceDetails.yearlyTargetStatus == ZekrTargetStatus.Enabled) {
        CountVisibilityCheckbox(
            Res.string.yearly,
            settingsDetails.showYearlyCount,
            settingsDetails.showCount,
            CounterHelper.toggleYearlyCountVisibility
        )
    }


    CountVisibilityCheckbox(
        Res.string.total,
        settingsDetails.showTotalCount,
        settingsDetails.showCount,
        CounterHelper.toggleTotalCountVisibility
    )

    CountVisibilityCheckbox(
        Res.string.session,
        settingsDetails.showSessionCount,
        settingsDetails.showCount,
        CounterHelper.toggleSessionCountVisibility
    )
}

/**
 * Displays a checkbox for a specific count visibility option.
 *
 * @param stringResource The string resource ID for the checkbox label.
 * @param checked Whether the checkbox is checked.
 * @param enabled Whether the checkbox is enabled.
 * @param onClick Callback invoked when the checkbox is clicked.
 */
@Composable
private fun CountVisibilityCheckbox(
    stringResource: StringResource,
    checked: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        modifier = modifier,
        text = { Text(text = stringResource(stringResource)) },
        onClick = onClick,
        trailingIcon = {
            Checkbox(
                checked = checked,
                onCheckedChange = { onClick() },
                enabled = enabled
            )
        },
        enabled = enabled
    )
}

/** A radio button menu item for use in a dropdown menu. */
@Composable
fun RadioMenuItem(
    text: String,
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    DropdownMenuItem(
        modifier = modifier,
        text = { Text(text = text) },
        onClick = onClick,
        trailingIcon = { RadioButton(selected = selected, onClick = onClick) },
        enabled = enabled
    )
}


/** A button for controlling the sound player. */
@Composable
private fun SoundPlayer(
    pagerState: PagerState,
    width: Dp,
    counterUiState: CounterUiState,
    tint: Color
) {
    if (CounterHelper.getZekr(pagerState.currentPage).value.soundFileName != null) {
        IconButton(
            onClick = CounterHelper.onClickSound,
            modifier = Modifier.requiredWidth(width)
        ) {
            HeaderImage(
//                imageRes = if (counterUiState.playState == Player.STATE_IDLE) R.drawable.play_sound else R.drawable.pause_sound,
                drawableResource = if (counterUiState.playState == 0) Res.drawable.play_sound else Res.drawable.pause_sound,
                contentDescription = stringResource(Res.string.listen_to_zekr),
                tint = tint
            )
        }
    }
}

/** A button for changing the swap direction. */
@Composable
private fun SwapDirection(
    settingsDetails: SettingsDetails,
    width: Dp,
    counterUiState: CounterUiState,
) {
    if (counterUiState.categoryDetails.value.zekrList.size > 1) {
        IconButton(
            onClick = {
                val swapDirection =
                    if (settingsDetails.swapDirection == SwapDirection.Horizontal) SwapDirection.Vertical
                    else SwapDirection.Horizontal
                CounterHelper.onSettingUpdate(settingsDetails.copy(swapDirection = swapDirection))
            },
            modifier = Modifier.requiredWidth(width)
        ) {
            Icon(
                Icons.Default.SwapVert,
                contentDescription = "Vertical Swap"
            )
        }
    }
}

/** A button for toggling the lock state. */
@Composable
private fun Lock(
    counterUiState: CounterUiState,
    width: Dp
) {
    IconButton(
        onClick = {
            CounterHelper.updateUiState(
                counterUiState.copy(lockEnabled = !counterUiState.lockEnabled)
            )
        },
        modifier = Modifier.requiredWidth(width)
    ) {
        Icon(
            imageVector =
            if (!counterUiState.lockEnabled) Icons.Default.LockOpen
            else Icons.Default.Lock,
            contentDescription = "Lock"
        )
    }
}

/** A button for changing the theme mode. */
@Composable
private fun ThemeMode(
    settingsDetails: SettingsDetails,
    width: Dp,
    tint: Color
) {
    IconButton(
        onClick = CounterHelper.onClickThemeMode,
        modifier = Modifier.requiredWidth(width)
    ) {
        if (settingsDetails.themeMode == ThemeMode.System) {
            Icon(
                imageVector = Icons.Filled.BrightnessAuto,
                contentDescription = "System Theme Mode",
            )
        } else {
            HeaderImage(
                drawableResource = ZekrTheme.resources(settingsDetails).themeMode,
                contentDescription = stringResource(Res.string.about_zekr),
                tint = tint
            )
        }
    }
}

/** Displays an image within the header. */
@Composable
fun HeaderImage(
    drawableResource: DrawableResource,
    tint: Color? = null,
    contentDescription: String,
) {
    Image(
        painter = painterResource(drawableResource),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        colorFilter = tint?.let { ColorFilter.tint(it) },
        modifier = Modifier.height(24.dp)
    )
}

/**
 * Displays a control for adjusting the font size.
 *
 * @param settingsDetails The settings for the application.
 * @param width The width of the control.
 */
@Composable
fun FontSizeControl(
    settingsDetails: SettingsDetails,
    width: Dp,
) {
    var showSlider by remember { mutableStateOf(false) }
    var height by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    Box(
        modifier = Modifier
            .requiredWidth(width),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = { showSlider = !showSlider },
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    height = with(localDensity) { coordinates.size.height.toDp() }
                }
        ) {
            Icon(
                imageVector = Icons.Filled.TextFields,
                contentDescription = "Change Font Size",
            )
        }

        AnimatedVisibility(visible = showSlider) {
            Popup(
                alignment = Alignment.TopCenter,
                onDismissRequest = { showSlider = false },
                offset = IntOffset(
                    0,
                    (height.value + xLarge.value).toInt()
                )
            ) {
                FontSizeSlider(settingsDetails)
            }
        }
    }
}

/**
 * A slider to control the font size.
 *
 * @param settingsDetails The settings for the application.
 */
@Composable
private fun FontSizeSlider(settingsDetails: SettingsDetails) {
    HeaderControlCard(settingsDetails) {
        Row(
            modifier = Modifier
                .padding(horizontal = small)
                .height(xxLarge),
            horizontalArrangement = Arrangement.spacedBy(normal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val colors = MaterialTheme.colorScheme
            val zekrColors = ZekrTheme.colors(settingsDetails)

            Text(
                text = "T",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = zekrColors.onSecondaryHeader
            )

            Slider(
                modifier = Modifier.fillMaxWidth(0.3f),
                value = settingsDetails.fontSize,
                onValueChange = { newFontSize ->
                    CounterHelper.onSettingUpdate(settingsDetails.copy(fontSize = newFontSize))
                },
                steps = 24,
                valueRange = 4f..96f,
                colors = SliderDefaults.colors(
                    thumbColor = zekrColors.onSecondaryHeader,
                    activeTrackColor = zekrColors.onSecondaryHeader,
                    inactiveTrackColor = colors.surfaceVariant,
                    activeTickColor = zekrColors.onSecondaryHeader,
                    inactiveTickColor = colors.surfaceVariant,
                )
            )

            Text(
                text = "T",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = zekrColors.onSecondaryHeader
            )
        }
    }
}


@Preview
@Composable
fun HomeBarPreviewDark() {
    AppTheme(ThemeMode.Dark) {
        Surface {
            RtlView {
                with(CounterHelper) {
                    getZekr = {
                        mutableStateOf(
                            ZekrDetails(
                                id = 1,
                                text = "سبحان الله وبحمده سبحان الله العظيم",
                                editable = true,
                                soundFileName = "sound.mp3",
                            )
                        )
                    }
                }
                ZekrCounterTopBar(
                    settingsDetails = SettingsDetails(
                        themeMode = ThemeMode.Dark
                    ),
                    categoryDetails = categoryDetailsPreviewState(),
                    counterUiState = CounterUiState(
//                        playState = Player.STATE_READY,
                        playState = 1,
                        categoryDetails = categoryDetailsPreviewState()
                    ),
                    pagerState = rememberPagerState(
                        initialPage = 0,
                        pageCount = { 1 }
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
fun FontSizeSliderPreview() {
    Surface {
        RtlView {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xff3b3729)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppTheme {
                    FontSizeSlider(
                        settingsDetails = SettingsDetails()
                    )
                }
                AppTheme(themeMode = ThemeMode.Dark) {
                    FontSizeSlider(
                        settingsDetails = SettingsDetails(
                            themeMode = ThemeMode.Dark
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeBarPreviewLight() {
    AppTheme {
        Surface {
            RtlView {
                ZekrCounterTopBar(
                    categoryDetails = categoryDetailsPreviewState(),
                    counterUiState = CounterUiState(),
                    settingsDetails = SettingsDetails(
                        themeMode = ThemeMode.Light
                    ),
                    pagerState = rememberPagerState(
                        initialPage = 0,
                        pageCount = { 1 }
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeBarPreviewLockEnabled() {
    AppTheme(themeMode = ThemeMode.Dark) {
        Surface {
            RtlView {
                ZekrCounterTopBar(
                    categoryDetails = categoryDetailsPreviewState(),
                    counterUiState = CounterUiState(
                        lockEnabled = true
                    ),
                    settingsDetails = SettingsDetails(
                        themeMode = ThemeMode.Light
                    ),
                    pagerState = rememberPagerState(
                        initialPage = 0,
                        pageCount = { 1 }
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun CountVisibilityDropdownMenuPreview() {
    AppTheme {
        Surface {
            RtlView {
                CountVisibilityDropdownMenu(
                    showCountSettingsMenu = remember { mutableStateOf(true) },
                    secondaryHeaderSize = DpSize(100.dp, 100.dp),
                    settingsDetails = SettingsDetails()
                )
            }
        }
    }
}


