package com.thekr.data.proto

import com.thekr.data.settings.SettingsDetails
import com.thekr.util.TimeHelper.now
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val language: String = "ar", // ar, en
    val themeMode: ThemeMode = ThemeMode.Light,
    val fontSize: Float = 16f,
    val materialYou: Boolean = false,
//    val colorSchemeDetails: ColorSchemeDetails = ColorSchemeDetails.newBuilder()
//        .setHue(defaultThemePrimary().hue)
//        .setSaturation(defaultThemePrimary().saturation)
//        .setValue(defaultThemePrimary().value)
//        .setAlpha(defaultThemePrimary().alpha)
//        .build(),

//    val colorSchemeDetails: ColorSchemeDetails = ColorSchemeDetails(
//        TODO()
//    ),
    val screenAlwaysOn: Boolean = false,
    val vibration: Boolean = false,
    val sound: Boolean = true,
    val clickSound: Boolean = true,
    val speechValue: Boolean = false,
    val speechName: Boolean = false,
    val volumeControl: Boolean = false,
    val fingerPrintControl: Boolean = false,
    val currentSheikh: String = "FasilBnGazyan",
    val swapDirection: SwapDirection = SwapDirection.Horizontal,

    val showCount: Boolean = true,
    val showDailyCount: Boolean = true,
    val showWeeklyCount: Boolean = true,
    val showMonthlyCount: Boolean = true,
    val showYearlyCount: Boolean = true,
    val showTotalCount: Boolean = true,
    val showSessionCount: Boolean = true,

    val initialized: Boolean = false,
    val dbInitialized: Boolean = false,
    val lastUpdate: Long = now()
){
    fun toSettingsDetails(): SettingsDetails = SettingsDetails(
        language = language,
        themeMode = themeMode,
        fontSize = fontSize,
        materialYou = materialYou,
//        colorSchemeDetails = colorSchemeDetails,
        screenAlwaysOn = screenAlwaysOn,
        vibration = vibration,
        sound = sound,
        clickSound = clickSound,
        speechValue = speechValue,
        speechName = speechName,
        volumeControl = volumeControl,
        fingerPrintControl = fingerPrintControl,
        currentSheikh = currentSheikh,
        swapDirection = swapDirection,
        // count
        showCount = showCount,
        showDailyCount = showDailyCount,
        showWeeklyCount = showWeeklyCount,
        showMonthlyCount = showMonthlyCount,
        showYearlyCount = showYearlyCount,
        showTotalCount = showTotalCount,
        showSessionCount = showSessionCount,

        initialized = initialized,
        dbInitialized = dbInitialized,
        lastUpdate = this.lastUpdate
    )

}

enum class SwapDirection {
    Horizontal,
    Vertical
}

enum class ThemeMode {
    Dark,
    Light,
    System
}

data class ColorSchemeDetails(
    val hue: Float = 0f,
    val saturation: Float = 0f,
    val value: Float = 0f,
    val alpha: Float = 0f,
    val contrast: Double = 0.0,
    val paletteStyle: PaletteStyle = PaletteStyle.TonalSpot
)

enum class PaletteStyle {
    TonalSpot,
    Neutral,
    Vibrant,
    Expressive,
    Rainbow,
    FruitSalad,
    Monochrome,
    Fidelity,
    Content
}