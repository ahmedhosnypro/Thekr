package com.thekr.data.thekr.instance

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.thekr.model.ThekrTargetStatus
import com.thekr.util.TimeHelper.now

@Stable
@Immutable
data class ThekrInstanceDetails(
    var id: Long = 1,
    var thekrId: Long = 1,
    var categoryId: Long = 1,
    var isProtected: Boolean = false,
    var editable: Boolean = false,
    // target goals
    var yearlyTarget: Long = 0,
    var yearlyTargetStatus: ThekrTargetStatus = ThekrTargetStatus.Disabled,
    var monthlyTarget: Long = 0,
    var monthlyTargetStatus: ThekrTargetStatus = ThekrTargetStatus.Disabled,
    var weeklyTarget: Long = 0,
    var weeklyTargetStatus: ThekrTargetStatus = ThekrTargetStatus.Disabled,
    var dailyTarget: Long = 0,
    var dailyTargetStatus: ThekrTargetStatus = ThekrTargetStatus.Disabled,
    var timeCreated: Long = now(),
    var timeUpdated: Long = timeCreated,
)