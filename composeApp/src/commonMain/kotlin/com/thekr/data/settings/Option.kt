package com.thekr.data.settings

import org.jetbrains.compose.resources.StringResource

data class Option(
    val textRes: StringResource,
    val descriptionTextRes: StringResource? = null,
    val value: String,
    val selected: Boolean = false
)
