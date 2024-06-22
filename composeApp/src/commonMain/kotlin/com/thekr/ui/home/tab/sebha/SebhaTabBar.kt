@file:OptIn(ExperimentalFoundationApi::class)

package com.thekr.ui.home.tab.sebha

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.ui.component.bottomBorder

@Composable
fun SebhaTabBar(
    userAzkar: SnapshotStateList<MutableState<CategoryDetails>>,
    pagerState: PagerState,
    onCategorySave: (Int) -> Unit = {}
) {
    val localDensity = LocalDensity.current
    val colors = MaterialTheme.colorScheme
    // Calculate the height of the tab bar, to be used in the new category button
    var height by remember {
        mutableStateOf(0.dp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bottomBorder(
                color = MaterialTheme.colorScheme.primary,
                height = 1f
            )
    ) {
        val createNewButtonWidth = 24.dp

        ScrollableTabRow(
            selectedTabIndex = minOf(userAzkar.size, pagerState.currentPage),
            edgePadding = 0.dp,
            containerColor = colors.surface,
            contentColor = colors.onSurface,
            modifier = Modifier
                .weight(1f)
                .background(color = Color.White)
                .onGloballyPositioned { coordinates ->
                    height = with(localDensity) {
                        (coordinates.size.height).toDp()
                    }
                },
            divider = {},
        ) {
            SebhaCategoryTab(
                pagerState = pagerState,
                tabs = userAzkar,
            )
        }

        // Create new zekr category button
        CreateCategoryButton(
            width = createNewButtonWidth,
            calculatedTabHeight = height,
            onCategorySave = onCategorySave
        )
    }
}