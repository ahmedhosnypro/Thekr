package com.thekr.fingerprint

import com.thekr.ui.counter.viewmodel.ThekrCounterViewModel
import com.thekr.ui.counter.viewmodel.action.onThekrCounterCount

object Fingerprint {
    // Keyed by the owning ViewModel so a stale dispose from a previous screen
    // clears only its own registration and cannot drop the live screen's
    // listener, while a new registration still takes the registry over so a
    // dead screen's listener cannot keep dispatching.
    private var registered: Pair<ThekrCounterViewModel, FingerprintEventListener>? = null

    fun ThekrCounterViewModel.setFingerprintListener(enabled: Boolean) {
        val current = registered
        if (current != null && (current.first === this || enabled)) {
            FingerprintEventDispatcher.removeListener(current.second)
            registered = null
        }

        if (enabled) {
            val listener = FingerprintEventListener { fingerprintEvent ->
                if (fingerprintEvent.type == FingerprintEventType.TouchUp) {
                    onThekrCounterCount()
                }
            }
            registered = this to listener
            FingerprintEventDispatcher.addListener(listener)
        }
    }

    fun ThekrCounterViewModel.clearFingerprintListener() {
        val current = registered ?: return
        if (current.first !== this) return
        FingerprintEventDispatcher.removeListener(current.second)
        registered = null
    }
}
