package com.thekr.values

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import network.chaintech.sdpcomposemultiplatform.sdp

object Dimensions {
    val tiny = 4.dp
    val small = 8.dp
    val normal = 12.dp
    val medium = 16.dp
    val large = 24.dp
    val xLarge = 32.dp
    val xxLarge = 48.dp
    val xxxLarge = 64.dp
}

object SDimensions{
    val sTiny: Dp @Composable get() = 4.sdp
    val sSmall: Dp @Composable get() = 8.sdp
    val sNormal: Dp @Composable get() = 12.sdp
    val sMedium: Dp @Composable get() = 16.sdp
    val sLarge: Dp @Composable get() = 24.sdp
    val sXLarge: Dp @Composable get() = 32.sdp
    val sXXLarge: Dp @Composable get() = 48.sdp
    val sXXXLarge: Dp @Composable get() = 64.sdp
}