package com.thekr.ui.counter.viewmodel.action

import com.thekr.ui.counter.viewmodel.action.ThekrSoundPlayer.playerSoundChannel
import com.thekr.ui.counter.viewmodel.action.ThekrSoundPlayer.scope
import korlibs.audio.sound.SoundAudioStream

actual  suspend fun SoundAudioStream.platformPlay() {
    val channel = play()
    playerSoundChannel = channel
    channel.onCompleted(scope.coroutineContext) {
        playerSoundChannel = null
    }
}
