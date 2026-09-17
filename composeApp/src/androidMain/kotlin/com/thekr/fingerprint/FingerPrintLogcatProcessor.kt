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

    // logcat's parseTime() accepts "MM-dd HH:mm:ss.mmm" (its primary documented
    // -T format) as well as "YYYY-MM-DD HH:mm:ss.mmm". The previous
    // kotlinx-datetime string used the ISO form, which parses fine — except
    // when the nanosecond-of-second is exactly 0, LocalTime.toString() omits
    // the fraction entirely, all accepted formats fail, and logcat exits
    // before streaming any line. SimpleDateFormat("...SSS") always emits a
    // 3-digit fraction, so the edge is gone. Locale.US is load-bearing:
    // Language.android.kt calls Locale.setDefault(Locale("ar")), and a
    // default-locale SimpleDateFormat would emit Arabic-Indic digits logcat
    // cannot parse.
    val formattedDateTime = SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US).format(Date())

    Shell.cmd("logcat *:S [GF_HAL][gf_hal_milan] -v tag -T \"$formattedDateTime\"")
        .to(callbackList)
        .submit()
}
