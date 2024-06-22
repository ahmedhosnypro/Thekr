//package com.thekr.ui.zekr.component
//
//import android.content.Context
//import android.content.Context.VIBRATOR_SERVICE
//import android.os.Build
//import android.os.Vibrator
//import android.os.VibratorManager
//import androidx.media3.common.MediaItem
//import androidx.media3.exoplayer.ExoPlayer
//import com.thekr.data.settings.SettingsHelper.settingsDetails
//import com.thekr.ui.counter.viewModel.ZekrCounterViewModel
//import com.thekr.ui.theme.SoundResourceHelper
//import com.thekr.ui.util.TTSSpeaker.textToSpeech
//import kotlinx.coroutines.delay
//
//
//fun counterClickFeedBack(
//    context: Context,
//    label: String,
//    count: Long,
//    clickSound: Boolean = true
//) {
//    //run ping.mp3 on click
//    if (clickSound && settingsDetails.sound) {
//        if (settingsDetails.clickSound) {
//            ZekrCounterViewModel.clickSoundPlayer?.seekTo(0)
//            ZekrCounterViewModel.clickSoundPlayer?.playWhenReady = true
//        }
//
//        val speechName = settingsDetails.speechName
//        val speechValue = settingsDetails.speechValue
//        when {
//            speechName && speechValue -> textToSpeech(
//                context, "$count $label"
//            )
//
//            speechName -> textToSpeech(context, label)
//            speechValue -> textToSpeech(context, count.toString())
//        }
//    }
//
//    if (settingsDetails.vibration) {
//        @Suppress("DEPRECATION")
//        vibrator(context).vibrate(longArrayOf(0, 16), -1)
//    }
//}
//
//
//fun playSound(player: ExoPlayer?, context: Context, soundFileName: String?) {
//    if (soundFileName != null) {
//        val soundResourceId = SoundResourceHelper.getResourceIdFromFileName(soundFileName)
//
//        if (soundResourceId != null) {
//            val mediaItem =
//                MediaItem.fromUri("android.resource://${context.packageName}/$soundResourceId")
//
//            player?.stop()
//            player?.setMediaItem(mediaItem)
//            player?.prepare()
//            player?.play()
//
//        }
//    }
//}
//
//fun playSound(player: ExoPlayer?, context: Context, soundResourceId: Int) {
//    val mediaItem =
//        MediaItem.fromUri("android.resource://${context.packageName}/$soundResourceId")
//
//    player?.stop()
//    player?.setMediaItem(mediaItem)
//    player?.prepare()
//    player?.play()
//
//}
//
//
//val heartbeat = longArrayOf(0, 200, 200, 200, 200, 400, 200, 200, 200, 200, 800)
//var doublePulse = longArrayOf(0, 200, 200, 200, 200, 500, 200, 200, 200, 200, 500)
//var triplePulse =
//    longArrayOf(0, 200, 200, 200, 200, 500, 200, 200, 200, 200, 500, 200, 200, 200, 200, 500)
//var shortLongAlternating = longArrayOf(0, 200, 200, 200, 200, 800, 200, 200, 200, 200, 800)
//var morseSOS = longArrayOf(0, 200, 200, 200, 600, 600, 200, 600, 200, 200, 200)
//var ascendingPattern = longArrayOf(0, 100, 200, 300, 400, 500, 600, 700, 800, 900)
//var descendingPattern = longArrayOf(0, 900, 800, 700, 600, 500, 400, 300, 200, 100)
//var triplePulseWithDelay =
//    longArrayOf(0, 200, 200, 200, 200, 500, 200, 200, 200, 200, 500, 200, 200, 200, 200, 800)
//val peacefulPulse = longArrayOf(
//    500, 500,
//    200, 300, 200, 300,
//    300, 1000
//)
//val energeticBuzz = longArrayOf(
//    100, 100, 100, 100, 200, 400,
//    200, 200, 200, 200, 300, 500,
//    100, 200, 300, 400
//)
//val playfulTickle = longArrayOf(
//    50, 50, 50, 50, 100, 200,
//    100, 100, 200, 100,
//    50, 100, 150, 100
//)
//val intriguingMorseCode = longArrayOf(
//    200, 200,  // Short
//    400, 200,  // Long
//    200,       // Space between dots
//    400,       // Space between letters
//    800        // Space between words
//)
//
//val patternList = mutableListOf(
//    heartbeat,
//    doublePulse,
//    triplePulse,
//    shortLongAlternating,
//    morseSOS,
//    ascendingPattern,
//    descendingPattern,
//    triplePulseWithDelay,
//    peacefulPulse,
//    energeticBuzz,
//    playfulTickle,
//    intriguingMorseCode
//)
//
//
//fun vibrator(context: Context) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//    val vibratorManager =
//        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
//    vibratorManager.defaultVibrator
//} else {
//    @Suppress("DEPRECATION")
//    context.getSystemService(VIBRATOR_SERVICE) as Vibrator
//}
//
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