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
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.IntSize
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
fun Modifier.background(
    painter: Painter,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Inside,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
): Modifier = this.then(
    BackgroundPainterElement(
        painter = painter,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        inspectorInfo = debugInspectorInfo {
            name = "background"
            properties["painter"] = painter
            properties["alignment"] = alignment
            properties["contentScale"] = contentScale
            properties["alpha"] = alpha
            properties["colorFilter"] = colorFilter
        }
    )
)

private class BackgroundPainterElement(
    val painter: Painter,
    val alignment: Alignment,
    val contentScale: ContentScale,
    val alpha: Float,
    val colorFilter: ColorFilter?,
    val inspectorInfo: InspectorInfo.() -> Unit
) : ModifierNodeElement<BackgroundPainterNode>() {
    override fun create(): BackgroundPainterNode {
        return BackgroundPainterNode(
            painter = painter,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
        )
    }

    override fun update(node: BackgroundPainterNode) {
        node.painter = painter
        node.alignment = alignment
        node.contentScale = contentScale
        node.alpha = alpha
        node.colorFilter = colorFilter

        node.invalidateDraw()
    }

    override fun InspectorInfo.inspectableProperties() {
        inspectorInfo()
    }

    override fun hashCode(): Int {
        var result = painter.hashCode()
        result = 31 * result + alignment.hashCode()
        result = 31 * result + contentScale.hashCode()
        result = 31 * result + alpha.hashCode()
        result = 31 * result + (colorFilter?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BackgroundPainterElement) return false

        if (painter != other.painter) return false
        if (alignment != other.alignment) return false
        if (contentScale != other.contentScale) return false
        if (alpha != other.alpha) return false
        if (colorFilter != other.colorFilter) return false

        return true
    }
}

private class BackgroundPainterNode(
    var painter: Painter,
    var alignment: Alignment,
    var contentScale: ContentScale,
    var alpha: Float,
    var colorFilter: ColorFilter?
) : DrawModifierNode, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        val intrinsicSize = painter.intrinsicSize

        val srcWidth = if (intrinsicSize.isSpecified) intrinsicSize.width else size.width
        val srcHeight = if (intrinsicSize.isSpecified) intrinsicSize.height else size.height

        val srcSize = Size(srcWidth, srcHeight)

        val scaleFactor = contentScale.computeScaleFactor(srcSize, size)

        val scaledSize:Size = if (size.width != 0f && size.height != 0f) {
            Size(srcSize.width * scaleFactor.scaleX, srcSize.height * scaleFactor.scaleY)
        } else {
            Size.Zero
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

        drawContent()
    }
}