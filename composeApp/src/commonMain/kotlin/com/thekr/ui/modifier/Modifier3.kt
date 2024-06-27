package com.thekr.ui.modifier

/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import androidx.annotation.FloatRange
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultBlendMode
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Draws an [ImageBitmap] with the given parameters and clipping shape
 * behind the content.
 *
 * @param bitmap The ImageBitmap to draw
 * @param shape desired shape of the background
 * @param alignment Alignment of the image within the layout bounds
 * @param contentScale Strategy for scaling the image if its size does not
 * @param repeat CSS-like background repeat behavior
 * @param alpha Opacity to be applied to [bitmap], with `0` being
 *     completely transparent and `1` being completely opaque. The value
 *     must be between `0` and `1`.
 * @param style Whether to fill or stroke the destination with the image
 * @param colorFilter ColorFilter to apply to the image when drawn
 * @param blendMode Blending algorithm to be applied to the image
 * @param filterQuality Sampling algorithm applied to the image when it is
 *     scaled and drawn
 */
@Stable
fun Modifier.background3(
    bitmap: ImageBitmap,
    shape: Shape = RectangleShape,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Inside,
    repeat: BackgroundRepeat = BackgroundRepeat.Repeat,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
    style: DrawStyle = Fill,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DefaultBlendMode,
    filterQuality: FilterQuality = FilterQuality.Low,
    drawBehind: ContentDrawScope.() -> Unit = {},
    drawFront: ContentDrawScope.() -> Unit = {}
): Modifier = composed {
    val bitmapPainter = remember(bitmap) { BitmapPainter(bitmap, filterQuality = filterQuality) }
    ImageBackgroundElement(
        image = bitmap,
        painter = bitmapPainter,
        shape = shape,
        alignment = alignment,
        contentScale = contentScale,
        repeat = repeat,
        alpha = alpha,
        style = style,
        colorFilter = colorFilter,
        blendMode = blendMode,
        filterQuality = filterQuality,
        drawBehind = drawBehind,
        drawFront = drawFront,
        inspectorInfo = debugInspectorInfo {
            name = "background"
            properties["image"] = bitmap
            properties["shape"] = shape
            properties["alignment"] = alignment
            properties["contentScale"] = contentScale
            properties["backgroundRepeat"] = repeat
            properties["alpha"] = alpha
            properties["style"] = style
            properties["colorFilter"] = colorFilter
            properties["blendMode"] = blendMode
            properties["filterQuality"] = filterQuality
            properties["drawBehind"] = drawBehind
            properties["drawFront"] = drawFront
        }
    )
}

private class ImageBackgroundElement(
    private val image: ImageBitmap,
    private val painter: BitmapPainter,
    private val shape: Shape,
    private val alignment: Alignment,
    private val contentScale: ContentScale,
    private val repeat: BackgroundRepeat,
    private val alpha: Float,
    private val style: DrawStyle,
    private val colorFilter: ColorFilter?,
    private val blendMode: BlendMode,
    private val filterQuality: FilterQuality,
    private val drawBehind: ContentDrawScope.() -> Unit,
    private val drawFront: ContentDrawScope.() -> Unit,
    private val inspectorInfo: InspectorInfo.() -> Unit
) : ModifierNodeElement<ImageBackgroundNode>() {
    override fun create(): ImageBackgroundNode {
        return ImageBackgroundNode(
            image,
            painter,
            shape,
            alignment,
            contentScale,
            repeat,
            alpha,
            style,
            colorFilter,
            blendMode,
            filterQuality,
            drawBehind,
            drawFront
        )
    }

    override fun update(node: ImageBackgroundNode) {
        node.image = image
        node.painter = painter
        node.shape = shape
        node.alignment = alignment
        node.contentScale = contentScale
        node.repeat = repeat
        node.alpha = alpha
        node.style = style
        node.colorFilter = colorFilter
        node.blendMode = blendMode
        node.filterQuality = filterQuality
        node.drawBehind = drawBehind
        node.drawFront = drawFront
    }

    override fun InspectorInfo.inspectableProperties() {
        inspectorInfo()
    }

    override fun hashCode(): Int {
        var result = image.hashCode()
        result = 31 * result + painter.hashCode()
        result = 31 * result + shape.hashCode()
        result = 31 * result + alignment.hashCode()
        result = 31 * result + contentScale.hashCode()
        result = 31 * result + repeat.hashCode()
        result = 31 * result + alpha.hashCode()
        result = 31 * result + style.hashCode()
        result = 31 * result + (colorFilter?.hashCode() ?: 0)
        result = 31 * result + blendMode.hashCode()
        result = 31 * result + filterQuality.hashCode()
        result = 31 * result + drawBehind.hashCode()
        result = 31 * result + drawFront.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        val otherModifier = other as? ImageBackgroundElement ?: return false
        return image == otherModifier.image &&
                painter == otherModifier.painter &&
                alignment == otherModifier.alignment &&
                contentScale == otherModifier.contentScale &&
                repeat == otherModifier.repeat &&
                shape == otherModifier.shape &&
                alpha == otherModifier.alpha &&
                style == otherModifier.style &&
                colorFilter == otherModifier.colorFilter &&
                blendMode == otherModifier.blendMode &&
                filterQuality == otherModifier.filterQuality &&
                drawBehind == otherModifier.drawBehind &&
                drawFront == otherModifier.drawFront
    }
}

