package com.thekr.ui.counter.bar.bottom

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.ui.counter.viewmodel.CounterUiState
import com.thekr.ui.theme.AppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThekrCounterBottomBar(
    settingsDetails: SettingsDetails,
    categoryDetails: MutableState<CategoryDetails>,
    counterUiState: CounterUiState,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    BottomAppBar(

    ) {
        val tint = AppTheme.colors(settingsDetails).onSecondaryHeader


    }
}