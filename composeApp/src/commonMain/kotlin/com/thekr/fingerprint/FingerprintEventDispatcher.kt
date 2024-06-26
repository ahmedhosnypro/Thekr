package com.thekr.fingerprint

object FingerprintEventDispatcher {
    private val listeners = mutableListOf<FingerprintEventListener>()

    fun addListener(listener: FingerprintEventListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: FingerprintEventListener) {
        listeners.remove(listener)
    }

    fun dispatchEvent(event: FingerprintEvent) {
        listeners.forEach { it.onFingerprintEvent(event) }
    }
}