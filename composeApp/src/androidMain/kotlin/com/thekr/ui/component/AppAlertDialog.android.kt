package com.thekr.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.thekr.data.proto.ThemeMode
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.component.AppAlertDialog
import com.thekr.ui.component.LocalizedApp

@Composable
@Preview(locale = "ar")
fun AppTopBarPreviewLight() {
    LocalizedApp {
        AppTheme {
            Surface {
                AppAlertDialog(
                    onDismissRequest = {},
                    onConfirmation = {},
                    dialogTitle = "نص العنوان",
                    dialogText = "نص الرسالة",
                    icon = Icons.Default.Info
                )
            }
        }
    }
}

@Composable
@Preview(locale = "en")
fun AppTopBarPreviewDark() {
    LocalizedApp {
        AppTheme(themeMode = ThemeMode.Dark) {
            Surface {
                AppAlertDialog(
                    onDismissRequest = {},
                    onConfirmation = {},
                    dialogTitle = "Dialog Title",
                    dialogText = "Dialog Text",
                    icon = Icons.Default.Info
                )
            }
        }
    }
}
