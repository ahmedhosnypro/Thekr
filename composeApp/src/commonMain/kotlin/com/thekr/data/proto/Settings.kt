package com.thekr.data.proto

import com.thekr.data.settings.SettingsDetails
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val language: String = "",
    val themeMode: ThemeMode = ThemeMode.Dark,
    val screenAlwaysOn: Boolean = false,
    val vibration: Boolean = false,
    val sound: Boolean = false,
    val clickSound: Boolean = false,
    val speechValue: Boolean = false,
    val speechName: Boolean = false,
    val volumeControl: Boolean = false,
    val fingerPrintControl: Boolean = false,
    val fontSize: Float = 0f,
    val materialYou: Boolean = false,
//    val colorSchemeDetails: ColorSchemeDetails = ColorSchemeDetails(
//        TODO()
//    ),
    val currentSheikh: String = "",
    val swapDirection: SwapDirection = SwapDirection.Horizontal,
    val showCount: Boolean = false,
    val showDailyCount: Boolean = false,
    val showWeeklyCount: Boolean = false,
    val showMonthlyCount: Boolean = false,
    val showYearlyCount: Boolean = false,
    val showTotalCount: Boolean = false,
    val showSessionCount: Boolean = false,
    val dbInitialized: Boolean = false,
    val initialized: Boolean = false,
    val lastUpdate: Long = 0L
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