package com.thekr.ui.util

import android.app.LocaleManager
import android.content.Context
import android.os.LocaleList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.thekr.ui.theme.rtlLanguages

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


@Composable
fun LtrView(

    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Ltr
    ) {
        content()
    }
}

fun changeLocales(context: Context, localeString: String) {
    context.getSystemService(LocaleManager::class.java)
        .applicationLocales = LocaleList.forLanguageTags(localeString)
}

@Composable
fun MultiLang(
    language: String,
    content: @Composable () -> Unit
) {
    // setup language
    changeLocales(context = LocalContext.current, language)

    if (language in rtlLanguages) {
        RtlView {
            content()
        }
    } else {
        content()
    }
}