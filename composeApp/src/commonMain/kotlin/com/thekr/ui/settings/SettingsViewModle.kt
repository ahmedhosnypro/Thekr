package com.thekr.ui.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.settings.SettingsHelper
import com.thekr.data.settingsStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SettingViewModel(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    val viewState: MutableState<SettingsDetails> = mutableStateOf(SettingsDetails())

    init {
        viewModelScope.launch {
            settingsStore.updates.collectLatest { settings ->
                settings?.let {
                    viewState.value = it.toSettingsDetails()
                }
            }
        }
    }

    fun update(settingsDetails: SettingsDetails) {
        viewModelScope.launch(ioDispatcher) {
            viewState.value = settingsDetails
            settingsStore.update {
                settingsDetails.toSettings()
            }
        }
    }


    private fun updateSuspended(settingsUiSate: SettingsDetails) {
        runBlocking {
            viewState.value = settingsUiSate
            settingsStore.update {
                settingsUiSate.toSettings()
            }
            SettingsHelper.updateState(settingsUiSate)
        }
    }

    fun increaseFontSize() {
        if (viewState.value.fontSize < 96) {
            update(
                viewState.value.copy(
                    fontSize = viewState.value.fontSize + 4
                )
            )
        }
    }

    fun decreaseFontSize() {
        if (viewState.value.fontSize > 4)
            update(
                viewState.value.copy(
                    fontSize = viewState.value.fontSize - 4
                )
            )
    }

    fun changeThemeMode() {
        val themeMode = when (viewState.value.themeMode) {
            ThemeMode.System -> ThemeMode.Light
            ThemeMode.Light -> ThemeMode.Dark
            ThemeMode.Dark -> ThemeMode.System
        }
        update(
            viewState.value.copy(
                themeMode = themeMode
            )
        )
    }

    fun changeCountVisibility() {
        updateSuspended(
            viewState.value.copy(
                showCount = !viewState.value.showCount
            )
        )
    }
}

