package com.thekr.ui.home.header

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview


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

