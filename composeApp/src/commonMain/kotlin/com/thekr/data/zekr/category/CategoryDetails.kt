package com.thekr.data.zekr.category

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.thekr.data.count.miss.CountMissDetails
import com.thekr.data.zekr.count.ZekrCount
import com.thekr.data.zekr.fadl.FadlDetails
import com.thekr.data.zekr.instance.ZekrInstanceDetails
import com.thekr.data.zekr.zekr.ZekrDetails

@Stable
@Immutable
data class CategoryDetails(
    val id: Long = 0,
    val name: String = "",
    val parent: Long = 0,
    val iconFileName: String? = null,
    val childCategories: SnapshotStateList<MutableState<CategoryDetails>> = mutableStateListOf(),
    val zekrList: SnapshotStateList<MutableState<ZekrDetails>> = mutableStateListOf(),
    val zekrInstanceList: SnapshotStateList<MutableState<ZekrInstanceDetails>> = mutableStateListOf(),
    val countList: SnapshotStateList<MutableState<ZekrCount>> = mutableStateListOf(),
    val countMissList: SnapshotStateList<CountMissDetails> = mutableStateListOf(),
    val fadlList: SnapshotStateList<FadlDetails> = mutableStateListOf(),
)