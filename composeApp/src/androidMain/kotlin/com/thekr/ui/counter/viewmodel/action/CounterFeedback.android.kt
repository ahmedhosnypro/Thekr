package com.thekr.ui.counter.viewmodel.action

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import com.thekr.ThekrApplication.Companion.appContext

@Suppress("DEPRECATION")
actual fun vibrate(){
    vibrator().vibrate(longArrayOf(0, 16), -1)
}

fun vibrator() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    val vibratorManager =
        appContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    vibratorManager.defaultVibrator
} else {
    @Suppress("DEPRECATION")
    appContext.getSystemService(VIBRATOR_SERVICE) as Vibrator
}

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