package com.thekr.ui.modifier


import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

fun Modifier.background5(
    painter: Painter,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Inside,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    drawBehind: ContentDrawScope.() -> Unit = {},
    drawFront: ContentDrawScope.() -> Unit = {}
): Modifier = layout { measurable, constraints ->
    println("background5: layout")
    // Measure the composable first
    val placeable = measurable.measure(constraints)

    // Lay ourselves out to match the composable
    layout(placeable.width, placeable.height) {
        placeable.placeRelative(0, 0)
    }
}.drawWithContent {
    println("background5: drawWithContent")
    drawBehind()

    val intrinsicSize = painter.intrinsicSize
    val srcWidth = if (intrinsicSize.isSpecified) intrinsicSize.width else size.width
    val srcHeight = if (intrinsicSize.isSpecified) intrinsicSize.height else size.height
    val srcSize = Size(srcWidth, srcHeight)

    // Calculate scaled size respecting ContentScale and without exceeding bounds
    val scaledSize = contentScale.computeScaleFactor(srcSize, size).let { scale ->
        Size(
            width = minOf(srcSize.width * scale.scaleX, size.width),
            height = minOf(srcSize.height * scale.scaleY, size.height)
        )
    }

    val alignedPosition = alignment.align(
        IntSize(scaledSize.width.roundToInt(), scaledSize.height.roundToInt()),
        IntSize(size.width.roundToInt(), size.height.roundToInt()),
        layoutDirection
    )

    val dx = alignedPosition.x.toFloat()
    val dy = alignedPosition.y.toFloat()

    translate(dx, dy) {
        with(painter) {
            draw(size = scaledSize, alpha = alpha, colorFilter = colorFilter)
        }
    }
    drawFront()
    drawContent()
}