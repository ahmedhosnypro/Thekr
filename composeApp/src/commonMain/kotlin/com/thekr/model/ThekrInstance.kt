package com.thekr.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thekr.data.thekr.instance.ThekrInstanceDetails
import com.thekr.util.TimeHelper.now
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "thekr_instance")
data class ThekrInstance(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var thekrId: Long = 1,
    var categoryId: Long = 1,
    @ColumnInfo(index = true)
    var isProtected: Boolean = false,
    var editable: Boolean = true,
    // target goals
    var yearlyTarget: Long = 0,
    var yearlyTargetStatus: ThekrTargetStatus = ThekrTargetStatus.Disabled,
    var monthlyTarget: Long = 0,
    var monthlyTargetStatus: ThekrTargetStatus = ThekrTargetStatus.Disabled,
    var weeklyTarget: Long = 0,
    var weeklyTargetStatus: ThekrTargetStatus = ThekrTargetStatus.Disabled,
    var dailyTarget: Long = 0,
    var dailyTargetStatus: ThekrTargetStatus = ThekrTargetStatus.Disabled,
    // end of target goals
    var timeCreated: Long = now(),
    var timeUpdated: Long = timeCreated,
) {
    fun toThekrInstanceDetails(): ThekrInstanceDetails = ThekrInstanceDetails(
        id = id,
        thekrId = thekrId,
        categoryId = categoryId,
        isProtected = isProtected,
        editable = editable,
        yearlyTarget = yearlyTarget,
        yearlyTargetStatus = yearlyTargetStatus,
        monthlyTarget = monthlyTarget,
        monthlyTargetStatus = monthlyTargetStatus,
        weeklyTarget = weeklyTarget,
        weeklyTargetStatus = weeklyTargetStatus,
        dailyTarget = dailyTarget,
        dailyTargetStatus = dailyTargetStatus,
        timeCreated = timeCreated,
        timeUpdated = timeUpdated,
    )
}


enum class ThekrTargetStatus {
    Enabled, Disabled, Impossible
}