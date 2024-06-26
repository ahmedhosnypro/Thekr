package com.thekr.ui.counter.viewmodel.action


import com.thekr.ui.settings.SettingActions.currentSettings
import korlibs.platform.Platform

fun counterClickFeedBack(
    label: String,
    count: Long,
    clickSound: Boolean = true
) {
    //run ping.mp3 on click
    val currentSettings = currentSettings()
    if (clickSound && currentSettings().sound) {
        if (currentSettings.clickSound) {
            ClickSoundPlayer.clickSound()
        }

//        val speechName = currentSettings.speechName
//        val speechValue = currentSettings.speechValue
//        when {
//            speechName && speechValue -> textToSpeech("$count $label")
//            speechName -> textToSpeech(label)
//            speechValue -> textToSpeech(count.toString())
//        }
    }

    if (Platform.isAndroid && currentSettings.vibration) {
        vibrate()
    }
}

expect fun vibrate()

val heartbeat = longArrayOf(0, 200, 200, 200, 200, 400, 200, 200, 200, 200, 800)
var doublePulse = longArrayOf(0, 200, 200, 200, 200, 500, 200, 200, 200, 200, 500)
var triplePulse =
    longArrayOf(0, 200, 200, 200, 200, 500, 200, 200, 200, 200, 500, 200, 200, 200, 200, 500)
var shortLongAlternating = longArrayOf(0, 200, 200, 200, 200, 800, 200, 200, 200, 200, 800)
var morseSOS = longArrayOf(0, 200, 200, 200, 600, 600, 200, 600, 200, 200, 200)
var ascendingPattern = longArrayOf(0, 100, 200, 300, 400, 500, 600, 700, 800, 900)
var descendingPattern = longArrayOf(0, 900, 800, 700, 600, 500, 400, 300, 200, 100)
var triplePulseWithDelay =
    longArrayOf(0, 200, 200, 200, 200, 500, 200, 200, 200, 200, 500, 200, 200, 200, 200, 800)
val peacefulPulse = longArrayOf(
    500, 500,
    200, 300, 200, 300,
    300, 1000
)
val energeticBuzz = longArrayOf(
    100, 100, 100, 100, 200, 400,
    200, 200, 200, 200, 300, 500,
    100, 200, 300, 400
)
val playfulTickle = longArrayOf(
    50, 50, 50, 50, 100, 200,
    100, 100, 200, 100,
    50, 100, 150, 100
)
val intriguingMorseCode = longArrayOf(
    200, 200,  // Short
    400, 200,  // Long
    200,       // Space between dots
    400,       // Space between letters
    800        // Space between words
)

val patternList = mutableListOf(
    heartbeat,
    doublePulse,
    triplePulse,
    shortLongAlternating,
    morseSOS,
    ascendingPattern,
    descendingPattern,
    triplePulseWithDelay,
    peacefulPulse,
    energeticBuzz,
    playfulTickle,
    intriguingMorseCode
)



//suspend fun alertSleep(
//    player: ExoPlayer?,
//    context: Context,
//    soundFileName: String?,
//) {
//    if (soundFileName != null) {
//        playSound(player, context, soundFileName)
//    } else {
//        playSound(player, context, SoundResourceHelper.getRandomResourceId())
//    }
//    val vibrator = vibrator(context)
//
//    val customizableCrescendo = mutableListOf<Long>()
//
//    var onTime = 100
//    var offTime = 1500
//
//    repeat(10) {
//        customizableCrescendo.add(onTime.toLong())
//        customizableCrescendo.add(offTime.toLong())
//
//        onTime += 50
//        offTime -= 50
//    }
//
//    val crescendoPattern = customizableCrescendo.toLongArray()
//    patternList.add(crescendoPattern)
//    patternList.forEach {
//        @Suppress("DEPRECATION")
//        vibrator.vibrate(it, -1)
//        delay(1000)
//    }
//}