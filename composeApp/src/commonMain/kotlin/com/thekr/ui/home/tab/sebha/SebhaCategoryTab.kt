package com.thekr.ui.home.tab.sebha

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import com.thekr.data.thekr.category.CategoryDetails
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SebhaCategoryTab(
    pagerState: PagerState,
    tabs: SnapshotStateList<MutableState<CategoryDetails>>,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val coroutineScope = rememberCoroutineScope()
    tabs.forEachIndexed { index, tab ->
        Tab(
            // todo: reorder
            modifier = modifier
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        // todo: select category to modify
                    }
                ),
            selected = index == pagerState.currentPage,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            text = { Text(tab.value.name) },
            unselectedContentColor = colors.onSurface,
            selectedContentColor = colors.primary,
        )
    }
}