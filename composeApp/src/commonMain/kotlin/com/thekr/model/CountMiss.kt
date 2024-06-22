package com.thekr.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thekr.data.count.miss.CountMissDetails
import com.thekr.util.TimeHelper.now

@Entity(tableName = "count_miss")
data class CountMiss(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var zekrCategoryId: Long = 0,
    var zekrInstanceId: Long = 0,
    var value: Long = 0,
    var timeCreated: Long = now(),
) {
    fun toCountMissDetails() = CountMissDetails(
        id = id,
        zekrCategoryId = zekrCategoryId,
        zekrInstanceId = zekrInstanceId,
        value = value,
        timeCreated = timeCreated,
    )
}