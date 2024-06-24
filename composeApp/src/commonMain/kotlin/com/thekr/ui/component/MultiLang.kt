package com.thekr.ui.component


import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.LayoutDirection
import com.thekr.ui.theme.rtlLanguages
import org.jetbrains.compose.resources.DefaultComposeEnvironment


@Composable
fun MultiLang(
    language: String, content: @Composable () -> Unit
) {

    LaunchedEffect(language) {
        DefaultComposeEnvironment.setLocale(Locale(language))
    }

    if (language in rtlLanguages) {
        RtlView {
            content()
        }
    } else {
        content()
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