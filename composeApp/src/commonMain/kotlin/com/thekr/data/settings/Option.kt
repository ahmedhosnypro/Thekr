package com.thekr.data.settings

data class Option(
    val textRes: Int,
    val descriptionTextRes: Int? = null,
    val value: String,
    val selected: Boolean = false
)
