package com.thekr.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "azkar")
data class Azkar(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var categoryId: Int = 0,
    var categoryName: String = "",
    var content: String = "",
    var dailyTarget: Int = 0,
    var description: String = "",
    var fadl: String = "",
    var contentEn: String = "",
    var fadlEn: String = "",
    var vocalEn: String = "",
    var contentTr: String = "",
    var vocalTr: String = "",
    var contentFr: String = "",
    var contentGr: String = "",
    var vocalGr: String = "",
    var contentUg: String = "",
    var fadlUg: String = "",
    var contentIn: String = "",
    var fadlIn: String = "",
    var basmlaType: String = "",
    var soundFileName: String = "",
    var priority: Int = 0,
    var specialTime: Int = 0,
    var coolDown: Long = 0
)