package com.thekr.ui.counter.viewmodel.action


import com.thekr.ui.counter.viewmodel.SuspendRunnable
import com.thekr.ui.counter.viewmodel.ZekrCounterViewModel
import com.thekr.ui.counter.viewmodel.action.AntiSleep.alertSleepRunnable
import com.thekr.ui.counter.viewmodel.action.AntiSleep.detectSleepingJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object AntiSleep {
//    var soundPlayer: ExoPlayer? = null
//
//    fun initSoundPlayer(player: ExoPlayer) {
//        soundPlayer = player
//    }

    // initialize the job to cancel it if user clicked, and prevent the alert
    var detectSleepingJob: Job? = null
    var alertSleepRunnable: SuspendRunnable? = null

    fun stopDetectSleepingJob() {
//        soundPlayer?.stop()
        detectSleepingJob?.cancel()
    }

    fun killDetectSleepingJob() {
//        soundPlayer?.stop()
        detectSleepingJob?.cancel()
        alertSleepRunnable = null
    }


    fun restartSleepJop() {
//        soundPlayer?.stop()
        detectSleepingJob?.cancel()
        detectSleepingJob = CoroutineScope(Dispatchers.Main).launch {
            alertSleepRunnable?.run()
        }
        detectSleepingJob?.start()
    }

    fun isSleepJobRunning(): Boolean {
        return detectSleepingJob?.isActive == true
    }
}

fun configSleepJop(
    viewModel: ZekrCounterViewModel
) {
    // if user didn't click for 5 seconds, then slept  =true, and start the alert,
    // use detectSleepingJob to cancel the job if user clicked
    with(viewModel) {
        val currentZekr = getCurrentZekr().value
        alertSleepRunnable = SuspendRunnable {
            delay(60000 + currentZekr.coolDown)
            while (true) {
//                todo:
//                alertSleep(
//                    AntiSleep.soundPlayer, appContext(), currentZekr.soundFileName
//                )
                delay(30000)
            }
        }
    }

    detectSleepingJob?.cancel()
    detectSleepingJob = CoroutineScope(Dispatchers.Main).launch {
        alertSleepRunnable?.run()
    }
    detectSleepingJob?.start()
}