package com.thekr.fingerprint

import com.thekr.ui.counter.viewmodel.ZekrCounterViewModel
import com.thekr.ui.counter.viewmodel.action.onZekrCounterCount

object Fingerprint {
    private var fingerprintEventListener: FingerprintEventListener? = null

    fun ZekrCounterViewModel.setFingerprintListener(enabled: Boolean) {

        if (fingerprintEventListener != null) {
            FingerprintEventDispatcher.removeListener(fingerprintEventListener!!)
        }

        if (enabled) {
            val listener = FingerprintEventListener { fingerprintEvent ->
                if (fingerprintEvent.type == FingerprintEventType.TouchUp) {
                    onZekrCounterCount()
                }
            }
            fingerprintEventListener = listener
            FingerprintEventDispatcher.addListener(listener)
        }
    }
}