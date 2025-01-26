package com.thekr.ui.counter.viewmodel.action

import com.thekr.resources.Res
import com.thekr.ui.counter.viewmodel.ZekrCounterViewModel
import com.thekr.ui.settings.SettingActions.currentSettings
import korlibs.audio.sound.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

expect suspend fun SoundAudioStream.platformPlay(player: SoundPlayer)
expect suspend fun Sound.platformPlay(): SoundChannel

interface SoundPlayer {
    var soundChannel: SoundChannel?

    fun stopPlayer()
}

object ThekrSoundPlayer : SoundPlayer {
    internal val scope = CoroutineScope(Dispatchers.Default)
    override var soundChannel: SoundChannel? = null

    private var stream: SoundAudioStream? = null

    @OptIn(ExperimentalResourceApi::class)
    fun ZekrCounterViewModel.playZekrAudio() {
        val soundFileName = getCurrentZekr().value.soundFileName
        val filePath = "files/thekr/${currentSettings().currentSheikh}/${soundFileName}.mp3"

        scope.launch {
            val bytes = try {
                Res.readBytes(filePath)
            } catch (e: Exception) {
                return@launch
            }
            val sound = nativeSoundProvider.createSound(data = bytes)
            val audioStream = sound.toStream()
            val soundAudioStream = SoundAudioStream(
                stream = audioStream,
                coroutineContext = scope.coroutineContext,
                soundProvider = nativeSoundProvider
            )
            stopPlayer()
            soundAudioStream.platformPlay(this@ThekrSoundPlayer)
            stream = soundAudioStream
        }
    }


    override fun stopPlayer() {
        if (soundChannel != null) {
            soundChannel?.stop()
            stream?.closeStream
            soundChannel = null
        }
    }

    val isPlaying: Boolean get() = soundChannel?.playing == true
}


@OptIn(ExperimentalResourceApi::class)
object ClickSoundPlayer : SoundPlayer {
    private val scope = CoroutineScope(Dispatchers.Main)
    override var soundChannel: SoundChannel? = null

//    private var stream: SoundAudioStream? = null

    private lateinit var sound: Sound

    // todo: dynamic click sound
    private const val filePath = "files/alert/click_1.mp3"

    init {
        scope.launch {
            val bytes = try {
                Res.readBytes(filePath)
            } catch (e: Exception) {
                return@launch
            }
            sound = nativeSoundProvider.createSound(data = bytes)
        }
    }

    fun clickSound() {
        scope.launch {
//            stopPlayer()
            sound.platformPlay()
        }
    }


    override fun stopPlayer() {
//        if (soundChannel != null) {
//            soundChannel?.stop()
//            soundChannel = null
//        }
    }
}