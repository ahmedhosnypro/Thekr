package com.thekr.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "thekr_lang")
data class ThekrLang(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var thekrId: Int = 1,
    var ar: String = "",
    var en: String = "",
)