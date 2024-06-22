package com.thekr.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.godaddy.android.colorpicker.HsvColor
import com.kyant.m3color.dynamiccolor.MaterialDynamicColors
import com.kyant.m3color.hct.Hct
import com.kyant.m3color.scheme.SchemeContent
import com.kyant.m3color.scheme.SchemeExpressive
import com.kyant.m3color.scheme.SchemeFidelity
import com.kyant.m3color.scheme.SchemeFruitSalad
import com.kyant.m3color.scheme.SchemeMonochrome
import com.kyant.m3color.scheme.SchemeNeutral
import com.kyant.m3color.scheme.SchemeRainbow
import com.kyant.m3color.scheme.SchemeTonalSpot
import com.kyant.m3color.scheme.SchemeVibrant
import com.thekr.data.proto.ColorSchemeDetails
import com.thekr.data.proto.PaletteStyle
import com.thekr.data.proto.ThemeMode

private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onErrorContainer = md_theme_light_onErrorContainer,
    outline = md_theme_light_outline,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    inverseSurface = md_theme_light_inverseSurface,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)


private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onErrorContainer = md_theme_dark_onErrorContainer,
    outline = md_theme_dark_outline,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    inverseSurface = md_theme_dark_inverseSurface,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

fun defaultThemePrimary(): HsvColor {
    return HsvColor.from(
        md_theme_dark_primary
    )
}

fun dynamicColorScheme(
    hct: Hct,
    paletteStyle: PaletteStyle,
    contrast: Double,
    isDark: Boolean,
): ColorScheme {
    return colorScheme(
        hct = hct,
        paletteStyle = paletteStyle,
        contrast = contrast,
        isDark = isDark
    )
}

/** Create a dynamic Material 3 color scheme. */
fun dynamicColorScheme(
    colorSchemeDetails: ColorSchemeDetails,
    isDark: Boolean,
): ColorScheme {
    val hsvColor = HsvColor(
        hue = colorSchemeDetails.hue,
        saturation = colorSchemeDetails.saturation,
        value = colorSchemeDetails.value,
        alpha = colorSchemeDetails.alpha
    )

    val hct = Hct.fromInt(hsvColor.toColor().toArgb())
    return colorScheme(
        hct = hct,
        paletteStyle = colorSchemeDetails.paletteStyle,
        contrast = colorSchemeDetails.contrast,
        isDark = isDark
    )
}

private fun colorScheme(
    hct: Hct?,
    paletteStyle: PaletteStyle,
    contrast: Double,
    isDark: Boolean
): ColorScheme {
    val colors = MaterialDynamicColors()
    val scheme = when (paletteStyle) {
        PaletteStyle.TonalSpot -> SchemeTonalSpot(hct, isDark, contrast)
        PaletteStyle.Neutral -> SchemeNeutral(hct, isDark, contrast)
        PaletteStyle.Vibrant -> SchemeVibrant(hct, isDark, contrast)
        PaletteStyle.Expressive -> SchemeExpressive(hct, isDark, contrast)
        PaletteStyle.Rainbow -> SchemeRainbow(hct, isDark, contrast)
        PaletteStyle.FruitSalad -> SchemeFruitSalad(hct, isDark, contrast)
        PaletteStyle.Monochrome -> SchemeMonochrome(hct, isDark, contrast)
        PaletteStyle.Fidelity -> SchemeFidelity(hct, isDark, contrast)
        PaletteStyle.Content -> SchemeContent(hct, isDark, contrast)
        else -> SchemeTonalSpot(hct, isDark, contrast)
    }

    return ColorScheme(
        background = Color(colors.background().getArgb(scheme)),
        error = Color(colors.error().getArgb(scheme)),
        errorContainer = Color(colors.errorContainer().getArgb(scheme)),
        inverseOnSurface = Color(colors.inverseOnSurface().getArgb(scheme)),
        inversePrimary = Color(colors.inversePrimary().getArgb(scheme)),
        inverseSurface = Color(colors.inverseSurface().getArgb(scheme)),
        onBackground = Color(colors.onBackground().getArgb(scheme)),
        onError = Color(colors.onError().getArgb(scheme)),
        onErrorContainer = Color(colors.onErrorContainer().getArgb(scheme)),
        onPrimary = Color(colors.onPrimary().getArgb(scheme)),
        onPrimaryContainer = Color(colors.onPrimaryContainer().getArgb(scheme)),
        onSecondary = Color(colors.onSecondary().getArgb(scheme)),
        onSecondaryContainer = Color(colors.onSecondaryContainer().getArgb(scheme)),
        onSurface = Color(colors.onSurface().getArgb(scheme)),
        onSurfaceVariant = Color(colors.onSurfaceVariant().getArgb(scheme)),
        onTertiary = Color(colors.onTertiary().getArgb(scheme)),
        onTertiaryContainer = Color(colors.onTertiaryContainer().getArgb(scheme)),
        outline = Color(colors.outline().getArgb(scheme)),
        outlineVariant = Color(colors.outlineVariant().getArgb(scheme)),
        primary = Color(colors.primary().getArgb(scheme)),
        primaryContainer = Color(colors.primaryContainer().getArgb(scheme)),
        scrim = Color(colors.scrim().getArgb(scheme)),
        secondary = Color(colors.secondary().getArgb(scheme)),
        secondaryContainer = Color(colors.secondaryContainer().getArgb(scheme)),
        surface = Color(colors.surface().getArgb(scheme)),
        surfaceTint = Color(colors.surfaceTint().getArgb(scheme)),
        surfaceVariant = Color(colors.surfaceVariant().getArgb(scheme)),
        tertiary = Color(colors.tertiary().getArgb(scheme)),
        tertiaryContainer = Color(colors.tertiaryContainer().getArgb(scheme))
    )
}

@Composable
fun MyMaterialTheme(
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = typography,
        content = content
    )
}

val rtlLanguages = listOf("ar", "fa", "he")


/**
 * Applies system bar colors and content padding based on the current
 * theme.
 */
@Composable
private fun SystemBarColorsAndPadding(isDarkTheme: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, false)

            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()

            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !isDarkTheme
            insetsController.isAppearanceLightNavigationBars = !isDarkTheme
        }
    }
}

/** The default theme for the app. */
@Composable
fun AppTheme(
    themeMode: ThemeMode = ThemeMode.System,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val isDarkTheme = when (themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Dark -> true
        ThemeMode.Light -> false
        ThemeMode.UNRECOGNIZED -> false
    }

    // Decide which color scheme to use
    val colorScheme = if (dynamicColor) {
        if (isDarkTheme) DarkColors else LightColors
    } else {
        dynamicColorScheme(
            hct = Hct.fromInt(md_theme_dark_primary.toArgb()),
            paletteStyle = PaletteStyle.TonalSpot,
            contrast = 1.0,
            isDark = isDarkTheme
        )
    }

    SystemBarColorsAndPadding(isDarkTheme)

    MyMaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Create a new Material 3 theme.
 * By generating a color scheme based on the
 * passed parameters.
 */
@Composable
fun CustomTheme(
    themeMode: ThemeMode = ThemeMode.System,
    colorSchemeDetails: ColorSchemeDetails,
    content: @Composable () -> Unit,
) {
    val isDarkTheme = when (themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Dark -> true
        ThemeMode.Light -> false
        ThemeMode.UNRECOGNIZED -> false
    }
    val colorScheme = dynamicColorScheme(
        colorSchemeDetails = colorSchemeDetails,
        isDark = isDarkTheme,
    )

    SystemBarColorsAndPadding(isDarkTheme)

    MyMaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
