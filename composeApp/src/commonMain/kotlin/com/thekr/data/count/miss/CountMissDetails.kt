package com.thekr.data.count.miss

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.thekr.util.TimeHelper.now

@Stable
@Immutable
data class CountMissDetails(
    val id: Long = 0,
    val zekrCategoryId: Long = 1,
    val zekrInstanceId: Long,
    val value: Long = 0,
    val timeCreated: Long = now(),
)