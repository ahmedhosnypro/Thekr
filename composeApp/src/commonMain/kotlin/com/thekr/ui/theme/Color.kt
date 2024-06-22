package com.thekr.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.thekr.data.proto.ThemeMode
import com.thekr.ui.util.RtlView

val md_theme_light_primary = Color(0xFF026E10)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFF99F98A)
val md_theme_light_onPrimaryContainer = Color(0xFF002201)
val md_theme_light_secondary = Color(0xFF53634E)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFD6E8CD)
val md_theme_light_onSecondaryContainer = Color(0xFF111F0F)
val md_theme_light_tertiary = Color(0xFF386569)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFBCEBEF)
val md_theme_light_onTertiaryContainer = Color(0xFF002022)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFCFDF6)
val md_theme_light_onBackground = Color(0xFF1A1C19)
val md_theme_light_surface = Color(0xFFFCFDF6)
val md_theme_light_onSurface = Color(0xFF1A1C19)
val md_theme_light_surfaceVariant = Color(0xFFDFE4D8)
val md_theme_light_onSurfaceVariant = Color(0xFF42493F)
val md_theme_light_outline = Color(0xFF73796E)
val md_theme_light_inverseOnSurface = Color(0xFFF1F1EB)
val md_theme_light_inverseSurface = Color(0xFF2F312D)
val md_theme_light_inversePrimary = Color(0xFF7EDC71)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF161515)
val md_theme_light_outlineVariant = Color(0xFFC2C8BC)
val md_theme_light_scrim = Color(0xFF000000)


val md_theme_dark_primary = Color(0xFF944A00)
val md_theme_dark_onPrimary = Color(0xFFFFFFFF)
val md_theme_dark_primaryContainer = Color(0xFFFFDCC6)
val md_theme_dark_onPrimaryContainer = Color(0xFF301400)
val md_theme_dark_secondary = Color(0xFF755845)
val md_theme_dark_onSecondary = Color(0xFFFFFFFF)
val md_theme_dark_secondaryContainer = Color(0xFFFFDCC6)
val md_theme_dark_onSecondaryContainer = Color(0xFF2B1708)
val md_theme_dark_tertiary = Color(0xFF5F6135)
val md_theme_dark_onTertiary = Color(0xFFFFFFFF)
val md_theme_dark_tertiaryContainer = Color(0xFFE4E6AE)
val md_theme_dark_onTertiaryContainer = Color(0xFF1B1D00)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
//val md_theme_dark_error = Color(0xFFBA1A1A)
//val md_theme_dark_errorContainer = Color(0xFFFFDAD6)
//val md_theme_dark_onError = Color(0xFFFFFFFF)
//val md_theme_dark_onErrorContainer = Color(0xFF410002)
//val md_theme_dark_background = Color(0xFF201A17)
//val md_theme_dark_onBackground = Color(0xFFECE0DA)
//val md_theme_dark_surface = Color(0xFF201A17)
//val md_theme_dark_onSurface = Color(0xFFECE0DA)
val md_theme_dark_background = Color.Black
val md_theme_dark_onBackground =  Color.White
val md_theme_dark_surface =  Color.Black
val md_theme_dark_onSurface =  Color.White
val md_theme_dark_surfaceVariant = Color(0xFF52443B)
val md_theme_dark_onSurfaceVariant = Color(0xFFD6C3B7)
val md_theme_dark_outline = Color(0xFF9F8D83)
val md_theme_dark_inverseOnSurface = Color(0xFF201A17)
val md_theme_dark_inverseSurface = Color(0xFFECE0DA)
val md_theme_dark_inversePrimary = Color(0xFFFFB784)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFFFFFFFF)
val md_theme_dark_outlineVariant = Color(0xFFD6C3B7)
val md_theme_dark_scrim = Color(0xFF000000)


val seed = Color(0xFFFF8400)


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AppPreview() {
    AppTheme {
        RtlView {
            Surface {
                Column {
//                    Home(
//                        navController = rememberNavController(),
//                        zekrList = zekrCardListPreviewData(),
//                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AppDarkPreview() {
    AppTheme(ThemeMode.Dark) {
        RtlView {
            Surface {
                Column {
//                    Home(
//                        navController = rememberNavController(),
//                        zekrList = zekrCardListPreviewData(),
//                    )
                }
            }
        }
    }
}