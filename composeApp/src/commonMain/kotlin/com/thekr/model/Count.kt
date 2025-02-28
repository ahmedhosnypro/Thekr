package com.thekr.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thekr.util.TimeHelper.now

@Entity(tableName = "count")
data class Count(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var thekrCategoryId: Long = 1,
    var thekrInstanceId: Long,
    var value: Long = 0,
    var timeCreated: Long = now(),
)