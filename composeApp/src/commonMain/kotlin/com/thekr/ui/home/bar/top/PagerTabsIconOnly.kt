@file:OptIn(ExperimentalFoundationApi::class)

package com.thekr.ui.home.bar.top

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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

@Preview
@Composable
fun PagerTabsIconOnlyPreview() {
    PagerTabsIconOnly(
        pagerState = rememberPagerState(pageCount = { 1 }),
    )
}