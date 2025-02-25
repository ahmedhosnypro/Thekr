package com.thekr.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.runtime.staticCompositionLocalOf

sealed class Language(val isoFormat : String) {
    data object English : Language("en")
    data object Arabic : Language("ar")
}

val LocalLocalization = staticCompositionLocalOf { Language.Arabic.isoFormat }

@Composable
fun MultiLang(
    language: String, content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides  if (isRtlLanguage(language)) LayoutDirection.Rtl else LayoutDirection.Ltr,
        LocalLocalization provides language
    ) {
        content()
    }
}

private val rtlLanguages = listOf("ar", "fa", "he")
private fun isRtlLanguage(langTag: String) = langTag in rtlLanguages