private class ImageBackgroundNode(
    var image: ImageBitmap,
    var painter: BitmapPainter,
    var shape: Shape,
    var alignment: Alignment,
    var contentScale: ContentScale,
    var repeat: BackgroundRepeat,
    var alpha: Float,
    var style: DrawStyle,
    var colorFilter: ColorFilter?,
    var blendMode: BlendMode,
    var filterQuality: FilterQuality,
    var drawBehind: ContentDrawScope.() -> Unit,
    var drawFront: ContentDrawScope.() -> Unit
) : DrawModifierNode, Modifier.Node() {

    override fun ContentDrawScope.draw() {
        val size = this.size
        drawIntoCanvas { canvas ->

            // 1. Clip to the destination bounds FIRST
            canvas.save() // Save the canvas state
            shape.createOutline(size, layoutDirection, this).apply {
                canvas.clipPath(Path().apply { addRect(size.toRect()) })
            }

            // 2. Calculate scaled size and alignment
            val intrinsicSize = painter.intrinsicSize
            val srcWidth = if (intrinsicSize.isSpecified) intrinsicSize.width else size.width
            val srcHeight = if (intrinsicSize.isSpecified) intrinsicSize.height else size.height
            val srcSize = Size(srcWidth, srcHeight)

            val scaledSize = calculateScaledSize(srcSize, size, contentScale)

            // 2. NOW draw the background image
            val alignedPosition = alignment.align(
                IntSize(scaledSize.width.roundToInt(), scaledSize.height.roundToInt()),
                IntSize(size.width.roundToInt(), size.height.roundToInt()),
                layoutDirection
            )

            val dx = alignedPosition.x.toFloat()
            val dy = alignedPosition.y.toFloat()

            // 3. Calculate src and dst Rects for drawImage
            val srcRect = Rect(0f, 0f, srcSize.width, srcSize.height)
            val dstRect = Rect(dx, dy, dx + scaledSize.width, dy + scaledSize.height)

            drawBehind()
//            translate(dx, dy) {
//
////                with(painter) {
////                    draw(size = scaledSize, alpha = alpha, colorFilter = colorFilter)
////                }
//            }

            // 1. Create a Paint object
            val paint = Paint().apply {
                alpha = this@ImageBackgroundNode.alpha
                colorFilter = this@ImageBackgroundNode.colorFilter
                blendMode = this@ImageBackgroundNode.blendMode
                filterQuality = this@ImageBackgroundNode.filterQuality
//                style = this@ImageBackgroundNode.style
            }

            // 3. Draw the image with repeat
            drawBehind()
            drawTiledImage(
                canvas = canvas,
                image = image,
                srcRect = srcRect,
                dstRect = dstRect,
                repeat = repeat,
                paint = paint
            )

//            drawImage(
//                image = image,
//                srcOffset = IntOffset.Zero, // We're using srcRect for source area
//                srcSize = IntSize(srcRect.width.toInt(), srcRect.height.toInt()),
//                dstOffset = IntOffset(dstRect.left.toInt(), dstRect.top.toInt()),
//                dstSize = IntSize(dstRect.width.toInt(), dstRect.height.toInt()),
//                alpha = alpha,
//                style = style,
//                colorFilter = colorFilter,
//                blendMode = blendMode,
//                filterQuality = filterQuality,
//            )

            drawFront()

            // 3. Restore the canvas state for the content
            canvas.restore()
        }
        drawContent()
    }
}

/**
 * Draws a tiled image on the canvas based on the specified repeat mode.
 */
private fun drawTiledImage(
    canvas: Canvas,
    image: ImageBitmap,
    srcRect: Rect,
    dstRect: Rect,
    repeat: BackgroundRepeat,
    paint: Paint
) {
    when (repeat) {
        BackgroundRepeat.Repeat -> {
            // Tile the image in both directions
            val tileWidth = srcRect.width.toInt()
            val tileHeight = srcRect.height.toInt()

            var x = dstRect.left.toInt()
            while (x < dstRect.right) {
                var y = dstRect.top.toInt()
                while (y < dstRect.bottom) {
                    val tileDstRect = Rect(x.toFloat(), y.toFloat(), (x + tileWidth).toFloat(), (y + tileHeight).toFloat())
                    canvas.drawImageRect(
                        image = image,
                        srcOffset = IntOffset.Zero,
                        srcSize = IntSize(srcRect.width.toInt(), srcRect.height.toInt()),
                        dstOffset = IntOffset(tileDstRect.left.toInt(), tileDstRect.top.toInt()),
                        dstSize = IntSize(tileDstRect.width.toInt(), tileDstRect.height.toInt()),
                        paint = paint
                    )
                    y += tileHeight
                }
                x += tileWidth
            }
        }
        BackgroundRepeat.RepeatX -> {
            // Tile horizontally only
            // ... (implementation similar to Repeat, but only iterate along x)
        }
        BackgroundRepeat.RepeatY -> {
            // Tile vertically only
            // ... (implementation similar to Repeat, but only iterate along y)
        }
        BackgroundRepeat.NoRepeat -> {
            // Draw only once
            canvas.drawImageRect(
                image = image,
                srcOffset = IntOffset.Zero,
                srcSize = IntSize(srcRect.width.toInt(), srcRect.height.toInt()),
                dstOffset = IntOffset(dstRect.left.toInt(), dstRect.top.toInt()),
                dstSize = IntSize(dstRect.width.toInt(), dstRect.height.toInt()),
                paint = paint
            )
        }
    }
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
            Size(srcWidth * scale, srcHeight * scale) // Initially scale to fill or exceed bounds

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

/**
 * Enum class representing CSS-like background repeat behavior.
 */
enum class BackgroundRepeat {
    Repeat,
    RepeatX,
    RepeatY,
    NoRepeat
}