package com.thekr.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thekr.data.zekr.instance.ZekrInstanceDetails
import com.thekr.util.TimeHelper.now

@Entity(tableName = "zekr_instance")
data class ZekrInstance(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var zekrId: Long = 1,
    var categoryId: Long = 1,
    @ColumnInfo(index = true)
    var isProtected: Boolean = false,
    var editable: Boolean = true,
    // target goals
    var yearlyTarget: Long = 0,
    var yearlyTargetStatus: ZekrTargetStatus = ZekrTargetStatus.Disabled,
    var monthlyTarget: Long = 0,
    var monthlyTargetStatus: ZekrTargetStatus = ZekrTargetStatus.Disabled,
    var weeklyTarget: Long = 0,
    var weeklyTargetStatus: ZekrTargetStatus = ZekrTargetStatus.Disabled,
    var dailyTarget: Long = 0,
    var dailyTargetStatus: ZekrTargetStatus = ZekrTargetStatus.Disabled,
    // end of target goals
    var timeCreated: Long = now(),
    var timeUpdated: Long = timeCreated,
) {
    fun toZekrInstanceDetails(): ZekrInstanceDetails = ZekrInstanceDetails(
        id = id,
        zekrId = zekrId,
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


enum class ZekrTargetStatus {
    Enabled, Disabled, Impossible
}