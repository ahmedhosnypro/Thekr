package com.thekr.util

sealed class Language(val isoFormat : String) {
    data object English : Language("en")
    data object Arabic : Language("ar")
}

val rtlLanguages = listOf("ar", "fa", "he")
fun isRtlLanguage(langTag: String) = langTag in rtlLanguages

expect fun changeLang(lang: String)
