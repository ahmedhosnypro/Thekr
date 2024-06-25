package com.thekr.ui.counter.viewmodel.action

import com.thekr.ThekrApplication.Companion.appContext
import com.thekr.ui.counter.viewmodel.action.ThekrSoundPlayer.playerSoundChannel
import com.thekr.ui.counter.viewmodel.action.ThekrSoundPlayer.scope
import korlibs.audio.sound.SoundAudioStream
import korlibs.io.android.withAndroidContext

actual suspend fun SoundAudioStream.platformPlay() = withAndroidContext(appContext) {
    val channel = play()
    playerSoundChannel = channel
    channel.onCompleted(scope.coroutineContext) {
        playerSoundChannel = null
    }
}