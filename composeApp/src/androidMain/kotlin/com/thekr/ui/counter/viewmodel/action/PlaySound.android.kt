package com.thekr.ui.counter.viewmodel.action

import com.thekr.ThekrApplication.Companion.appContext
import korlibs.audio.sound.SoundAudioStream
import korlibs.audio.sound.SoundChannel
import korlibs.io.android.withAndroidContext

actual suspend fun SoundAudioStream.platformPlay(): SoundChannel = withAndroidContext(appContext) {
    run {
        play()
    }
}