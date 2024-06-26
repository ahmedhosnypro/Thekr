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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
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

/**
 * Draws an [ImageBitmap] with the given parameters and clipping shape
 * behind the content.
 *
 * @param bitmap The ImageBitmap to draw
 * @param shape desired shape of the background
 * @param alignment Alignment of the image within the layout bounds
 * @param contentScale Strategy for scaling the image if its size does not
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
            properties["alpha"] = alpha
            properties["style"] = style
            properties["colorFilter"] = colorFilter
            properties["blendMode"] = blendMode
            properties["filterQuality"] = filterQuality
        }
    )
}

private class ImageBackgroundElement(
    private val image: ImageBitmap,
    private val painter: BitmapPainter,
    private val shape: Shape,
    private val alignment: Alignment,
    private val contentScale: ContentScale,
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

            val intrinsicSize =
            val srcWidth = if (intrinsicSize.isSpecified) intrinsicSize.width else size.width
            val srcHeight = if (intrinsicSize.isSpecified) intrinsicSize.height else size.height
            val srcSize = Size(srcWidth, srcHeight)
            val srcRect = srcOffset.toRect(srcSize)
            val dstRect = size.toRect()
            canvas.save()
            shape.createOutline(size, layoutDirection, this).apply {

//                this.bounds.let {
//                    canvas.clipRect(
//                        left = it.left,
//                        top = it.top,
//                        right = it.right,
//                        bottom = it.bottom
//                    )
//                }
                // todo: try this
                canvas.clipPath(Path().apply {
                    addRect(size.toRect())
                })
            }

            // The image is drawn with srcRect and dstRect, but it may need to be scaled to fit
            // within the size of the layout. Calculate the scale to apply to the image so that
            // either its width or height matches the maximum dimension of the layout.
            val scaleFactor = computeScaleFactor(srcSize, dstSize, size)

            val scaledSrcWidth = srcRect.width * scaleFactor
            val scaledSrcHeight = srcRect.height * scaleFactor

            val scaledDstWidth = dstRect.width * scaleFactor
            val scaledDstHeight = dstRect.height * scaleFactor

            val scaledSrcOffset = IntOffset(
                (srcRect.left * scaleFactor).toInt(),
                (srcRect.top * scaleFactor).toInt()
            )
            val scaledDstOffset = IntOffset(
                (dstRect.left * scaleFactor).toInt(),
                (dstRect.top * scaleFactor).toInt()
            )


            drawImage(
                image = image,
                srcOffset = scaledSrcOffset,
                srcSize = IntSize(scaledSrcWidth.toInt(), scaledSrcHeight.toInt()),
                dstOffset = scaledDstOffset,
                dstSize = IntSize(scaledDstWidth.toInt(), scaledDstHeight.toInt()),
                alpha = alpha,
                style = style,
                colorFilter = colorFilter,
                blendMode = blendMode,
                filterQuality = filterQuality,
            )
            canvas.restore()
        }
        drawContent()
    }

    private fun computeScaleFactor(
        srcSize: IntSize,
        dstSize: IntSize,
        canvasSize: Size
    ): Float {
        val scaleFactorX = if (dstSize.width != 0) {
            canvasSize.width / dstSize.width
        } else {
            1f
        }
        val scaleFactorY = if (dstSize.height != 0) {
            canvasSize.height / dstSize.height
        } else {
            1f
        }
        return minOf(scaleFactorX, scaleFactorY)
    }
}

private fun IntOffset.toRect(size: IntSize): Rect =
    Rect(
        left = this.x.toFloat(),
        top = this.y.toFloat(),
        right = this.x + size.width.toFloat(),
        bottom = this.y + size.height.toFloat()
    )