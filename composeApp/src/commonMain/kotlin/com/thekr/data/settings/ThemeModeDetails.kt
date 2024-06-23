package com.thekr.data.settings

import org.jetbrains.compose.resources.StringResource
import com.thekr.resources.Res
import com.thekr.resources.dark
import com.thekr.resources.light
import com.thekr.resources.system_default

enum class ThemeModeDetails(var titleRes: StringResource) {
    System(Res.string.system_default),
    Light(Res.string.light),
    Dark(Res.string.dark),
}