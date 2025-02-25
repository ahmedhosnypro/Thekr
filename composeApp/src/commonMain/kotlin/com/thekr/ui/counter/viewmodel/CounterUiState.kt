package com.thekr.ui.counter.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.data.zekr.count.ZekrCount
import com.thekr.data.zekr.instance.ZekrInstanceDetails
import com.thekr.data.zekr.zekr.ZekrDetails
import com.thekr.model.Count

@Stable
@Immutable
data class CounterUiState(
    val categoryDetails: MutableState<CategoryDetails> = mutableStateOf(CategoryDetails()),
    val currentZekrInstance: MutableState<ZekrInstanceDetails> = mutableStateOf(ZekrInstanceDetails()),
    val currentZekr: MutableState<ZekrDetails> = mutableStateOf(ZekrDetails()),
    val currentCount: MutableState<ZekrCount> = mutableStateOf(ZekrCount()),
    val countItemsByDay: Map<Long, List<Count>> = mapOf(),
    val controlHeaderVisible: Boolean = true,
    val clickable: Boolean = true,

    val showCounter: Boolean = false,

    val showCategoryZekrListMenu: Boolean = false,

    val showStatistics: Boolean = false,

    val isAudioPlaying: Boolean = false,

    val lockEnabled: Boolean = false,
)