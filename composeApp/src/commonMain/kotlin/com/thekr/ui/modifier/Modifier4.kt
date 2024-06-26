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
import androidx.compose.ui.unit.IntSize
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Draws content from [painter] behind the content.
 *
 * @param painter used to draw content
 * @param alignment specifies alignment of the [painter] relative to
 *     content
 * @param contentScale strategy for scaling [painter] if its size does not
 *     match the content size
 * @param alpha opacity of [painter]
 * @param colorFilter optional [ColorFilter] to apply to [painter]
 */
fun Modifier.background4(
    painter: Painter,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Inside,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    drawBehind: ContentDrawScope.() -> Unit = {},
    drawFront: ContentDrawScope.() -> Unit = {}
): Modifier = drawWithContent {
    println("background: drawWithContent")
    drawBehind()
    val intrinsicSize = painter.intrinsicSize
    val srcWidth = if (intrinsicSize.isSpecified) intrinsicSize.width else size.width
    val srcHeight = if (intrinsicSize.isSpecified) intrinsicSize.height else size.height
    val srcSize = Size(srcWidth, srcHeight)

    // Calculate scaled size respecting ContentScale and without exceeding bounds
    // Calculate scaled size respecting ContentScale and without exceeding bounds
    val scaledSize = calculateScaledSize(srcSize, size, contentScale)
//        val scaledSize = contentScale.computeScaleFactor(srcSize, size).let { scale ->
//            Size(
//                width = minOf(srcSize.width * scale.scaleX, size.width),
//                height = minOf(srcSize.height * scale.scaleY, size.height)
//            )
//        }

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


// Helper functions to calculate scaled size based on ContentScale
private fun calculateScaledSize(
    srcSize: Size,
    dstSize: Size,
    contentScale: ContentScale
): Size {
    val dstWidth = dstSize.width
    val dstHeight = dstSize.height

    val srcWidth = srcSize.width
    val srcHeight = srcSize.height

    return when (contentScale) {

        ContentScale.Crop -> {
            val scale = max(
                dstWidth / srcWidth,
                dstHeight / srcHeight
            )
            val scaledWidth = srcWidth * scale
            val scaledHeight = srcHeight * scale
            if (scaledWidth > srcWidth || scaledHeight > srcHeight) {
                // find the firs smallest scale that results  to be less than the destination size with the same aspect ratio
                val scale = min(
                    dstWidth / srcWidth,
                    dstHeight / srcHeight
                )
                val scaledWidth = srcWidth * scale
                val scaledHeight = srcHeight * scale
                Size(scaledWidth, scaledHeight)
            }else{
                Size(scaledWidth, scaledHeight)
            }
        }

        ContentScale.Fit -> {
            val scale = min(
                dstSize.width / srcSize.width,
                dstSize.height / srcSize.height
            )
            Size(srcSize.width * scale, srcSize.height * scale)
        }

        ContentScale.FillHeight -> {
            val scale = dstSize.height / srcSize.height
            Size(
                minOf(srcSize.width * scale, dstSize.width),
                dstSize.height
            )
        }

        ContentScale.FillWidth -> {
            val scale = dstSize.width / srcSize.width
            Size(
                dstSize.width,
                minOf(srcSize.height * scale, dstSize.height)
            )
        }

        ContentScale.Inside -> {
            if (srcSize.width <= dstSize.width && srcSize.height <= dstSize.height) {
                srcSize
            } else {
                val scale = min(
                    dstSize.width / srcSize.width,
                    dstSize.height / srcSize.height
                )
                Size(srcSize.width * scale, srcSize.height * scale)
            }
        }

        ContentScale.None -> srcSize
        ContentScale.FillBounds -> dstSize
        else -> Size.Zero
    }
}