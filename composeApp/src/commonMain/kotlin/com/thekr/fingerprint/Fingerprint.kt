package com.thekr.fingerprint

import com.thekr.ui.counter.viewmodel.ThekrCounterViewModel
import com.thekr.ui.counter.viewmodel.action.onThekrCounterCount

object Fingerprint {
    private var fingerprintEventListener: FingerprintEventListener? = null

    fun ThekrCounterViewModel.setFingerprintListener(enabled: Boolean) {

        if (fingerprintEventListener != null) {
            FingerprintEventDispatcher.removeListener(fingerprintEventListener!!)
            fingerprintEventListener = null
        }

        if (enabled) {
            val listener = FingerprintEventListener { fingerprintEvent ->
                if (fingerprintEvent.type == FingerprintEventType.TouchUp) {
                    onThekrCounterCount()
                }
            }
            fingerprintEventListener = listener
            FingerprintEventDispatcher.addListener(listener)
        }
    }

    fun clearFingerprintListener() {
        fingerprintEventListener?.let {
            FingerprintEventDispatcher.removeListener(it)
        }
        fingerprintEventListener = null
    }
}