package com.thekr.data.thekr.category

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.thekr.data.count.miss.CountMissDetails
import com.thekr.data.thekr.count.ThekrCount
import com.thekr.data.thekr.fadl.FadlDetails
import com.thekr.data.thekr.instance.ThekrInstanceDetails
import com.thekr.data.thekr.thekr.ThekrDetails

@Stable
@Immutable
data class CategoryDetails(
    val id: Long = 0,
    val name: String = "",
    val parent: Long = 0,
    val iconFileName: String? = null,
    val childCategories: SnapshotStateList<MutableState<CategoryDetails>> = mutableStateListOf(),
    val thekrList: SnapshotStateList<MutableState<ThekrDetails>> = mutableStateListOf(),
    val thekrInstanceList: SnapshotStateList<MutableState<ThekrInstanceDetails>> = mutableStateListOf(),
    val countList: SnapshotStateList<MutableState<ThekrCount>> = mutableStateListOf(),
    val countMissList: SnapshotStateList<CountMissDetails> = mutableStateListOf(),
    val fadlList: SnapshotStateList<FadlDetails> = mutableStateListOf(),
)