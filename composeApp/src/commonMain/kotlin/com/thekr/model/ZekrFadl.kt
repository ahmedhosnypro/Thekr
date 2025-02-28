package com.thekr.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thekr.data.thekr.fadl.FadlDetails
import com.thekr.util.TimeHelper.now

@Entity(tableName = "thekr_fadl")
data class ThekrFadl(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var thekrId: Long = 0,
    var fadl: String = "",
    var timeCreated: Long = now(),
    var timeUpdated: Long = timeCreated,
) {
    fun toFadlDetails(): FadlDetails {
        return FadlDetails(
            id = id,
            thekrId = thekrId,
            fadl = fadl,
            timeCreated = timeCreated,
            timeUpdated = timeUpdated,
        )
    }
}