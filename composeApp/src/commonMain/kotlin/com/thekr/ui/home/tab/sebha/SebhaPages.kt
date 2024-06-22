package com.thekr.ui.home.tab.sebha

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.thekr.R
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.ui.home.HomeActions
import com.thekr.ui.home.list.ZekrList

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
            if (category.value.zekrList.isNotEmpty()) {
                ZekrList(
                    category = category,
                    modifier = Modifier.fillMaxSize(),
                    onHomeListItemClick = HomeActions.onZekrClick,
                    settingsDetails = settingsDetails,
                )
            } else {
                SebhaAddNewButton(
                    onCLick = { HomeActions.onCreateZekrClick() },
                    text = stringResource(id = R.string.add_zekr)
                )
            }
        }
    }
}