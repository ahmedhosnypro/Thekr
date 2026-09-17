package com.thekr.ui.util

import androidx.compose.foundation.focusable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import com.thekr.ui.counter.CounterHelper
import com.thekr.ui.util.EventKeyHandler.handleKeyDebounce

object EventKeyHandler {
    private const val DEBOUNCE_INTERVAL_MILLIS = 50L

    fun Modifier.handleKeyDebounce(
        key: Key,
        onKeyAction: () -> Unit,
    ): Modifier = this.composed {
        val currentAction by rememberUpdatedState(onKeyAction)
        val lastClickTime = remember { mutableLongStateOf(0L) }

        onKeyEvent { event ->
            // Only consume matching KeyDown events; key events bubble outward
            // from the focus target, and consuming everything would starve
            // the other handlers in the chain.
            if (event.key == key && event.type == KeyEventType.KeyDown) {
                // Debounce the action for the specified key
                debounceAction(
                    lastClickTime = lastClickTime,
                ) {
                    currentAction()
                }
                true
            } else {
                false
            }
        }
    }

    // Generic debounce function outside the composable
    private fun debounceAction(
        lastClickTime: MutableLongState,
        action: () -> Unit,
    ) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime.longValue > DEBOUNCE_INTERVAL_MILLIS) {
            action()
            lastClickTime.longValue = currentTime
        }
    }
}

fun Modifier.customOnKeyEvent(
    enabled: Boolean,
    focusRequester: FocusRequester,
) = this then if (enabled) {
    Modifier
        // Key handlers must sit outward of the focus target: key events are
        // dispatched to the focused FocusTargetNode and bubble outward through
        // the chain, so handlers placed after it never fire. The debounce
        // handler goes outermost so the Back handler below still sees Back.
        .handleKeyDebounce(key = Key.VolumeUp) { CounterHelper.onCount() }
        .onKeyEvent { keyEvent ->
            // Handle Back Key
            if (keyEvent.key == Key.Back && keyEvent.type == KeyEventType.KeyDown) {
                CounterHelper.onNavigateUp()
                true
            } else {
                false
            }
        }
        .focusable(true)
        .focusRequester(focusRequester)
        .focusTarget()
} else {
    Modifier
}
