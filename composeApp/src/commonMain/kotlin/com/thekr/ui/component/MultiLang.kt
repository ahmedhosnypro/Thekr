package com.thekr.ui.component


import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

val rtlLanguages = listOf("ar", "fa", "he")

@Composable
fun MultiLang(
    language: String, content: @Composable () -> Unit
) {

//    LaunchedEffect(language) {
//        val locale = try {
//            Locale(language)
//        } catch (e: Exception) {
//            null
//        }
//        locale?.let {
//            DefaultComposeEnvironment.setLocale(it)
//        }
//    }

    if (isRtlLanguage(language)) {
        RtlView {
            content()
        }
    } else {
        content()
    }
}

fun isRtlLanguage(langTag: String) = langTag in rtlLanguages

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