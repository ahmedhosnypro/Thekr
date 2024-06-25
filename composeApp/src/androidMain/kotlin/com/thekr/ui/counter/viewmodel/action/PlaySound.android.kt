package com.thekr.ui.counter.viewmodel.action

import androidx.lifecycle.viewModelScope
import com.thekr.ThekrApplication.Companion.appContext
import com.thekr.resources.Res
import com.thekr.ui.counter.viewmodel.ZekrCounterViewModel
import com.thekr.ui.settings.SettingActions.settingState
import korlibs.audio.sound.nativeSoundProvider
import korlibs.io.android.withAndroidContext
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
actual fun ZekrCounterViewModel.playZekrAudio() {
    val soundFileName = getCurrentZekr().value.soundFileName
    val filePath = "files/thekr/${settingState.value.currentSheikh}/${soundFileName}.mp3"

    viewModelScope.launch {
        val bytes= Res.readBytes(filePath)
        val sound = nativeSoundProvider.createSound(data = bytes)
        withAndroidContext(appContext) {
            sound.play()
        }
    }
}

actual fun stopPlayer(viewModel: ZekrCounterViewModel) {
}