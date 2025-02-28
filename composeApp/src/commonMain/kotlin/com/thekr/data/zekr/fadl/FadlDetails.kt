package com.thekr.data.thekr.fadl

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.thekr.util.TimeHelper.now

@Stable
@Immutable
data class FadlDetails(
    val id: Long = 0,
    val thekrId: Long = 0,
    val fadl: String = "",
    val timeCreated: Long = now(),
    val timeUpdated: Long = timeCreated,
)