package com.thekr.ui.component


import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.thekr.ui.theme.rtlLanguages


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