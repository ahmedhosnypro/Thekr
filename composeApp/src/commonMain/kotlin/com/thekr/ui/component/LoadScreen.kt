package com.thekr.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import com.thekr.resources.Res
import com.thekr.resources.icon

@Composable
fun LoadScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // paint res/icon.png
        Image(
            painter = painterResource(Res.drawable.icon),
            contentDescription = "App icon"
        )
    }
}