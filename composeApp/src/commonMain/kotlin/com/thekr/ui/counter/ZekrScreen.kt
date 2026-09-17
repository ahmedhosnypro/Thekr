package com.thekr.ui.counter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.fingerprint.Fingerprint.setFingerprintListener
import com.thekr.ui.counter.CounterHelper.CounterActionComponents
import com.thekr.ui.counter.body.CategoryThekrList
import com.thekr.ui.counter.header.ThekrCounterTopBar
import com.thekr.ui.counter.viewmodel.CounterUiState
import com.thekr.ui.counter.viewmodel.ThekrCounterViewModel
import com.thekr.ui.counter.viewmodel.action.configSleepJop
import com.thekr.ui.home.list.categoryDetailsPreviewState
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.stats.ThekrStats
import com.thekr.ui.viewmodel.AppViewModelProvider
import korlibs.platform.Platform
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThekrScreen(
    settingsDetails: SettingsDetails,
    viewModel: ThekrCounterViewModel = viewModel {
        AppViewModelProvider.Factory.create(ThekrCounterViewModel::class, this)
    },
) {
    val counterUiState by viewModel.uiState.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = viewModel.initialPage,
        pageCount = { viewModel.pageCount },
    )

    val overlaysClosed = !counterUiState.showStatistics && !counterUiState.showCategoryThekrListMenu
    LaunchedEffect(settingsDetails.fingerPrintControl, overlaysClosed) {
        if (Platform.isAndroid) {
            viewModel.setFingerprintListener(settingsDetails.fingerPrintControl && overlaysClosed)
        }
    }

    val category = counterUiState.categoryDetails
    val coroutineScope = rememberCoroutineScope()
    // Derived from live state on every recomposition; the sheet state itself can
    // only capture it as a frozen initialValue, so the effect below re-applies it
    // whenever the live target changes (e.g. the instance list finishing loading).
    val sheetTarget = if (settingsDetails.showCount) {
        SheetValue.Expanded
    } else {
        if (category.value.thekrInstanceList.size > 1) {
            SheetValue.PartiallyExpanded
        } else {
            SheetValue.Hidden
        }
    }
    val countSheetState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = sheetTarget,
            confirmValueChange = { false },
            skipHiddenState = false
        )
    )

    LaunchedEffect(sheetTarget, category.value.thekrInstanceList.size) {
        val sheetState = countSheetState.bottomSheetState
        if (sheetState.currentValue != sheetTarget) {
            when (sheetTarget) {
                SheetValue.Expanded -> sheetState.expand()
                SheetValue.PartiallyExpanded -> sheetState.partialExpand()
                SheetValue.Hidden -> sheetState.hide()
            }
        }
    }

    LaunchedEffect(Unit) {
        CounterHelper.initActions(
            actionComponents = CounterActionComponents(
                counterViewModel = viewModel,
                coroutineScope = coroutineScope,
                thekrCountSheetState = countSheetState,
                pagerState = pagerState,
            )
        )
    }

    val actionsInitialized = CounterHelper.initialized

    if (actionsInitialized.value.not()) {
        return
    }


    LaunchedEffect(counterUiState.currentThekrInstance) {
        configSleepJop(viewModel)
    }


    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.currentStateFlow
            .drop(1)
            .filter { it == Lifecycle.State.RESUMED }
            .collect { configSleepJop(viewModel) }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onCounterDispose()
        }
    }

    if (counterUiState.showCategoryThekrListMenu) {
        CategoryThekrList(
            category = category,
            onNavigateUp = {
                viewModel.hideCategoryThekrListMenu()
            },
            categoryListOnClick = { tabIndex ->
                viewModel.hideCategoryThekrListMenu()
                coroutineScope.launch {
                    pagerState.animateScrollToPage(tabIndex)
                }
            },
            settingsDetails = settingsDetails,
        )
    } else if (counterUiState.showStatistics) {
        ThekrStats(
            settingsDetails = settingsDetails,
            onNavigateUp = {
                viewModel.hideStatistics()
            }
        )
    } else {
        ThekrScreenBody(
            settingsDetails = settingsDetails,
            categoryDetails = category,
            counterUiState = counterUiState,
            pagerState = pagerState,
            countSheetState = countSheetState
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThekrScreenBody(
    settingsDetails: SettingsDetails,
    counterUiState: CounterUiState,
    categoryDetails: MutableState<CategoryDetails>,
    pagerState: PagerState,
    countSheetState: BottomSheetScaffoldState,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier, topBar = {
        ThekrCounterTopBar(
            settingsDetails = settingsDetails,
            counterUiState = counterUiState,
            categoryDetails = categoryDetails,
            pagerState = pagerState
        )
    }) { scaffoldInnerPadding ->
        ThekrHome(
            counterUiState = counterUiState,
            settingsDetails = settingsDetails,
            pagerState = pagerState,
            countSheetState = countSheetState,
            categoryDetails = categoryDetails,
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldInnerPadding)
        )
    }
}


@Preview
@Composable
fun ThekrScreenBodyPreview() {
    LocalizedApp {
        AppTheme {
            Surface {
                ThekrScreenPreviewOnly(
                    settingsDetails = SettingsDetails(
                        showCount = true,
                        fingerPrintControl = true,
                        themeMode = ThemeMode.Light
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun ThekrScreenBodyPreviewDark() {
    LocalizedApp {
        AppTheme(ThemeMode.Dark) {
            Surface {
                ThekrScreenPreviewOnly(
                    settingsDetails = SettingsDetails(
                        showCount = true,
                        fingerPrintControl = true,
                        themeMode = ThemeMode.Dark
                    )
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ThekrScreenPreviewOnly(
    settingsDetails: SettingsDetails
) {
    val categoryDetails = categoryDetailsPreviewState()
    Scaffold(topBar = {
        with(CounterHelper) {
            getThekrCount = { categoryDetails.value.countList[0] }
            getThekr = { categoryDetails.value.thekrList[0] }
            getThekrInstance = { categoryDetails.value.thekrInstanceList[0] }
        }
        ThekrCounterTopBar(
            settingsDetails = settingsDetails,
            categoryDetails = categoryDetails,
            CounterUiState(),
            pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { 1 }
            )
        )
    }) { innerPadding ->
        ThekrHomeBody(
            CounterUiState(showCounter = true),
            settingsDetails = settingsDetails,
            countSheetState = rememberBottomSheetScaffoldState(),
            categoryDetails = categoryDetailsPreviewState(),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}