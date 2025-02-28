package com.thekr.ui.counter.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.data.thekr.count.ThekrCount
import com.thekr.data.thekr.instance.ThekrInstanceDetails
import com.thekr.data.thekr.thekr.ThekrDetails
import com.thekr.model.Count

@Stable
@Immutable
data class CounterUiState(
    val categoryDetails: MutableState<CategoryDetails> = mutableStateOf(CategoryDetails()),
    val currentThekrInstance: MutableState<ThekrInstanceDetails> = mutableStateOf(ThekrInstanceDetails()),
    val currentThekr: MutableState<ThekrDetails> = mutableStateOf(ThekrDetails()),
    val currentCount: MutableState<ThekrCount> = mutableStateOf(ThekrCount()),
    val countItemsByDay: Map<Long, List<Count>> = mapOf(),
    val controlHeaderVisible: Boolean = true,
    val clickable: Boolean = true,

    val showCounter: Boolean = false,

    val showCategoryThekrListMenu: Boolean = false,

    val showStatistics: Boolean = false,

    val isAudioPlaying: Boolean = false,

    val lockEnabled: Boolean = false,
)