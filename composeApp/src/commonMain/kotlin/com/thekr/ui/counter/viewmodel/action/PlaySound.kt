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

expect suspend fun SoundAudioStream.platformPlay()

object ThekrSoundPlayer {
    internal val scope = CoroutineScope(Dispatchers.IO)
    internal var playerSoundChannel: SoundChannel? = null

    private var stream: SoundAudioStream? = null

    @OptIn(ExperimentalResourceApi::class)
    fun ZekrCounterViewModel.playZekrAudio() {
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
            stopPlayer()
            soundAudioStream.platformPlay()
            stream = soundAudioStream
        }
    }


    fun stopPlayer() {
        if (playerSoundChannel != null) {
            playerSoundChannel?.stop()
            stream?.closeStream
            playerSoundChannel = null
        }
    }

    val isPlaying: Boolean get() = playerSoundChannel?.playing == true
}