package com.thekr.ui.counter.viewmodel.action


import com.thekr.resources.Res
import com.thekr.ui.counter.viewmodel.ZekrCounterViewModel
import com.thekr.ui.settings.SettingActions.settingState
import korlibs.audio.sound.PlatformAudioOutput
import korlibs.audio.sound.Sound
import korlibs.audio.sound.SoundAudioStream
import korlibs.audio.sound.SoundChannel
import korlibs.audio.sound.nativeSoundProvider
import korlibs.audio.sound.playing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

expect suspend fun SoundAudioStream.platformPlay(): SoundChannel

object ThekrSoundPlayer {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var playerSoundChannel: SoundChannel? = null

    @OptIn(ExperimentalResourceApi::class)
    fun ZekrCounterViewModel.playZekrAudio() {
        stopPlayer()

        val soundFileName = getCurrentZekr().value.soundFileName
        val filePath = "files/thekr/${settingState.value.currentSheikh}/${soundFileName}.mp3"

        scope.launch {
            val bytes = Res.readBytes(filePath)
            val sound = nativeSoundProvider.createSound(data = bytes)
            val audioStream = sound.toStream()
            val soundAudioStream = SoundAudioStream(
                stream = audioStream,
                coroutineContext = scope.coroutineContext,
                soundProvider = nativeSoundProvider
            )

            val channel = soundAudioStream.platformPlay()

            playerSoundChannel = channel
            channel.onCompleted(scope.coroutineContext) {
                playerSoundChannel = null
            }
        }
    }


    fun stopPlayer() {
        if (playerSoundChannel != null) {
            playerSoundChannel?.stop()
            playerSoundChannel = null
        }
    }

    val isPlaying: Boolean get() = playerSoundChannel?.playing == true
}