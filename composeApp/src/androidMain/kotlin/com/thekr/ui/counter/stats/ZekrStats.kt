//package com.thekr.ui.counter.stats
//
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.BoxWithConstraints
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.PagerState
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Tab
//import androidx.compose.material3.TabRow
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableLongStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.dimensionResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import com.thekr.R
//import com.thekr.data.settings.SettingsDetails
//import com.thekr.ui.bar.top.ZekrBar
//import com.thekr.util.calcMidnight
//import kotlinx.coroutines.launch
//
//enum class StatisticsType(val titleRes: Int) {
//    Daily(R.string.day),
//    Weekly(R.string.week),
//    Monthly(R.string.month),
//}
//
//enum class DayStatisticsType {
//    Hourly,
//    Minute,
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun ZekrStats(
//    settingsDetails: SettingsDetails,
//    onNavigateUp: () -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    val tabs = listOf("Count", "Miss")
//    val countMissPagerState = rememberPagerState(pageCount = { tabs.size })
//    val scope = rememberCoroutineScope()
//    Scaffold(
//        modifier = modifier,
//        topBar = {
//            ZekrBar(
//                title = {
//                    TabRow(selectedTabIndex = countMissPagerState.currentPage) {
//                        tabs.forEachIndexed { index, title ->
//                            Tab(
//                                selected = countMissPagerState.currentPage == index,
//                                onClick = {
//                                    scope.launch {
//                                        countMissPagerState.animateScrollToPage(index)
//                                    }
//                                },
//                                text = { Text(title) }
//                            )
//                        }
//                    }
//                },
//                settingsDetails = settingsDetails,
//                navigationIcon = {
//                    IconButton(onClick = onNavigateUp) {
//                        Icon(
//                            Icons.AutoMirrored.Filled.ArrowBackIos,
//                            contentDescription = stringResource(R.string.back),
//                        )
//                    }
//                },
//            )
//        }
//    ) { innerPadding ->
//        HorizontalPager(
//            state = countMissPagerState,
//            modifier = Modifier.padding(innerPadding),
//        ) { page ->
//            when (page) {
//                0 -> CountStats()
//                1 -> MissStats()
//            }
//        }
//    }
//}
//
//@Composable
//fun MissStats() {
//
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun CountStats(
//    modifier: Modifier = Modifier,
//) {
//    val countPagerState = rememberPagerState(pageCount = { StatisticsType.entries.size })
//
//    val time = remember {
//        mutableLongStateOf(calcMidnight())
//    }
//
//
//    Column(modifier = modifier.fillMaxWidth()) {
//        BoxWithConstraints {
//            val width = maxWidth
//            TabRow(
//                selectedTabIndex = minOf(StatisticsType.entries.size, countPagerState.currentPage),
//                modifier = Modifier
//                    .padding(horizontal = dimensionResource(id = R.dimen.padding_xlarge))
//            ) {
//                StatisticsTab(
//                    countPagerState,
//                    modifier = Modifier.width(width / StatisticsType.entries.size)
//                )
//            }
//        }
//        val dayStatisticsType = remember {
//            mutableStateOf(DayStatisticsType.Hourly)
//        }
//        HorizontalPager(
//            modifier = Modifier
//                .fillMaxSize(),
//            verticalAlignment = Alignment.Top,
//            state = countPagerState,
//            userScrollEnabled = false
//        ) { page ->
//            when (page) {
//                0 -> DayStats(
//                    midnight = time,
//                    dayStatisticsType = dayStatisticsType,
//                    modifier = modifier
//                )
//
//                1 -> WeekStats(
//                    time = time,
//                    modifier = modifier
//                )
//
//                2 -> MonthlyStats(
//                    modifier = modifier
//                )
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//private fun StatisticsTab(
//    pagerState: PagerState,
//    modifier: Modifier = Modifier
//) {
//    val coroutineScope = rememberCoroutineScope()
//    StatisticsType.entries.forEachIndexed { index, tab ->
//        val color by animateColorAsState(
//            if (pagerState.currentPage != index) MaterialTheme.colorScheme.onSurfaceVariant
//            else MaterialTheme.colorScheme.primary, label = ""
//        )
//        Row(modifier) {
//            Tab(
//                selected = (index == pagerState.currentPage), onClick = {
//                    coroutineScope.launch {
//                        pagerState.animateScrollToPage(index)
//                    }
//                },
//                text = {
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(4.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(text = stringResource(tab.titleRes), color = color)
//                    }
//                }
//            )
//        }
//    }
//}