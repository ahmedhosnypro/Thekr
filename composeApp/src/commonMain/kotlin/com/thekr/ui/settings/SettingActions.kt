package com.thekr.ui.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.thekr.values.Constants
import com.thekr.data.proto.Settings
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.settingsStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


object SettingActions {
    private val _settingState: StateFlow<Settings?> = settingsStore.updates.stateIn(
        CoroutineScope(Dispatchers.IO),
        started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
        initialValue = null
    )

    private lateinit var settingState: MutableState<SettingsDetails>

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun currentSettings() = settingState.value

    init {
        runBlocking {
            launch {
                settingState = mutableStateOf(
                    settingsStore.get()?.toSettingsDetails() ?: SettingsDetails()
                )
            }
        }
        scope.launch {
            _settingState.collect { settings ->
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

    private fun suspenseUpdate(settingsDetails: SettingsDetails) {
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
        val s = currentSettings()
        suspenseUpdate(
            s.copy(
                showCount = !s.showCount
            )
        )
    }
}

