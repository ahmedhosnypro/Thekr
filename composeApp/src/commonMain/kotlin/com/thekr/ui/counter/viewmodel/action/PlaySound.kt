package com.thekr.ui.counter.viewmodel.action


import com.thekr.ui.counter.viewmodel.ZekrCounterViewModel

expect fun ZekrCounterViewModel.playZekrAudio()

expect fun stopPlayer(viewModel: ZekrCounterViewModel)
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

//fun ZekrCounterViewModel.observePlayerEvents(
//
//) {
////    zekrSoundPlayer.addListener(object : Player.Listener {
////        override fun onPlaybackStateChanged(playbackState: Int) {
////            super.onPlaybackStateChanged(playbackState)
////            if (playbackState == Player.STATE_ENDED) {
////                repeatAudio()
////                restartSleepJop()
////            }
////        }
////    })
//}
