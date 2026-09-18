package com.thekr.ui.counter.viewmodel.action

actual fun vibrate() {
    // Desktop has no haptic hardware; counterClickFeedBack also gates this
    // behind Platform.isAndroid, so the no-op only keeps the actual linked.
}
