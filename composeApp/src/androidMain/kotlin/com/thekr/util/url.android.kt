package com.thekr.util

import android.content.Intent
import android.net.Uri
import com.thekr.ThekrApplication

internal actual fun openUrl(url: String?) {
    val uri = url?.let { Uri.parse(it) } ?: return
    val intent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = uri
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    ThekrApplication.appContext.startActivity(intent)
}