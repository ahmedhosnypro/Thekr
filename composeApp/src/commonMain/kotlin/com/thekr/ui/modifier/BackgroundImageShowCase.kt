package com.thekr.ui.modifier

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.ContentScale
import com.thekr.resources.Res
import com.thekr.resources.wood
import org.jetbrains.compose.resources.painterResource

@Composable
fun BackgroundImageShowCase() {
    val contentScale = remember { ContentScale.Crop }
    val drawableResource = remember { Res.drawable.wood }
    val shape = remember { RectangleShape }
    val alignment = remember { Alignment.Center }
    val repeat = remember { BackgroundRepeat.RepeatX }
    val alpha = remember { 0.5f }
    val colorFilter = remember { null }
    val drawBehind: ContentDrawScope.() -> Unit = remember { {} }
    val drawFront: ContentDrawScope.() -> Unit = remember { {} }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .backgroundImage(
                painter = painterResource(drawableResource),
                contentScale = contentScale,
                shape = shape,
                alignment = alignment,
                repeat = repeat,
                alpha = alpha,
                colorFilter = colorFilter,
                drawBehind = drawBehind,
                drawFront = drawFront
            )
    ) {
        // Content
    }
}