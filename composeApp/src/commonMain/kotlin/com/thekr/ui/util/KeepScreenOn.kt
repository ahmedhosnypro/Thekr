package com.thekr.ui.util


import androidx.compose.runtime.Composable

@Composable
fun KeepScreenOn(
//    activity: Activity,
    enabled: Boolean,
    content: @Composable () -> Unit
) {
//    if (enabled) {
//        // Set the KEEP_SCREEN_ON flag to keep the screen on
//        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
//        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//        // Remember to clear the flag when the Composable is disposed
//        DisposableEffect(Unit) {
//            onDispose {
//                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//            }
//        }
//    }
    // Provide the content inside the Composable
    content()
}