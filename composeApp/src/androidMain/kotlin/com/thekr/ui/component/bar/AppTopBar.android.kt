package com.thekr.ui.component.bar

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.component.bar.AppBarPreviewTemplate

@Composable
@Preview(locale = "ar")
fun AppTopBarPreviewLight() {
    AppTheme {
        Surface {
            AppBarPreviewTemplate()
        }
    }
}

@Preview
@Composable
fun AppTopBarPreviewDark() {
    AppTheme(themeMode = ThemeMode.Dark) {
        Surface {
            AppBarPreviewTemplate(
                settingsDetails = SettingsDetails(
                    themeMode = ThemeMode.Dark
                )
            )
        }
    }
}



