/*
 * Copyright 2020 The Android Open Source Project
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

package com.thekr.ui.modifier

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.times
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.node.invalidateMeasurement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Paints and optionally repeats the content using [painter].
 *
 * @param painter used to paint content
 * @param repeat defines the repetition behavior of the painter
 * @param sizeToIntrinsics `true` to size the element relative to
 *     [Painter.intrinsicSize]
 * @param alignment specifies alignment of the [painter] relative to
 *     content
 * @param contentScale strategy for scaling [painter] if its size does not
 *     match the content size
 * @param alpha opacity of [painter]
 * @param colorFilter optional [ColorFilter] to apply to [painter]
 */
fun Modifier.paint(
    painter: Painter,
    repeat: PaintingRepeat = PaintingRepeat.NoRepeat,
    sizeToIntrinsics: Boolean = true,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Inside,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) = this then PainterElement(
    painter = painter,
    repeat = repeat,
    sizeToIntrinsics = sizeToIntrinsics,
    alignment = alignment,
    contentScale = contentScale,
    alpha = alpha,
    colorFilter = colorFilter
)

/**
 * Customized [ModifierNodeElement] for painting content using [painter].
 *
 * @param painter used to paint content
 * @param repeat defines the repetition behavior of the painter
 * @param sizeToIntrinsics `true` to size the element relative to
 *     [Painter.intrinsicSize]
 * @param alignment specifies alignment of the [painter] relative to
 *     content
 * @param contentScale strategy for scaling [painter] if its size does not
 *     match the content size
 * @param alpha opacity of [painter]
 * @param colorFilter optional [ColorFilter] to apply to [painter]
 */
private data class PainterElement(
    val painter: Painter,
    val repeat: PaintingRepeat,
    val sizeToIntrinsics: Boolean,
    val alignment: Alignment,
    val contentScale: ContentScale,
    val alpha: Float,
    val colorFilter: ColorFilter?
) : ModifierNodeElement<PainterNode>() {
    override fun create(): PainterNode {
        return PainterNode(
            painter = painter,
            repeat = repeat,
            sizeToIntrinsics = sizeToIntrinsics,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
        )
    }

    override fun update(node: PainterNode) {
        val intrinsicsChanged = node.sizeToIntrinsics != sizeToIntrinsics ||
                (sizeToIntrinsics && node.painter.intrinsicSize != painter.intrinsicSize)

        node.painter = painter
        node.repeat = repeat
        node.sizeToIntrinsics = sizeToIntrinsics
        node.alignment = alignment
        node.contentScale = contentScale
        node.alpha = alpha
        node.colorFilter = colorFilter

        // Only remeasure if intrinsics have changed.
        if (intrinsicsChanged) {
            node.invalidateMeasurement()
        }
        // redraw because one of the node properties has changed.
        node.invalidateDraw()
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "paint"
        properties["painter"] = painter
        properties["repeat"] = repeat
        properties["sizeToIntrinsics"] = sizeToIntrinsics
        properties["alignment"] = alignment
        properties["contentScale"] = contentScale
        properties["alpha"] = alpha
        properties["colorFilter"] = colorFilter
    }
}

/**
 * [DrawModifier] used to draw the provided [Painter] followed by the
 * contents of the component itself
 *
 * IMPORTANT NOTE: This class sets
 * [androidx.compose.ui.Modifier.Node.shouldAutoInvalidate] to false
 * which means it MUST invalidate both draw and the layout. It
 * invalidates both in the [PainterElement.update] method through
 * [LayoutModifierNode.invalidateLayer] (invalidates draw) and
 * [LayoutModifierNode.invalidateLayout] (invalidates layout).
 */
