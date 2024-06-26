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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultBlendMode
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.times
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection

/**
 * Draws an [ImageBitmap] with the given parameters and clipping shape
 * behind the content.
 *
 * @param image The ImageBitmap to draw
 * @param shape desired shape of the background
 * @param srcOffset The offset in the image to start drawing from
 * @param srcSize The size of the source image to draw
 * @param dstOffset The offset in the destination to start drawing the
 *     image at
 * @param dstSize The size in the destination to draw the image
 * @param alpha Opacity to be applied to [image], with `0` being completely
 *     transparent and `1` being completely opaque. The value must be
 *     between `0` and `1`.
 * @param style Whether to fill or stroke the destination with the image
 * @param colorFilter ColorFilter to apply to the image when drawn
 * @param blendMode Blending algorithm to be applied to the image
 * @param filterQuality Sampling algorithm applied to the image when it is
 *     scaled and drawn
 */
@Stable
fun Modifier.background(
    image: ImageBitmap,
    shape: Shape = RectangleShape,
    srcOffset: IntOffset = IntOffset.Zero,
    srcSize: IntSize = IntSize(image.width, image.height),
    dstOffset: IntOffset = IntOffset.Zero,
    dstSize: IntSize = srcSize,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
    style: DrawStyle = Fill,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DefaultBlendMode,
    filterQuality: FilterQuality = FilterQuality.Low,
): Modifier = this.then(
    ImageBackgroundElement(
        image = image,
        shape = shape,
        srcOffset = srcOffset,
        srcSize = srcSize,
        dstOffset = dstOffset,
        dstSize = dstSize,
        alpha = alpha,
        style = style,
        colorFilter = colorFilter,
        blendMode = blendMode,
        filterQuality = filterQuality,
        inspectorInfo = debugInspectorInfo {
            name = "background"
            properties["image"] = image
            properties["shape"] = shape
            properties["srcOffset"] = srcOffset
            properties["srcSize"] = srcSize
            properties["dstOffset"] = dstOffset
            properties["dstSize"] = dstSize
            properties["alpha"] = alpha
            properties["style"] = style
            properties["colorFilter"] = colorFilter
            properties["blendMode"] = blendMode
            properties["filterQuality"] = filterQuality
        }
    )
)

private class ImageBackgroundElement(
    private val image: ImageBitmap,
    private val shape: Shape,
    private val srcOffset: IntOffset,
    private val srcSize: IntSize,
    private val dstOffset: IntOffset,
    private val dstSize: IntSize,
    private val alpha: Float,
    private val style: DrawStyle,
    private val colorFilter: ColorFilter?,
    private val blendMode: BlendMode,
    private val filterQuality: FilterQuality,
    private val inspectorInfo: InspectorInfo.() -> Unit
) : ModifierNodeElement<ImageBackgroundNode>() {
    override fun create(): ImageBackgroundNode {
        return ImageBackgroundNode(
            image,
            shape,
            srcOffset,
            srcSize,
            dstOffset,
            dstSize,
            alpha,
            style,
            colorFilter,
            blendMode,
            filterQuality
        )
    }

    override fun update(node: ImageBackgroundNode) {
        node.image = image
        node.shape = shape
        node.srcOffset = srcOffset
        node.srcSize = srcSize
        node.dstOffset = dstOffset
        node.dstSize = dstSize
        node.alpha = alpha
        node.style = style
        node.colorFilter = colorFilter
        node.blendMode = blendMode
        node.filterQuality = filterQuality
    }

    override fun InspectorInfo.inspectableProperties() {
        inspectorInfo()
    }

    override fun hashCode(): Int {
        var result = image.hashCode()
        result = 31 * result + shape.hashCode()
        result = 31 * result + srcOffset.hashCode()
        result = 31 * result + srcSize.hashCode()
        result = 31 * result + dstOffset.hashCode()
        result = 31 * result + dstSize.hashCode()
        result = 31 * result + alpha.hashCode()
        result = 31 * result + style.hashCode()
        result = 31 * result + (colorFilter?.hashCode() ?: 0)
        result = 31 * result + blendMode.hashCode()
        result = 31 * result + filterQuality.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        val otherModifier = other as? ImageBackgroundElement ?: return false
        return image == otherModifier.image &&
                shape == otherModifier.shape &&
                srcOffset == otherModifier.srcOffset &&
                srcSize == otherModifier.srcSize &&
                dstOffset == otherModifier.dstOffset &&
                dstSize == otherModifier.dstSize &&
                alpha == otherModifier.alpha &&
                style == otherModifier.style &&
                colorFilter == otherModifier.colorFilter &&
                blendMode == otherModifier.blendMode &&
                filterQuality == otherModifier.filterQuality
    }
}

private class ImageBackgroundNode(
    var image: ImageBitmap,
    var shape: Shape,
    var srcOffset: IntOffset,
    var srcSize: Size,
    var dstOffset: IntOffset,
    var dstSize: Size,
    var alpha: Float,
    var style: DrawStyle,
    var colorFilter: ColorFilter?,
    var blendMode: BlendMode,
    var filterQuality: FilterQuality
) : DrawModifierNode, Modifier.Node() {

    override fun ContentDrawScope.draw() {
        val size = this.size
        drawIntoCanvas { canvas ->
            val canvasSize = size
            val srcRect = srcOffset.toRect(srcSize)
            val dstRect = dstOffset.toRect(dstSize)
            canvas.save()
            shape.createOutline(size, layoutDirection, this).apply {
                canvas.clipPath(this.path)
            }

            // The image is drawn with srcRect and dstRect, but it may need to be scaled to fit
            // within the size of the layout. Calculate the scale to apply to the image so that
            // either its width or height matches the maximum dimension of the layout.
            val scaleFactor = computeScaleFactor(srcSize, dstSize, canvasSize)

            val scaledSrcWidth = srcRect.width * scaleFactor
            val scaledSrcHeight = srcRect.height * scaleFactor

            val scaledDstWidth = dstRect.width * scaleFactor
            val scaledDstHeight = dstRect.height * scaleFactor

            val scaledSrcOffset = Offset(
                srcRect.left * scaleFactor,
                srcRect.top * scaleFactor
            )
            val scaledDstOffset = Offset(
                dstRect.left * scaleFactor,
                dstRect.top * scaleFactor
            )
            canvas.drawImageRect(
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
        srcSize: Size,
        dstSize: Size,
        canvasSize: Size
    ): Float {
        val scaleFactorX = if (dstSize.width != 0f) {
            canvasSize.width / dstSize.width
        } else {
            1f
        }
        val scaleFactorY = if (dstSize.height != 0f) {
            canvasSize.height.toFloat() / dstSize.height
        } else {
            1f
        }
        return minOf(scaleFactorX, scaleFactorY)
    }
}

private fun IntOffset.toRect(size: Size): Rect =
    Rect(
        left = this.x.toFloat(),
        top = this.y.toFloat(),
        right = this.x + size.width,
        bottom = this.y + size.height
    )