package com.thekr.fingerprint

import com.topjohnwu.superuser.CallbackList
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object FingerPrintLogcatProcessor {
    /**
     * Logcat line that indicates a touch-up event
     * for down event: "GF_IRQ_FINGER_DOWN_MASK"
     */
    private const val GF_IRQ_FINGER_UP_MASK = "GF_IRQ_FINGER_UP_MASK"

    private var lastTouchUp = System.currentTimeMillis()

    // todo add a job to be able to cancel this, add stopMonitoring()
     fun startMonitoring() {
        val callbackList = object : CallbackList<String>() {
            override fun onAddElement(s: String) {
                handleLogcatLine(s)
            }
        }
        val time = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
        Shell.cmd("logcat *:S [GF_HAL][gf_hal_milan] -v tag -T \"$time\"")
            .to(callbackList)
            .submit()
    }

    fun handleLogcatLine(line: String) {
        if (line.contains(GF_IRQ_FINGER_UP_MASK) && stableTouchUp()) {
            FingerprintEventDispatcher.dispatchEvent(
                FingerprintEvent(
                    type = FingerprintEventType.TouchUp,
                    timeStamp = System.currentTimeMillis()
                )
            )
        }
    }

    private fun stableTouchUp(): Boolean {
        val now = System.currentTimeMillis()

        val timeDiff = now - lastTouchUp
        if (timeDiff > 50) {
            lastTouchUp = now
            return true
        }
        lastTouchUp = now

        return false
    }

}

