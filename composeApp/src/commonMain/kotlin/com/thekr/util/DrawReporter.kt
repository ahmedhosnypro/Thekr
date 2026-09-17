package com.thekr.util

import androidx.compose.runtime.Composable

/**
 * TTFD anchor: reports the app's fully-drawn moment to the system when the
 * LoadScreen -> content transition first composes, so TTFD (time to fully
 * drawn) is captured by macrobenchmark's [androidx.benchmark.macro.StartupTimingMetric]
 * and by system startup traces.
 *
 * The Android actual calls [android.app.Activity.reportFullyDrawn] for the
 * hosting Activity; every other platform gets an inert no-op actual.
 *
 * Lowercase because ktlint's function-naming rule (with this repo's
 * ignoreAnnotated=Composable entry being ineffective for expect/actual
 * declarations) rejects new PascalCase composables that are not in the
 * lint baseline, which this file cannot edit.
 */
@Composable
expect fun reportFullyDrawnAnchor()
