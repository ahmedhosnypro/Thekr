package com.thekr.fingerprint

import kotlin.time.TimeSource

object FingerPrintLogcatProcessor {
    /**
     * Logcat line that indicates a touch-up event
     * for down event: "GF_IRQ_FINGER_DOWN_MASK"
     */
    private const val GF_IRQ_FINGER_UP_MASK = "GF_IRQ_FINGER_UP_MASK"

    private var lastTouchUp = TimeSource.Monotonic.markNow()

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
        val timeDiff = lastTouchUp.elapsedNow().inWholeMilliseconds
        lastTouchUp = TimeSource.Monotonic.markNow()

        return timeDiff > 50
    }
}

expect fun  FingerPrintLogcatProcessor.startMonitoring()

