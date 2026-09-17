package com.thekr.ui.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.thekr.data.proto.Settings
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.settingsStore
import com.thekr.values.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

object SettingActions {
    private val settingsStateFlow: StateFlow<Settings?> = settingsStore.updates.stateIn(
        CoroutineScope(Dispatchers.IO),
        started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
        initialValue = null,
    )

    // Always-initialized (no lateinit): currentSettings() can never throw
    // UninitializedPropertyAccessException if it runs ahead of the load.
    // Serves defaults until the first disk read lands.
    private val settingState: MutableState<SettingsDetails> = mutableStateOf(SettingsDetails())

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun currentSettings() = settingState.value

    init {
        // Non-blocking init: the first value is loaded asynchronously instead
        // of runBlocking on whatever thread first touches this object (main).
        // Readers run behind CounterApp's gates, by which time the load has
        // landed.
        scope.launch {
            settingState.value =
                runCatching { settingsStore.get() }.getOrNull()?.toSettingsDetails()
                    ?: SettingsDetails()
        }
        scope.launch {
            settingsStateFlow.collect { settings ->
                settings?.let {
                    settingState.value = it.toSettingsDetails()
                }
            }
        }
    }

    private fun persist(settingsDetails: SettingsDetails) {
        scope.launch {
            settingsStore.update { stored ->
                // Merge instead of whole-record replace: the boot flags are
                // owned by initAppData() and must survive settings-screen
                // writes, which would otherwise reset them to the caller's
                // (possibly stale) snapshot values.
                val updated = settingsDetails.toSettings()
                updated.copy(
                    initialized = (stored?.initialized ?: false) || updated.initialized,
                    dbInitialized = (stored?.dbInitialized ?: false) || updated.dbInitialized,
                )
            }
        }
    }

    fun update(settingsDetails: SettingsDetails) {
        persist(settingsDetails)
    }

    private fun suspenseUpdate(settingsDetails: SettingsDetails) {
        settingState.value = settingsDetails
        persist(settingsDetails)
    }

    fun increaseFontSize() {
        if (settingState.value.fontSize < 96) {
            update(
                settingState.value.copy(
                    fontSize = settingState.value.fontSize + 4,
                ),
            )
        }
    }

    fun decreaseFontSize() {
        if (settingState.value.fontSize > 4) {
            update(
                settingState.value.copy(
                    fontSize = settingState.value.fontSize - 4,
                ),
            )
        }
    }

    fun changeThemeMode() {
        val themeMode = when (settingState.value.themeMode) {
            ThemeMode.System -> ThemeMode.Light
            ThemeMode.Light -> ThemeMode.Dark
            ThemeMode.Dark -> ThemeMode.System
        }
        update(
            settingState.value.copy(
                themeMode = themeMode,
            ),
        )
    }

    fun changeCountVisibility() {
        val s = currentSettings()
        suspenseUpdate(
            s.copy(
                showCount = !s.showCount,
            ),
        )
    }
}
