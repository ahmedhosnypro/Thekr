package com.thekr.ui.counter.viewmodel.action


import com.thekr.resources.Res
import com.thekr.ui.counter.viewmodel.ZekrCounterViewModel
import com.thekr.ui.settings.SettingActions.settingState
import korlibs.audio.sound.PlatformAudioOutput
import korlibs.audio.sound.nativeSoundProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

expect suspend fun PlatformAudioOutput.platformStart()

object ThekrSoundPlayer {
    private lateinit var platformAudioOutput: PlatformAudioOutput
    private val scope = CoroutineScope(Dispatchers.IO)

    @OptIn(ExperimentalResourceApi::class)
    fun ZekrCounterViewModel.playZekrAudio() {
        val soundFileName = getCurrentZekr().value.soundFileName
        val filePath = "files/thekr/${settingState.value.currentSheikh}/${soundFileName}.mp3"
        scope.launch {
            val bytes = Res.readBytes(filePath)
            val sound = nativeSoundProvider.createSound(data = bytes)
            val audioData = sound.toAudioData()

            init()
            platformAudioOutput.add(audioData)
            platformAudioOutput.platformStart()
        }
    }

    private suspend fun init() {
        if (::platformAudioOutput.isInitialized) platformAudioOutput.close()
        platformAudioOutput = nativeSoundProvider.createPlatformAudioOutput()
    }

    fun stopPlayer(viewModel: ZekrCounterViewModel) {
        platformAudioOutput.close()
    }
}