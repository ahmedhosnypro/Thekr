package com.thekr.ui.stats

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thekr.data.settings.SettingsDetails
import com.thekr.resources.Res
import com.thekr.resources.back
import com.thekr.resources.daily
import com.thekr.resources.monthly
import com.thekr.resources.statistics
import com.thekr.resources.weekly
import com.thekr.stats.tab.DayStats
import com.thekr.stats.tab.MonthlyStats
import com.thekr.stats.tab.WeekStats
import com.thekr.ui.component.bar.AppTopBar
import com.thekr.values.Dimensions.xLarge
import com.thekr.util.TimeHelper.calcMidnight
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

enum class StatisticsType(val titleRes: StringResource) {
    Daily(Res.string.daily),
    Weekly(Res.string.weekly),
    Monthly(Res.string.monthly),
}

enum class DayStatisticsType {
    Hourly,
    Minute,
}

@Composable
fun ThekrStats(
    settingsDetails: SettingsDetails,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf("Count", "Miss")
    val countMissPagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    val dayStatisticsType = remember {
        mutableStateOf(DayStatisticsType.Hourly)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = {
                    Text(stringResource(Res.string.statistics))
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
                },
                settingsDetails = settingsDetails,
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = stringResource(Res.string.back),
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = countMissPagerState,
            modifier = Modifier.padding(innerPadding),
        ) { page ->
            when (page) {
                0 -> CountStats(
                    dayStatisticsType = dayStatisticsType
                )
                1 -> MissStats()
            }
        }
    }
}

@Composable
fun MissStats() {

}

@Composable
fun CountStats(
    modifier: Modifier = Modifier,
    dayStatisticsType: MutableState<DayStatisticsType>
) {
    val countPagerState = rememberPagerState(pageCount = { StatisticsType.entries.size })

    val time = remember {
        mutableLongStateOf(calcMidnight())
    }


    Column(modifier = modifier.fillMaxWidth()) {
        BoxWithConstraints {
            val width = maxWidth
            TabRow(
                selectedTabIndex = minOf(StatisticsType.entries.size, countPagerState.currentPage),
                modifier = Modifier
                    .padding(horizontal = xLarge)
            ) {
                StatisticsTab(
                    countPagerState,
                    modifier = Modifier.width(width / StatisticsType.entries.size)
                )
            }
        }

        HorizontalPager(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.Top,
            state = countPagerState,
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> DayStats(
                    midnight = time,
                    dayStatisticsType = dayStatisticsType,
                    modifier = modifier
                )

                1 -> WeekStats(
                    time = time,
                    modifier = modifier
                )

                2 -> MonthlyStats(
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun StatisticsTab(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    StatisticsType.entries.forEachIndexed { index, tab ->
        val color by animateColorAsState(
            if (pagerState.currentPage != index) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.primary, label = ""
        )
        Row(modifier) {
            Tab(
                selected = (index == pagerState.currentPage), onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(tab.titleRes), color = color)
                    }
                }
            )
        }
    }
}