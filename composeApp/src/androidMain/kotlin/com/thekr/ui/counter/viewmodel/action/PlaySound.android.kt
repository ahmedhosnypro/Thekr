package com.thekr.ui.counter.viewmodel.action

import com.thekr.ThekrApplication.Companion.appContext
import korlibs.audio.sound.PlatformAudioOutput
import korlibs.audio.sound.SoundChannel
import korlibs.io.android.withAndroidContext

actual suspend fun PlatformAudioOutput.platformStart() {
    withAndroidContext(appContext) {
        start()
    }
}