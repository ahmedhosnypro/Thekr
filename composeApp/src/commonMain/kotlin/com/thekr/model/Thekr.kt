package com.thekr.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thekr.data.thekr.thekr.ThekrDetails
import com.thekr.data.thekr.thekr.ThekrEntry
import com.thekr.util.TimeHelper.now
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "thekr")
data class Thekr(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var categoryId: Long = 0,
    var categoryName: String = "",
    var content: String = "",

    var dailyTarget: Int = 0,

    var description: String? = null,
    var fadl: String? = null,

    var contentEn: String? = null,
    var fadlEn: String? = null,
    var vocalEn: String? = null,

    var contentTr: String? = null,
    var vocalTr: String? = null,

    var contentFr: String? = null,

    var contentGr: String? = null,
    var vocalGr: String? = null,

    var contentUg: String? = null,
    var fadlUg: String? = null,

    var contentIn: String? = null,
    var fadlIn: String? = null,

    var basmlaType: Int = 0,
    var priority: Int = 0,
    var specialTime: Int = 0,
    var coolDown: Long = 0,
    var soundFileName: String? = "",
    var shortSoundFileName: String? = null,

    @ColumnInfo(index = true)
    var isProtected: Boolean = false,
    var editable: Boolean = true,
    var timeCreated: Long = now(),
    var timeUpdated: Long = timeCreated,
) {

    fun toThekrEntry(): ThekrEntry {
        return ThekrEntry(
            id = id,
            text = content,
            coolDown = coolDown,
        )
    }

    fun toThekrDetails(): ThekrDetails {
        return ThekrDetails(
            id = id,
            text = content,
            basmlaType = basmlaType,
            coolDown = coolDown,
            soundFileName = soundFileName,
            shortSoundFileName = shortSoundFileName,
            isProtected = isProtected,
            editable = editable,
            timeCreated = timeCreated,
            timeUpdated = timeUpdated,
        )
    }

    fun update(thekrEntry: ThekrEntry): Thekr {
        return this.copy(
            content = thekrEntry.text,
            timeUpdated = now(),
        )
    }
}