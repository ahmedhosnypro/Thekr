package com.thekr.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.thekr.resources.Res
import com.thekr.resources.app_icon
import com.thekr.resources.icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@Suppress("ktlint:standard:function-naming")
fun LoadScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        // paint res/icon.png
        Image(
            painter = painterResource(Res.drawable.icon),
            contentDescription = stringResource(Res.string.app_icon),
        )
    }
}
