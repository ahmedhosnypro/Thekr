package com.thekr.ui.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.settingsStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


object SettingActions {
    val settingState: MutableState<SettingsDetails> = mutableStateOf(SettingsDetails())
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        scope.launch {
            settingsStore.updates.collectLatest { settings ->
                settings?.let {
                    settingState.value = it.toSettingsDetails()
                }
            }
        }
    }

    fun update(settingsDetails: SettingsDetails) {
        scope.launch {
            settingsStore.update {
                settingsDetails.toSettings()
            }
        }
    }

    private fun suspenseUpdate(settingsDetails: SettingsDetails){
        settingState.value = settingsDetails
        scope.launch {
            settingsStore.update {
                settingsDetails.toSettings()
            }
        }
    }

    fun increaseFontSize() {
        if (settingState.value.fontSize < 96) {
            update(
                settingState.value.copy(
                    fontSize = settingState.value.fontSize + 4
                )
            )
        }
    }

    fun decreaseFontSize() {
        if (settingState.value.fontSize > 4)
            update(
                settingState.value.copy(
                    fontSize = settingState.value.fontSize - 4
                )
            )
    }

    fun changeThemeMode() {
        val themeMode = when (settingState.value.themeMode) {
            ThemeMode.System -> ThemeMode.Light
            ThemeMode.Light -> ThemeMode.Dark
            ThemeMode.Dark -> ThemeMode.System
        }
        update(
            settingState.value.copy(
                themeMode = themeMode
            )
        )
    }

    fun changeCountVisibility() {
        suspenseUpdate(
            settingState.value.copy(
                showCount = !settingState.value.showCount
            )
        )
    }
}

