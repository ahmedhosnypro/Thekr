package com.thekr.ui.util

import androidx.compose.foundation.focusable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import com.thekr.ui.util.EventKeyHandler.handleKeyDebounce

object EventKeyHandler {
    private const val DEBOUNCE_INTERVAL_MILLIS = 50L

    fun Modifier.handleKeyDebounce(
        key: Key,
        onKeyAction: () -> Unit
    ): Modifier = this.composed {
        val currentAction by rememberUpdatedState(onKeyAction)
        val lastClickTime = remember { mutableLongStateOf(0L) }

         onKeyEvent { event ->
            if (event.key == key) {
                // Debounce the action for the specified key
                debounceAction(
                    lastClickTime = lastClickTime,
                ) {
                    currentAction()
                }
            }
            true // Consume the event
        }
    }

    // Generic debounce function outside the composable
    private fun debounceAction(
        lastClickTime: MutableLongState,
        action: () -> Unit
    ) {

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime.longValue > DEBOUNCE_INTERVAL_MILLIS) {
            action()
            lastClickTime.longValue = currentTime
        }
    }
}

fun Modifier.customOnKeyEvent(
    navigateUp: () -> Unit,
    enabled: Boolean,
    onCount: () -> Unit,
    focusRequester: FocusRequester
) = this then  if (enabled) Modifier
    .onKeyEvent { keyEvent ->
        // Handle Back Key
        if (keyEvent.key == Key.Back) {
            navigateUp()
        }
        true // Consume back key event
    }
    .focusable(true)
    .focusRequester(focusRequester)
    .focusTarget()
    // Apply debounced key handling
    .handleKeyDebounce(key = Key.VolumeUp) {
        onCount()
    }
else Modifier