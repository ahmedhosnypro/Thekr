package com.thekr.ui.counter.viewmodel.action

import com.thekr.ui.counter.viewmodel.action.ThekrSoundPlayer.scope
import korlibs.audio.sound.Sound
import korlibs.audio.sound.SoundAudioStream

actual suspend fun SoundAudioStream.platformPlay(player: SoundPlayer) {
    val channel = play()
    player.soundChannel = channel
    channel.onCompleted(scope.coroutineContext) {
        player.soundChannel = null
    }
}


actual suspend fun Sound.platformPlay() = play()