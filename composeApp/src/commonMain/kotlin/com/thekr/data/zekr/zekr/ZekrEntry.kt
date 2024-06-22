package com.thekr.data.zekr.zekr

import androidx.compose.runtime.Stable
import com.thekr.model.Zekr
import com.thekr.model.ZekrInstance
import com.thekr.model.ZekrTargetStatus
import com.thekr.util.TimeHelper.now


@Stable
data class ZekrEntry(
    val id: Long = 0,
    val text: String = "أستغفر الله",
    val coolDown: Long = 400,
    // target
    val yearlyTarget: Long = 0,
    val yearlyTargetStatus: ZekrTargetStatus = ZekrTargetStatus.Disabled,
    val monthlyTarget: Long = 0,
    val monthlyTargetStatus: ZekrTargetStatus = ZekrTargetStatus.Disabled,
    val weeklyTarget: Long = 0,
    val weeklyTargetStatus: ZekrTargetStatus = ZekrTargetStatus.Disabled,
    val dailyTarget: Long = 0,
    val dailyTargetStatus: ZekrTargetStatus = ZekrTargetStatus.Disabled,
) {
    fun toZekr(): Zekr = this.let {
        val now = now()
        Zekr(
            id = id,
            content = text,
            coolDown = coolDown,
            timeCreated = now,
            timeUpdated = now,
        )
    }

    fun toZekrInstance(): ZekrInstance {
        val now = now()
        return ZekrInstance(
            zekrId = id,
            categoryId = 1,
            yearlyTarget = yearlyTarget,
            yearlyTargetStatus = yearlyTargetStatus,
            monthlyTarget = monthlyTarget,
            monthlyTargetStatus = monthlyTargetStatus,
            weeklyTarget = weeklyTarget,
            weeklyTargetStatus = weeklyTargetStatus,
            dailyTarget = dailyTarget,
            dailyTargetStatus = dailyTargetStatus,
            timeCreated = now,
            timeUpdated = now,
        )
    }
}