private class PainterNode(
    var painter: Painter,
    var repeat: PaintingRepeat,
    var sizeToIntrinsics: Boolean,
    var alignment: Alignment = Alignment.Center,
    var contentScale: ContentScale = ContentScale.Inside,
    var alpha: Float = DefaultAlpha,
    var colorFilter: ColorFilter? = null
) : LayoutModifierNode, Modifier.Node(), DrawModifierNode {

    /**
     * Helper property to determine if we should size content to the intrinsic
     * size of the Painter or not. This is only done if [sizeToIntrinsics] is
     * true and the Painter has an intrinsic size
     */
    private val useIntrinsicSize: Boolean
        get() = sizeToIntrinsics && painter.intrinsicSize.isSpecified

    override val shouldAutoInvalidate: Boolean
        get() = false

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(modifyConstraints(constraints))
        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ): Int {
        return if (useIntrinsicSize) {
            val constraints = modifyConstraints(Constraints(maxHeight = height))
            val layoutWidth = measurable.minIntrinsicWidth(height)
            max(constraints.minWidth, layoutWidth)
        } else {
            measurable.minIntrinsicWidth(height)
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ): Int {
        return if (useIntrinsicSize) {
            val constraints = modifyConstraints(Constraints(maxHeight = height))
            val layoutWidth = measurable.maxIntrinsicWidth(height)
            max(constraints.minWidth, layoutWidth)
        } else {
            measurable.maxIntrinsicWidth(height)
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ): Int {
        return if (useIntrinsicSize) {
            val constraints = modifyConstraints(Constraints(maxWidth = width))
            val layoutHeight = measurable.minIntrinsicHeight(width)
            max(constraints.minHeight, layoutHeight)
        } else {
            measurable.minIntrinsicHeight(width)
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ): Int {
        return if (useIntrinsicSize) {
            val constraints = modifyConstraints(Constraints(maxWidth = width))
            val layoutHeight = measurable.maxIntrinsicHeight(width)
            max(constraints.minHeight, layoutHeight)
        } else {
            measurable.maxIntrinsicHeight(width)
        }
    }

    private fun calculateScaledSize(dstSize: Size): Size {
        return if (!useIntrinsicSize) {
            dstSize
        } else {
            val srcWidth = if (!painter.intrinsicSize.hasSpecifiedAndFiniteWidth()) {
                dstSize.width
            } else {
                painter.intrinsicSize.width
            }

            val srcHeight = if (!painter.intrinsicSize.hasSpecifiedAndFiniteHeight()) {
                dstSize.height
            } else {
                painter.intrinsicSize.height
            }

            val srcSize = Size(srcWidth, srcHeight)
            if (dstSize.width != 0f && dstSize.height != 0f) {
                srcSize * contentScale.computeScaleFactor(srcSize, dstSize)
            } else {
                Size.Zero
            }
        }
    }

    private fun modifyConstraints(constraints: Constraints): Constraints {
        // When repeating, don't let intrinsic size influence constraints
        if (repeat != PaintingRepeat.NoRepeat) {
            return constraints
        }

        val hasBoundedDimens = constraints.hasBoundedWidth && constraints.hasBoundedHeight
        val hasFixedDimens = constraints.hasFixedWidth && constraints.hasFixedHeight
        if ((!useIntrinsicSize && hasBoundedDimens) || hasFixedDimens) {
            return constraints.copy(
                minWidth = constraints.maxWidth,
                minHeight = constraints.maxHeight
            )
        }

        // Rest of constraint logic only applies for NoRepeat
        val intrinsicSize = painter.intrinsicSize
        val intrinsicWidth = if (intrinsicSize.hasSpecifiedAndFiniteWidth()) {
            intrinsicSize.width.roundToInt()
        } else {
            constraints.minWidth
        }

        val intrinsicHeight = if (intrinsicSize.hasSpecifiedAndFiniteHeight()) {
            intrinsicSize.height.roundToInt()
        } else {
            constraints.minHeight
        }

        val constrainedWidth = constraints.constrainWidth(intrinsicWidth)
        val constrainedHeight = constraints.constrainHeight(intrinsicHeight)
        val scaledSize = calculateScaledSize(
            Size(constrainedWidth.toFloat(), constrainedHeight.toFloat())
        )

        val minWidth = constraints.constrainWidth(scaledSize.width.roundToInt())
        val minHeight = constraints.constrainHeight(scaledSize.height.roundToInt())
        
        return constraints.copy(minWidth = minWidth, minHeight = minHeight)
    }

    override fun ContentDrawScope.draw() {
        val intrinsicSize = painter.intrinsicSize

        val srcWidth =
            if (intrinsicSize.hasSpecifiedAndFiniteWidth()) {
                intrinsicSize.width
            } else {
                size.width
            }

        val srcHeight =
            if (intrinsicSize.hasSpecifiedAndFiniteHeight()) {
                intrinsicSize.height
            } else {
                size.height
            }

        val srcSize = Size(srcWidth, srcHeight)

        // Compute the offset to translate the content based on the given alignment
        // and size to draw based on the ContentScale parameter
        val scaledSize =
            if (size.width != 0f && size.height != 0f) {
                srcSize * contentScale.computeScaleFactor(srcSize, size)
            } else {
                Size.Zero
            }

        val alignedPosition = alignment.align(
            IntSize(scaledSize.width.roundToInt(), scaledSize.height.roundToInt()),
            IntSize(size.width.roundToInt(), size.height.roundToInt()),
            layoutDirection
        )

        val tileSize = if (repeat != PaintingRepeat.NoRepeat) {
            // Calculate tile size that maintains aspect ratio and fits within bounds
            val targetWidth = (size.width / (size.width / scaledSize.width).roundToInt())
            val targetHeight = (size.height / (size.height / scaledSize.height).roundToInt())
            Size(targetWidth, targetHeight)
        } else {
            scaledSize
        }

        if (repeat == PaintingRepeat.NoRepeat) {
            translate(alignedPosition.x.toFloat(), alignedPosition.y.toFloat()) {
                with(painter) {
                    draw(size = tileSize, alpha = alpha, colorFilter = colorFilter)
                }
            }
        } else {
            var currentX = 0f
            while (currentX < size.width) {
                var currentY = 0f
                while (currentY < size.height) {
                    translate(currentX, currentY) {
                        with(painter) {
                            draw(tileSize, alpha, colorFilter)
                        }
                    }
                    if (repeat == PaintingRepeat.RepeatX) break
                    currentY += tileSize.height
                }
                if (repeat == PaintingRepeat.RepeatY) break
                currentX += tileSize.width
            }
        }

        drawContent()
    }

    private fun Size.hasSpecifiedAndFiniteWidth() = this != Size.Unspecified && width.isFinite()
    private fun Size.hasSpecifiedAndFiniteHeight() = this != Size.Unspecified && height.isFinite()

    override fun toString(): String =
        "PainterModifier(" +
                "painter=$painter, " +
                "repeat=$repeat, " +
                "sizeToIntrinsics=$sizeToIntrinsics, " +
                "alignment=$alignment, " +
                "alpha=$alpha, " +
                "colorFilter=$colorFilter)"
}

/** Enum class representing painting repeat behavior. */
enum class PaintingRepeat {
    Repeat,
    RepeatX,
    RepeatY,
    NoRepeat
}