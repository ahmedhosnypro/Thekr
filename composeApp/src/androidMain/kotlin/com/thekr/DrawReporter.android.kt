package com.thekr.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

/**
 * Reports the fully-drawn moment for the hosting Activity, ending the TTFD
 * (time to fully drawn) measurement. Re-invocations after the first report are
 * ignored by the framework, so this is safe across recompositions.
 *
 * [LocalContext] resolves the Activity directly from composition, so the very
 * first call — during the first content composition of the very first launch,
 * before any ActivityLifecycleCallbacks-based tracker could have registered —
 * still reaches the Activity. That is why the anchor is a composable rather
 * than a plain function: a tracker registered at first call would miss the
 * first activity, which is exactly the launch TTFD measures.
 */
@Composable
actual fun reportFullyDrawnAnchor() {
    val activity = LocalContext.current as? Activity
    LaunchedEffect(activity) {
        activity?.reportFullyDrawn()
    }
}
