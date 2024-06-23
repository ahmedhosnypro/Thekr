package com.thekr.ui.counter

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.ui.counter.CounterHelper.CounterActionComponents
import com.thekr.ui.counter.body.CategoryZekrList
import com.thekr.ui.counter.header.ZekrCounterTopBar
import com.thekr.ui.counter.viewModel.CounterUiState
import com.thekr.ui.counter.viewModel.ZekrCounterViewModel
import com.thekr.ui.counter.viewModel.action.configSleepJop
import com.thekr.ui.home.list.categoryDetailsPreviewState
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.component.RtlView
import com.thekr.ui.viewmodel.AppViewModelProvider
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ZekrScreen(
    settingsDetails: SettingsDetails,
    viewModel: ZekrCounterViewModel = viewModel {
        AppViewModelProvider.Factory.create(ZekrCounterViewModel::class, this)
    },
) {
    val counterUiState by viewModel.uiState.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = viewModel.initialPage,
        pageCount = { viewModel.pageCount },
    )

    LaunchedEffect(settingsDetails.fingerPrintControl) {
        viewModel.setFingerprintListener(settingsDetails.fingerPrintControl)
    }

    val category = counterUiState.categoryDetails
    val coroutineScope = rememberCoroutineScope()

    val countSheetState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = if (settingsDetails.showCount) SheetValue.Expanded
            else {
                if (category.value.zekrList.size > 1) SheetValue.PartiallyExpanded
                else SheetValue.Hidden
            },
            confirmValueChange = { false },
            skipHiddenState = false
        )
    )

    LaunchedEffect(Unit) {
        CounterHelper.initActions(
            actionComponents = CounterActionComponents(
                counterViewModel = viewModel,
                coroutineScope = coroutineScope,
                zekrCountSheetState = countSheetState,
                pagerState = pagerState,
            )
        )
    }


    LaunchedEffect(counterUiState.currentZekrInstance) {
        configSleepJop(viewModel)
    }


    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        // Do something with your state
        // You may want to use DisposableEffect or other alternatives
        // instead of LaunchedEffect
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                configSleepJop(viewModel)
            }

            else -> {
                // ignore
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onCounterDispose()
        }
    }

    if (counterUiState.showCategoryZekrListMenu) {
        CategoryZekrList(
            category = category,
            onNavigateUp = {
                viewModel.hideCategoryZekrListMenu()
            },
            onZekrClick = { zekrDetails ->
                viewModel.hideCategoryZekrListMenu()
                val tabIndex = category.value.zekrInstanceList.indexOf(zekrDetails)
                coroutineScope.launch {
                    pagerState.animateScrollToPage(tabIndex)
                }
            },
            settingsDetails = settingsDetails,
        )
    } else if (counterUiState.showStatistics) {
        // todo:
        //        ZekrStats(
//            settingsDetails = settingsDetails,
//            onNavigateUp = {
//                viewModel.hideStatistics()
//            }
//        )
    } else {
        ZekrScreenBody(
            settingsDetails = settingsDetails,
            categoryDetails = category,
            counterUiState = counterUiState,
            pagerState = pagerState,
            countSheetState = countSheetState
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ZekrScreenBody(
    settingsDetails: SettingsDetails,
    counterUiState: CounterUiState,
    categoryDetails: MutableState<CategoryDetails>,
    pagerState: PagerState,
    countSheetState: BottomSheetScaffoldState,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier, topBar = {
        ZekrCounterTopBar(
            settingsDetails = settingsDetails,
            counterUiState = counterUiState,
            categoryDetails = categoryDetails,
            pagerState = pagerState
        )
    }) { scaffoldInnerPadding ->
        ZekrHome(
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
fun ZekrScreenBodyPreview() {
    RtlView {
        AppTheme {
            Surface {
                ZekrScreenPreviewOnly(
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
fun ZekrScreenBodyPreviewDark() {
    RtlView {
        AppTheme(ThemeMode.Dark) {
            Surface {
                ZekrScreenPreviewOnly(
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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun ZekrScreenPreviewOnly(
    settingsDetails: SettingsDetails
) {
    val categoryDetails = categoryDetailsPreviewState()
    Scaffold(topBar = {
        with(CounterHelper) {
            getZekrCount = { categoryDetails.value.countList[0] }
            getZekr = { categoryDetails.value.zekrList[0] }
            getZekrInstance = { categoryDetails.value.zekrInstanceList[0] }
        }
        ZekrCounterTopBar(
            settingsDetails = settingsDetails,
            categoryDetails = categoryDetails,
            CounterUiState(),
            pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { 1 }
            )
        )
    }) { innerPadding ->
        ZekrHomeBody(
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