@file:OptIn(
    ExperimentalMaterial3Api::class,
    InternalVoyagerApi::class
)

package com.thekr.ui.counter

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.internal.BackHandler
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.data.thekr.instance.ThekrInstanceDetails
import com.thekr.model.ThekrTargetStatus
import com.thekr.ui.counter.body.ThekrText
import com.thekr.ui.counter.footer.CurrentThekrIndicator
import com.thekr.ui.counter.footer.ThekrCount
import com.thekr.ui.counter.viewmodel.CounterUiState
import com.thekr.values.Dimensions.normal
import com.thekr.values.Dimensions.small
import com.thekr.values.Dimensions.tiny
import com.thekr.ui.home.list.categoryDetailsPreviewState
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.util.KeepScreenOn
import com.thekr.ui.util.NoRippleInteractionSource
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.util.customOnKeyEvent
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ThekrHome(
    settingsDetails: SettingsDetails,
    counterUiState: CounterUiState,
    categoryDetails: MutableState<CategoryDetails>,
    pagerState: PagerState,
    countSheetState: BottomSheetScaffoldState,
    modifier: Modifier = Modifier,
) {
    BackHandler(true) {
        if (counterUiState.lockEnabled) {
            // todo: show a snackbar
        } else {
            CounterHelper.onNavigateUp()
        }
    }

    DisposableEffect(Unit) { onDispose { CounterHelper.onCounterDispose() } }

    KeepScreenOn(
        settingsDetails.screenAlwaysOn,
    ) {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            if (settingsDetails.volumeControl)
                focusRequester.requestFocus()
        }
        Box(
            modifier = modifier
                .clickable(
                    interactionSource = NoRippleInteractionSource(),
                    indication = LocalIndication.current,
                    onClick = { CounterHelper.onCount() }
                )
                // to use volume keys to increment and decrement the counter
                .customOnKeyEvent(
                    enabled = settingsDetails.volumeControl,
                    focusRequester = focusRequester
                )
        ) {
            LaunchedEffect(pagerState.currentPage) {
                CounterHelper.scrollToThekr(pagerState.currentPage)
            }
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.Top,
                state = pagerState,
                userScrollEnabled = counterUiState.lockEnabled.not()
            ) { tabIndex ->
                ThekrHomeBody(
                    counterUiState = counterUiState,
                    settingsDetails = settingsDetails,
                    categoryDetails = categoryDetails,
                    tabIndex = tabIndex,
                    countSheetState = countSheetState,
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThekrHomeBody(
    counterUiState: CounterUiState,
    categoryDetails: MutableState<CategoryDetails>,
    settingsDetails: SettingsDetails,
    countSheetState: BottomSheetScaffoldState,
    modifier: Modifier = Modifier,
    tabIndex: Int = 0,
) {

    val peak = if (categoryDetails.value.thekrList.size > 1) 72.dp else 0.dp
    val colors = AppTheme.colors(settingsDetails)
    BottomSheetScaffold(
        scaffoldState = countSheetState,
        sheetSwipeEnabled = false,
        sheetContent = {
            if (settingsDetails.showCount) {
                ThekrCount(
                    settingsDetails = settingsDetails,
                    tabIndex = tabIndex
                )
            }
        },
        sheetDragHandle = {
            if (categoryDetails.value.thekrList.size > 1) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = small)
                        .requiredHeight(peak),

                    verticalArrangement = Arrangement.spacedBy(normal, Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CurrentThekrIndicator(
                        categoryDetails = categoryDetails,
                        tabIndex = tabIndex,
                        settingsDetails = settingsDetails,
                    )
//                        if (isDailyTargetEnabled) {
//                            DailyTarget(
//                                thekrInstanceDetails = counterUiState.currentThekrInstance,
//                                category = categoryDetails,
//                                settingsDetails = settingsDetails,
//                            )
//                        }
                }
            }
        },
        sheetPeekHeight = peak,
        sheetContainerColor = colors.sheetBackgroundColor,
        modifier = Modifier.background(color = Color.Green),
    ) {
        ThekrText(
            settingsDetails = settingsDetails,
            categoryDetails = categoryDetails,
            tabIndex = tabIndex,
            counterUiState = counterUiState,
        )
    }
}

@Composable
fun ChangeCurrentSheikh() {

}

@Composable
fun SheikhCard(
    sheikhName: String,
) {
    Card {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = tiny),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "بصوت الشيخ $sheikhName")
            }
            Row {
                TextButton(onClick = { }) {

                }
                TextButton(onClick = { }) {

                }
            }
        }
    }
}

@Preview
@Composable
fun CounterScreenPreviewDark() {
    LocalizedApp {
        AppTheme(ThemeMode.Dark) {
            Surface {
                with(CounterHelper) {
                    getThekrCount = {
                        mutableStateOf(
                            com.thekr.data.thekr.count.ThekrCount(
                                thekrInstanceId = 1,
                                dailyCount = 22,
                            )
                        )
                    }
//                    getThekr = {
//                        ThekrDetails(
//                            id = 1,
//                            text = "سبحان الله وبحمده سبحان الله العظيم",
//                            editable = true,
//                            soundFileName = "sound.mp3",
//                        )
//                    }
                    getThekrInstance = {
                        mutableStateOf(
                            ThekrInstanceDetails(
                                id = 1,
                                thekrId = 1,
                                categoryId = 1,
                                dailyTarget = 100,
                                dailyTargetStatus = ThekrTargetStatus.Enabled,
                            )
                        )
                    }
                }
                ThekrHomeBody(
                    categoryDetails = categoryDetailsPreviewState(),
                    counterUiState = CounterUiState(
                        showCounter = true,
                    ),
                    tabIndex = 0,
                    settingsDetails = SettingsDetails(
                        fontSize = 32f,

                        themeMode = ThemeMode.Dark,
                    ),
                    countSheetState = rememberBottomSheetScaffoldState(
                        bottomSheetState = rememberStandardBottomSheetState(
                            initialValue = SheetValue.Expanded
                        )
                    ),
                )
            }
        }
    }
}


@Preview()
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CounterScreenPreviewLight() {
    LocalizedApp {
        AppTheme(ThemeMode.Light) {
            Surface {
                ThekrHomeBody(
                    categoryDetails = categoryDetailsPreviewState(),
                    counterUiState = CounterUiState(
                        showCounter = true,
//                        currentThekrInstance = previewList()[0],
                    ),
                    settingsDetails = SettingsDetails(
                        fontSize = 48f,
                        themeMode = ThemeMode.Light,
                    ),
                    countSheetState = rememberBottomSheetScaffoldState(),
                )
            }
        }
    }
}