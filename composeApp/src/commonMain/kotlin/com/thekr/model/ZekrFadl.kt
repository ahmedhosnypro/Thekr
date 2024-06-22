package com.thekr.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thekr.data.zekr.fadl.FadlDetails
import com.thekr.util.TimeHelper.now

@Entity(tableName = "zekr_fadl")
data class ZekrFadl(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var zekrId: Long = 0,
    var fadl: String = "",
    var timeCreated: Long = now(),
    var timeUpdated: Long = timeCreated,
) {
    fun toFadlDetails(): FadlDetails {
        return FadlDetails(
            id = id,
            zekrId = zekrId,
            fadl = fadl,
            timeCreated = timeCreated,
            timeUpdated = timeUpdated,
        )
    }
}