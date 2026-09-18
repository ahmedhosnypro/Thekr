package com.thekr.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import com.thekr.ui.util.findActivity

@Composable
internal actual fun SystemAppearance(isLight: Boolean) {
    val view = LocalView.current
    LaunchedEffect(isLight) {
        val window = view.context.findActivity().window
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = isLight
            isAppearanceLightNavigationBars = isLight
        }
    }
}
