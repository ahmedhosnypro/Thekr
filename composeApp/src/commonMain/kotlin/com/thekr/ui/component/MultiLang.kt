package com.thekr.ui.component


import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.LayoutDirection
import com.thekr.ui.theme.rtlLanguages

val LocalAppLocale = staticCompositionLocalOf { Locale("ar") }


@Composable
 fun MultiLang(
    language: String, content: @Composable () -> Unit
){
    val locale = Locale(language)
    CompositionLocalProvider(LocalAppLocale provides locale) {
        if (language in rtlLanguages) {
            RtlView {
                content()
            }
        } else {
            content()
        }
    }
}


@Composable
fun RtlView(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        content()
    }
}