package com.thekr.ui.util

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.thekr.ui.theme.rtlLanguages

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun changeLocales(context: Context, localeString: String) {
    context.getSystemService(LocaleManager::class.java)
        .applicationLocales = LocaleList.forLanguageTags(localeString)
}

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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