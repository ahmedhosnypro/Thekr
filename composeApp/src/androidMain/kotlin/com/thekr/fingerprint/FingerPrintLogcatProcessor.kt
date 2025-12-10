package com.thekr.fingerprint

import com.topjohnwu.superuser.CallbackList
import com.topjohnwu.superuser.Shell
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


// todo add a job to be able to cancel this, add stopMonitoring()
@OptIn(ExperimentalTime::class)
actual fun FingerPrintLogcatProcessor.startMonitoring() {
    val callbackList = object : CallbackList<String>() {
        override fun onAddElement(s: String) {
            handleLogcatLine(s)
        }
    }
//        val time = now()
//            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))

    val dateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val formattedDateTime = "${dateTime.date} ${dateTime.time}"

    Shell.cmd("logcat *:S [GF_HAL][gf_hal_milan] -v tag -T \"$formattedDateTime\"")
        .to(callbackList)
        .submit()
}
