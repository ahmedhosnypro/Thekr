package com.thekr.ui.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.ContentDrawScope

fun Modifier.drawWithContentIfReady(ready: Boolean, onDraw: ContentDrawScope.() -> Unit = {}) = this.drawWithContent {
    if (ready) {
        drawContent()
        onDraw()
    }
}