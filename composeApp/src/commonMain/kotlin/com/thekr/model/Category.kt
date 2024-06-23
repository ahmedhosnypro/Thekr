package com.thekr.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thekr.data.zekr.category.CategoryDetails
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String = "",
    var parent: Long = 0,
    var iconFileName: String? = null,
) {
    fun toCategoryDetails() = CategoryDetails(
        id = id,
        name = name,
        parent = parent,
        iconFileName = iconFileName,
    )
}

enum class ZekrCategoryType(val id: Long, val tabIndex: Int) {
    User(1, 0),
    HesnAlMuslim(2, 1),
    Knooz(3, 2),
    Dua(4, 3),
}