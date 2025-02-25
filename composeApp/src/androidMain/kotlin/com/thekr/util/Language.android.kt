package com.thekr.util

import java.util.Locale

actual fun changeLang(lang: String) {
    val locale = Locale(lang)
    Locale.setDefault(locale)
}
