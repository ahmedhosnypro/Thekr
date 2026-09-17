package com.thekr.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.thekr.data.count.miss.CountMissDetails
import com.thekr.util.TimeHelper.now

@Entity(
    tableName = "count_miss",
    indices = [Index("thekrInstanceId")],
)
data class CountMiss(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val thekrCategoryId: Long = 0,
    val thekrInstanceId: Long = 0,
    val value: Long = 0,
    val timeCreated: Long = now(),
) {
    fun toCountMissDetails() = CountMissDetails(
        id = id,
        thekrCategoryId = thekrCategoryId,
        thekrInstanceId = thekrInstanceId,
        value = value,
        timeCreated = timeCreated,
    )
}
