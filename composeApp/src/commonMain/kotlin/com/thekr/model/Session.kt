package com.thekr.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thekr.util.TimeHelper.now

@Entity
data class Session(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var thekrCategoryId: Int = 1,
    var thekrInstanceId: Int,
    var timeStarted: Long = now(),
    var lastActivity: Long = 0,
    var timeEnded: Long = 0,
)