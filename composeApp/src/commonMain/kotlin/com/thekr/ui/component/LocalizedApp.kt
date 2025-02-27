package com.thekr.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.thekr.util.Language
import com.thekr.util.isRtlLanguage


@Composable
fun LocalizedApp(
    language: String = Language.Arabic.isoFormat,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides  if (isRtlLanguage(language)) LayoutDirection.Rtl else LayoutDirection.Ltr,
    ) {
        content()
    }
}

