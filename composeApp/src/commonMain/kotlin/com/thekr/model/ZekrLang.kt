package com.thekr.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "zekr_lang")
data class ZekrLang(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var zekrId: Int = 1,
    var ar: String = "",
    var en: String = "",
)