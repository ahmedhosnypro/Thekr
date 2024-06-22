package com.thekr.fingerprint

data class FingerprintEvent(
    val type: FingerprintEventType,
    val timeStamp:Long,
)