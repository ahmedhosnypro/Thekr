//package com.thekr.ui.counter.viewModel.action
//
//import android.content.res.AssetManager
//import androidx.media3.common.MediaItem
//import androidx.media3.common.Player
//import com.thekr.data.settings.SettingsHelper.settingsDetails
//import com.thekr.ui.counter.viewModel.ZekrCounterViewModel
//import com.thekr.ui.counter.viewModel.action.AntiSleep.restartSleepJop
//import com.thekr.ui.counter.viewModel.action.AntiSleep.stopDetectSleepingJob
//import com.thekr.ui.zekr.component.playSound
//import kotlinx.coroutines.flow.update
//import java.io.IOException
//
//fun ZekrCounterViewModel.playZekrAudio(
//) {
//    val soundFileName = getCurrentZekr().value.soundFileName
//    appContext().assets
//    if (soundFileName != null) {
//        val assetPath = "${settingsDetails.currentSheikh}/${soundFileName}.mp3"
//        if (fileExistsInAssets(assetPath, appContext().assets)) {
//            val mediaItem = MediaItem.fromUri("asset:///$assetPath")
//            playSound(zekrSoundPlayer, appContext(), soundFileName)
//            zekrSoundPlayer.stop()
//            zekrSoundPlayer.setMediaItem(mediaItem)
//            zekrSoundPlayer.prepare()
//            zekrSoundPlayer.play()
//            mutableUiState.update { currentState ->
//                currentState.copy(
//                    playState = Player.STATE_READY
//                )
//            }
//            stopDetectSleepingJob()
//        }
//    }
//}
//
//
//fun fileExistsInAssets(
//    assetPath: String,
//    assets: AssetManager
//): Boolean {
//    return try {
//        assets.open(assetPath).use { }  // Use {} for automatic closing
//        true
//    } catch (e: IOException) {
//        false
//    }
//}
//
//fun stopPlayer(
//    viewModel: ZekrCounterViewModel
//) {
//    with(viewModel) {
//        zekrSoundPlayer.stop()
//        mutableUiState.update { currentState ->
//            currentState.copy(
//                playState = Player.STATE_IDLE
//            )
//        }
//    }
//}
//
//fun ZekrCounterViewModel.observePlayerEvents(
//
//) {
//    zekrSoundPlayer.addListener(object : Player.Listener {
//        override fun onPlaybackStateChanged(playbackState: Int) {
//            super.onPlaybackStateChanged(playbackState)
//            if (playbackState == Player.STATE_ENDED) {
//                repeatAudio()
//                restartSleepJop()
//            }
//        }
//    })
//}
