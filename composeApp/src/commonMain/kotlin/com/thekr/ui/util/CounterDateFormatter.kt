package com.thekr.ui.util

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

fun timeElapsed(timeDiff: Long): String {
    val days = TimeUnit.MILLISECONDS.toDays(timeDiff)
    val hours = TimeUnit.MILLISECONDS.toHours(timeDiff) % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff) % 60

    return when {
        days > 0 -> "${days}d ${hours}h ${minutes}m ${seconds}s"
        hours > 0 -> "${hours}h ${minutes}m ${seconds}s"
        minutes > 0 -> "${minutes}m ${seconds}s"
        else -> "${seconds}s"
    }
}

fun formatDateTimeStamp(long: Long): String {
    val sdf = SimpleDateFormat("MM/dd/yy hh:mm a", Locale.ENGLISH)
    return sdf.format(Timestamp(long))
}

fun formatDate(long: Long): String {
    val sdf = SimpleDateFormat("MM/dd/yy", Locale.ENGLISH)
    return sdf.format(Timestamp(long))
}