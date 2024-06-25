package com.thekr.ui.counter.viewmodel.action

import korlibs.audio.sound.PlatformAudioOutput

actual suspend fun PlatformAudioOutput.platformStart() = start()