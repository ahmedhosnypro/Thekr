package com.thekr.data.settings

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.thekr.data.proto.Settings
import com.thekr.data.proto.SwapDirection
import com.thekr.data.proto.ThemeMode
import com.thekr.util.TimeHelper.now

@Stable
@Immutable
data class SettingsDetails(
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
    val vibration: Boolean = true,
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
) {
    fun toSettings() = Settings(
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
        showCount = showCount,
        showDailyCount = showDailyCount,
        showWeeklyCount = showWeeklyCount,
        showMonthlyCount = showMonthlyCount,
        showYearlyCount = showYearlyCount,
        showTotalCount = showTotalCount,
        showSessionCount = showSessionCount,
        initialized = initialized,
        dbInitialized = dbInitialized,
        lastUpdate = lastUpdate
    )
}