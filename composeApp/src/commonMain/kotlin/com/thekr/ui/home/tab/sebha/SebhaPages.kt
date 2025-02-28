@file:OptIn(ExperimentalFoundationApi::class)

package com.thekr.ui.home.tab.sebha

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.ui.home.HomeActions
import com.thekr.ui.home.list.ThekrList
import org.jetbrains.compose.resources.stringResource
import com.thekr.resources.Res
import com.thekr.resources.add_thekr

@Composable
fun SebhaPages(
    userAzkar: SnapshotStateList<MutableState<CategoryDetails>>,
    pagerState: PagerState,
    settingsDetails: SettingsDetails,
) {
    HorizontalPager(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.Top,
        state = pagerState,
        userScrollEnabled = true
    ) { tabIndex ->
        val category = userAzkar[tabIndex]
        Column {
            if (category.value.thekrList.isNotEmpty()) {
                ThekrList(
                    categoryDetails = category,
                    modifier = Modifier.fillMaxSize(),
                    settingsDetails = settingsDetails,
                    homeOnClick = {tabIndex1: Int, categoryId: Long, thekrId: Long ->
                        HomeActions.onThekrClick(tabIndex1, categoryId, thekrId)
                    }
                )
            } else {
                SebhaAddNewButton(
                    onCLick = { HomeActions.onThekrCategoryClick() },
                    text = stringResource(Res.string.add_thekr)
                )
            }
        }
    }
}