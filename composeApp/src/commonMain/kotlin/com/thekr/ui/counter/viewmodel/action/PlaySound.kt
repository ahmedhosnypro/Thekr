package com.thekr.ui.counter.viewmodel.action

import com.thekr.resources.Res
import com.thekr.ui.counter.viewmodel.ThekrCounterViewModel
import com.thekr.ui.settings.SettingActions.currentSettings
import korlibs.audio.sound.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
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
    private const val MAX_DECODED_BYTES = 16 * 1024 * 1024
    private const val BYTES_PER_PCM_SAMPLE = 2

    private data class CachedSound(val sound: Sound, val bytes: Long)

    private val decodedSounds = LinkedHashMap<String, CachedSound>()
    private var decodedBytes = 0L
    private val decodeMutex = Mutex()
    private var playJob: Job? = null

    @OptIn(ExperimentalResourceApi::class)
    fun ThekrCounterViewModel.onPlayAudio() {
        if (soundChannel?.playing == true) {
            stopPlayer()
            mutableUiState.update { it.copy(isAudioPlaying = false) }
            return
        }
        // A playback coroutine may still be decoding; launching another would
        // play the same sound twice and orphan the first channel.
        if (playJob?.isActive == true) return
        val soundFileName = getCurrentThekr().value.soundFileName
        val filePath = "files/thekr/${currentSettings().currentSheikh}/$soundFileName.mp3"
        playJob = scope.launch {
            val sound = decodeSound(filePath) ?: return@launch
            if (!isActive || soundChannel?.playing == true) return@launch
            val channel = sound.play()
            if (!isActive) {
                channel.stop()
                return@launch
            }
            soundChannel = channel
            mutableUiState.update { it.copy(isAudioPlaying = true) }
            channel.onCompleted(coroutineContext = scope.coroutineContext) {
                // Only clean up if this channel is still the current one;
                // a stale completion callback must not kill a newer playback.
                if (soundChannel === channel) {
                    soundChannel = null
                    mutableUiState.update { it.copy(isAudioPlaying = false) }
                }
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun decodeSound(filePath: String): Sound? = decodeMutex.withLock {
        // Cache hit: re-insert as the most recently used entry so eviction is LRU.
        decodedSounds.remove(filePath)?.let { entry ->
            decodedSounds[filePath] = entry
            return@withLock entry.sound
        }
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
        // A cancelled play job means the screen was disposed mid-decode:
        // playing is skipped by the caller, so don't retain the decode either.
        if (playJob?.isActive != true) return@withLock null
        // Sound.length is always 0 for korlibs 6.0.0 decoded sounds; the wrapped
        // AudioData carries the real decoded size (toAudioData on SoundAudioData
        // returns its cached data without re-decoding).
        val decodedSize = sound.toAudioData().let {
            it.totalSamples.toLong() * it.channels * BYTES_PER_PCM_SAMPLE
        }
        decodedSounds[filePath] = CachedSound(sound, decodedSize)
        decodedBytes += decodedSize
        while (decodedBytes > MAX_DECODED_BYTES || decodedSounds.size > MAX_DECODED_SOUNDS) {
            val eldest = decodedSounds.entries.firstOrNull() ?: break
            decodedSounds.remove(eldest.key)
            decodedBytes -= eldest.value.bytes
        }
        sound
    }

    // Sound holds decoded PCM in memory; dropping all references lets the GC
    // reclaim it, so a plain clear under the decode mutex is a full release.
    fun releaseCache() {
        scope.launch {
            decodeMutex.withLock {
                decodedSounds.clear()
                decodedBytes = 0L
            }
        }
    }

    override fun stopPlayer() {
        // Cancel an in-flight decode/play coroutine so playback cannot start
        // after the screen has been disposed.
        playJob?.cancel()
        playJob = null
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
