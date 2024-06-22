package com.thekr.ui.home

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import thekr.composeapp.generated.resources.Res
import thekr.composeapp.generated.resources.doaa_active
import thekr.composeapp.generated.resources.dua
import thekr.composeapp.generated.resources.hesn_active
import thekr.composeapp.generated.resources.hesn_almuslim
import thekr.composeapp.generated.resources.knooz
import thekr.composeapp.generated.resources.mesbaha
import thekr.composeapp.generated.resources.sebha
import thekr.composeapp.generated.resources.sound_azkar_active

/** Represents the tabs available on the Home screen. */
enum class HomeTab(
    val stringResource: StringResource,
    val iconRes: DrawableResource
) {
    Mesbaha(Res.string.mesbaha, Res.drawable.sebha),
    HesnAlMuslim(Res.string.hesn_almuslim, Res.drawable.hesn_active),
    Knooz(Res.string.knooz, Res.drawable.sound_azkar_active),
    Dua(Res.string.dua, Res.drawable.doaa_active),
}