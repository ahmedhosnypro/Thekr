package com.thekr.fingerprint

fun interface FingerprintEventListener {
    fun onFingerprintEvent(event: FingerprintEvent)
}
