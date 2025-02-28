package com.thekr.data.thekr.count

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.thekr.util.TimeHelper.now

@Stable
@Immutable
data class ThekrCount(
    val thekrInstanceId: Long = 0,
    val categoryId: Long = 0,
    var dailyCount: Long = 0,
    var weeklyCount: Long = 0,
    var monthlyCount: Long = 0,
    var yearlyCount: Long = 0,
    var totalCount: Long = 0,
    var timeUpdated: Long = now(),
)