package com.thekr.ui.counter.viewmodel.action

import com.thekr.ui.counter.viewmodel.SuspendRunnable
import com.thekr.ui.counter.viewmodel.ThekrCounterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object AntiSleep {
    private val scope = CoroutineScope(Dispatchers.Main)

    // initialize the job to cancel it if user clicked, and prevent the alert
    var detectSleepingJob: Job? = null
    var alertSleepRunnable: SuspendRunnable? = null

    fun stopDetectSleepingJob() {
        detectSleepingJob?.cancel()
    }

    fun killDetectSleepingJob() {
        detectSleepingJob?.cancel()
        alertSleepRunnable = null
    }

    fun restartSleepJop() {
        detectSleepingJob?.cancel()
        detectSleepingJob = scope.launch {
            alertSleepRunnable?.run()
        }
    }

    fun isSleepJobRunning(): Boolean = detectSleepingJob?.isActive == true
}

fun configSleepJop(
    viewModel: ThekrCounterViewModel,
) {
    // if user didn't click for 5 seconds, then slept  =true, and start the alert,
    // use detectSleepingJob to cancel the job if user clicked
    // Capture only the coolDown primitive so the singleton does not pin the disposed ViewModel.
    val coolDown = viewModel.getCurrentThekr().value.coolDown
    AntiSleep.alertSleepRunnable = SuspendRunnable {
        delay(60000 + coolDown)
        while (true) {
//                todo:
//                alertSleep(
//                    AntiSleep.soundPlayer, appContext(), currentThekr.soundFileName
//                )
            delay(30000)
        }
    }
    AntiSleep.restartSleepJop()
}
