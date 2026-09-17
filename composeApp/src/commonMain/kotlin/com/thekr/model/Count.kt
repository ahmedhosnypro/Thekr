package com.thekr.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.thekr.util.TimeHelper.now

@Entity(
    tableName = "count",
    indices = [Index("thekrInstanceId", "timeCreated")],
)
data class Count(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val thekrCategoryId: Long = 1,
    val thekrInstanceId: Long,
    val value: Long = 0,
    val timeCreated: Long = now(),
)
