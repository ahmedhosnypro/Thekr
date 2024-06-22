package com.thekr.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zekr_goal_completion")
data class ZekrGoalCompletion(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
)