package com.thekr.fingerprint

import kotlin.concurrent.Volatile

object FingerprintEventDispatcher {
    // dispatchEvent runs on the libsu logcat callback thread while listeners
    // are added/removed from the main thread; copy-on-write keeps mutation and
    // dispatch safe to run concurrently without blocking either side.
    @Volatile
    private var listeners: List<FingerprintEventListener> = emptyList()

    fun addListener(listener: FingerprintEventListener) {
        listeners = listeners + listener
    }

    fun removeListener(listener: FingerprintEventListener) {
        listeners = listeners - listener
    }

    fun dispatchEvent(event: FingerprintEvent) {
        listeners.forEach { it.onFingerprintEvent(event) }
    }
}
