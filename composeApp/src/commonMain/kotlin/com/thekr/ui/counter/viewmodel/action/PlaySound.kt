package com.thekr.ui.counter.viewmodel.action

import com.thekr.resources.Res
import com.thekr.ui.counter.viewmodel.ThekrCounterViewModel
import com.thekr.ui.settings.SettingActions.currentSettings
import korlibs.audio.sound.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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

    private const val MAX_DECODED_SOUNDS = 16
    private val decodedSounds = LinkedHashMap<String, Sound>()
    private val decodeMutex = Mutex()

    @OptIn(ExperimentalResourceApi::class)
    fun ThekrCounterViewModel.onPlayAudio() {
        if (soundChannel?.playing == true) {
            stopPlayer()
            return
        }
        val soundFileName = getCurrentThekr().value.soundFileName
        val filePath = "files/thekr/${currentSettings().currentSheikh}/${soundFileName}.mp3"
        scope.launch {
            val sound = decodeSound(filePath) ?: return@launch
            soundChannel = sound.play()
            updateUiState(uiState.value.copy(isAudioPlaying = true))
            soundChannel?.onCompleted(coroutineContext = scope.coroutineContext) {
                stopPlayer()
                updateUiState(uiState.value.copy(isAudioPlaying = false))
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun decodeSound(filePath: String): Sound? = decodeMutex.withLock {
        decodedSounds[filePath] ?: run {
            val bytes = try {
                Res.readBytes(filePath)
            } catch (e: Exception) {
                return@withLock null
            }
            val sound = try {
                nativeSoundProvider.createSound(data = bytes)
            } catch (e: Exception) {
                return@withLock null
            }
            decodedSounds[filePath] = sound
            if (decodedSounds.size > MAX_DECODED_SOUNDS) {
                decodedSounds.remove(decodedSounds.keys.first())
            }
            sound
        }
    }


    override fun stopPlayer() {
        if (soundChannel != null) {
            soundChannel?.stop()
            soundChannel = null
        }
    }

    val isPlaying: Boolean get() = soundChannel?.playing == true
}


@OptIn(ExperimentalResourceApi::class)
object ClickSoundPlayer : SoundPlayer {
    private val scope = CoroutineScope(Dispatchers.Main)
    override var soundChannel: SoundChannel? = null

    private var sound: Sound? = null

    // todo: dynamic click sound
    private const val PATH = "files/alert/click_1.mp3"

    init {
        scope.launch {
            val bytes = try {
                Res.readBytes(PATH)
            } catch (e: Exception) {
                return@launch
            }
            sound = nativeSoundProvider.createSound(data = bytes)
        }
    }

    fun clickSound() {
        val currentSound = sound ?: return
        scope.launch {
//            stopPlayer()
            currentSound.platformPlay()
        }
    }


    override fun stopPlayer() {
//        if (soundChannel != null) {
//            soundChannel?.stop()
//            soundChannel = null
//        }
    }
}