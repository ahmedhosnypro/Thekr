package com.thekr.fingerprint

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

object FingerprintEventDispatcher {
    // addListener/removeListener run on Main (Compose effects, screen dispose)
    // while dispatchEvent arrives on the libsu logcat callback thread;
    // copy-on-write lets the registry be read safely from either side.
    @Volatile
    private var listeners: List<FingerprintEventListener> = emptyList()

    // Listener code drives the ViewModel count pipeline and Compose state, so
    // events must leave the logcat thread before any listener runs. The 50ms
    // debounce in FingerPrintLogcatProcessor already serialized events on the
    // single logcat thread, and Main dispatch is FIFO, so ordering is kept.
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun addListener(listener: FingerprintEventListener) {
        listeners = listeners + listener
    }

    fun removeListener(listener: FingerprintEventListener) {
        listeners = listeners - listener
    }

    fun dispatchEvent(event: FingerprintEvent) {
        if (listeners.isEmpty()) return
        scope.launch {
            // Re-read on Main: a listener removed before the hop executes
            // (screen dispose) must not receive the queued event.
            listeners.forEach { it.onFingerprintEvent(event) }
        }
    }
}
