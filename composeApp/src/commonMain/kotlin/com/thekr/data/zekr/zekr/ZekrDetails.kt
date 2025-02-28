package com.thekr.data.thekr.thekr

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.thekr.model.Thekr
import com.thekr.util.TimeHelper.now

@Stable
@Immutable
data class ThekrDetails(
    val id: Long = 1,
    val text: String = "",
    val basmlaType: Int = 0,
    // min time between clicks
    val coolDown: Long = 400,
    val soundFileName: String? = null,
    var shortSoundFileName: String? = null,
    val isProtected: Boolean = false,
    val editable: Boolean = true,
    val timeCreated: Long = now(),
    val timeUpdated: Long = timeCreated,
) {
    fun toThekr() = Thekr(
        id = id,
        content = text,
        coolDown = coolDown,
        soundFileName = soundFileName,
        shortSoundFileName = shortSoundFileName,
        isProtected = isProtected,
        editable = editable,
        timeCreated = timeCreated,
        timeUpdated = timeUpdated,
    )
}