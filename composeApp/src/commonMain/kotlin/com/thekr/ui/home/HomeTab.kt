package com.thekr.ui.home

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import com.thekr.resources.Res
import com.thekr.resources.doaa_active
import com.thekr.resources.dua
import com.thekr.resources.hesn_active
import com.thekr.resources.hesn_almuslim
import com.thekr.resources.knooz
import com.thekr.resources.mesbaha
import com.thekr.resources.sebha
import com.thekr.resources.sound_azkar_active

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