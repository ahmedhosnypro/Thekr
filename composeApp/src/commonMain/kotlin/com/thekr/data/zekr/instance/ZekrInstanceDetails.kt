package com.thekr.data.zekr.instance

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.thekr.model.ZekrTargetStatus
import com.thekr.util.TimeHelper.now

@Stable
@Immutable
data class ZekrInstanceDetails(
    var id: Long = 1,
    var zekrId: Long = 1,
    var categoryId: Long = 1,
    var isProtected: Boolean = false,
    var editable: Boolean = false,
    // target goals
    var yearlyTarget: Long = 0,
    var yearlyTargetStatus: ZekrTargetStatus = ZekrTargetStatus.Disabled,
    var monthlyTarget: Long = 0,
    var monthlyTargetStatus: ZekrTargetStatus = ZekrTargetStatus.Disabled,
    var weeklyTarget: Long = 0,
    var weeklyTargetStatus: ZekrTargetStatus = ZekrTargetStatus.Disabled,
    var dailyTarget: Long = 0,
    var dailyTargetStatus: ZekrTargetStatus = ZekrTargetStatus.Disabled,
    var timeCreated: Long = now(),
    var timeUpdated: Long = timeCreated,
)