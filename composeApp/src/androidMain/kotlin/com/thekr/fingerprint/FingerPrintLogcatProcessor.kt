package com.thekr.fingerprint

import com.topjohnwu.superuser.CallbackList
import com.topjohnwu.superuser.Shell
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// The logcat shell runs for the process lifetime and libsu 6.0.0's Shell.Job
// has no cancel API, so start the monitor at most once per process instead of
// piling up a new shell on every startMonitoring call.
private val monitorStarted = AtomicBoolean(false)

@OptIn(ExperimentalTime::class)
actual fun FingerPrintLogcatProcessor.startMonitoring() {
    if (!monitorStarted.compareAndSet(false, true)) {
        return
    }

    val callbackList = object : CallbackList<String>() {
        override fun onAddElement(s: String) {
            handleLogcatLine(s)
        }
    }

    val dateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val formattedDateTime = "${dateTime.date} ${dateTime.time}"

    Shell.cmd("logcat *:S [GF_HAL][gf_hal_milan] -v tag -T \"$formattedDateTime\"")
        .to(callbackList)
        .submit()
}
