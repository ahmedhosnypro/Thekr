package com.thekr.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "thekr_goal_completion")
data class ThekrGoalCompletion(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
)