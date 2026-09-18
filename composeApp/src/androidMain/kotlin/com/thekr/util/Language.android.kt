package com.thekr.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

actual fun changeLang(lang: String) {
    val locale = Locale.forLanguageTag(lang)
    Locale.setDefault(locale)
    val appLocales = LocaleListCompat.forLanguageTags(lang)
    if (AppCompatDelegate.getApplicationLocales() != appLocales) {
        AppCompatDelegate.setApplicationLocales(appLocales)
    }
}
