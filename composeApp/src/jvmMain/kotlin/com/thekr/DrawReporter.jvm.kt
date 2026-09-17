package com.thekr.util

import androidx.compose.runtime.Composable

/** TTFD anchor has no system meaning on desktop; inert no-op. */
@Composable
actual fun reportFullyDrawnAnchor() = Unit
