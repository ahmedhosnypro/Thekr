package com.thekr.data.thekr.thekr

import androidx.compose.runtime.Stable
import com.thekr.model.Thekr
import com.thekr.model.ThekrInstance
import com.thekr.model.ThekrTargetStatus
import com.thekr.util.TimeHelper.now


@Stable
data class ThekrEntry(
    val id: Long = 0,
    val text: String = "أستغفر الله",
    val coolDown: Long = 400,
    // target
    val yearlyTarget: Long = 0,
    val yearlyTargetStatus: ThekrTargetStatus = ThekrTargetStatus.Disabled,
    val monthlyTarget: Long = 0,
    val monthlyTargetStatus: ThekrTargetStatus = ThekrTargetStatus.Disabled,
    val weeklyTarget: Long = 0,
    val weeklyTargetStatus: ThekrTargetStatus = ThekrTargetStatus.Disabled,
    val dailyTarget: Long = 0,
    val dailyTargetStatus: ThekrTargetStatus = ThekrTargetStatus.Disabled,
) {
    fun toThekr(): Thekr = this.let {
        val now = now()
        Thekr(
            id = id,
            content = text,
            coolDown = coolDown,
            timeCreated = now,
            timeUpdated = now,
        )
    }

    fun toThekrInstance(): ThekrInstance {
        val now = now()
        return ThekrInstance(
            thekrId = id,
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