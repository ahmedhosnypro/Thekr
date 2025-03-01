package com.thekr.ui.home.bar.top

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.thekr.ui.home.HomeTab
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PagerTabsIconOnly(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    HomeTab.entries.forEachIndexed { index, tab ->
        val selected = (index == pagerState.currentPage)
        Surface(
            modifier = modifier,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }, color = Color.Transparent
        ) {
            Icon(
                painterResource(tab.iconRes),
                contentDescription = null, tint = if (selected) Color.White
                else Color.White.copy(alpha = .7f), modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun HeaderTabsRow(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val width = maxWidth
        ScrollableTabRow(selectedTabIndex = minOf(HomeTab.entries.size, pagerState.currentPage),
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .background(
                    color = Color(0x0DFFFFFF)
                ),
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(
                        tabPositions[pagerState.currentPage]
                    ),
                    color = Color.White
                )
            },
            divider = {}) {
            PagerTabsIconOnly(
                pagerState, modifier = Modifier.width(width / HomeTab.entries.size)
            )
        }
    }
}

@Preview
@Composable
fun PagerTabsIconOnlyPreview() {
    PagerTabsIconOnly(
        pagerState = rememberPagerState(pageCount = { 1 }),
    )
}

@Preview
@Composable
fun HeaderTabsRowPreview() {
    HeaderTabsRow(
//        maxWidth = 300.dp,
        pagerState = rememberPagerState(pageCount = { 3 })
    )
}

