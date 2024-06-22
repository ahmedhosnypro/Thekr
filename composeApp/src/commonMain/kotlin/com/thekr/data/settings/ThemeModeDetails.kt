package com.thekr.data.settings

import org.jetbrains.compose.resources.StringResource
import thekr.composeapp.generated.resources.Res
import thekr.composeapp.generated.resources.dark
import thekr.composeapp.generated.resources.light
import thekr.composeapp.generated.resources.system_default

enum class ThemeModeDetails(var titleRes: StringResource) {
    System(Res.string.system_default),
    Light(Res.string.light),
    Dark(Res.string.dark),
}