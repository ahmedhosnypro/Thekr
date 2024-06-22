package com.thekr.ui.home.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color


@Composable
private fun zekrIndicatorColorList(): List<Color> = remember {
    listOf(
        Color(0xFFC75C5D),
        Color(0xFF70B744),
        Color(0xFF306CA5),
        Color(0xFFC5713F),
        Color(0xFF4FBE9B),
        Color(0xFF3099A6),
    )
}

@Composable
fun zekrIndicatorColor(
    index:Int
): Color{
    val colors = zekrIndicatorColorList()
    return remember {
        colors[index % colors.size]
    }
}