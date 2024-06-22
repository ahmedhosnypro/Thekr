package com.thekr.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thekr.util.TimeHelper.now

@Entity(tableName = "count")
data class Count(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var zekrCategoryId: Long = 1,
    var zekrInstanceId: Long,
    var value: Long = 0,
    var timeCreated: Long = now(),
)