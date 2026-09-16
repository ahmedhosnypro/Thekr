package com.thekr.util

import java.awt.Desktop
import java.net.URI

internal actual fun openUrl(url: String?) {
    val uri = url?.let { runCatching { URI.create(it) }.getOrNull() } ?: return
    if (!Desktop.isDesktopSupported()) return
    val desktop = Desktop.getDesktop()
    if (desktop.isSupported(Desktop.Action.BROWSE)) {
        desktop.browse(uri)
    }
}