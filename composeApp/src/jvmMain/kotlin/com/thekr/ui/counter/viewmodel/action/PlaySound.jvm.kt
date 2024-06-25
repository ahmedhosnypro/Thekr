package com.thekr.ui.counter.viewmodel.action

import korlibs.audio.sound.SoundAudioStream
import korlibs.audio.sound.SoundChannel

actual suspend fun SoundAudioStream.platformPlay(): SoundChannel = play()
