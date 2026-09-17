package com.thekr.fingerprint

import com.topjohnwu.superuser.CallbackList
import com.topjohnwu.superuser.Shell
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

// The logcat shell runs for the process lifetime and libsu 6.0.0's Shell.Job
// has no cancel API, so start the monitor at most once per process instead of
// piling up a new shell on every startMonitoring call.
private val monitorStarted = AtomicBoolean(false)

actual fun FingerPrintLogcatProcessor.startMonitoring() {
    if (!monitorStarted.compareAndSet(false, true)) {
        return
    }

    val callbackList = object : CallbackList<String>() {
        override fun onAddElement(s: String) {
            handleLogcatLine(s)
        }
    }

    // logcat -T only parses "MM-DD HH:MM:SS.mmm" timestamps; an ISO-style
    // "YYYY-MM-DD HH:MM:SS" is rejected, making logcat exit before streaming
    // any line, silently killing the fingerprint monitor. Locale.US pins
    // ASCII digits regardless of the app locale.
    val formattedDateTime = SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US).format(Date())

    Shell.cmd("logcat *:S [GF_HAL][gf_hal_milan] -v tag -T \"$formattedDateTime\"")
        .to(callbackList)
        .submit()
}
