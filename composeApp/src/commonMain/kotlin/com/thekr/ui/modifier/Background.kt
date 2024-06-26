//package com.thekr.ui.modifier
//
//import androidx.compose.runtime.Stable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.graphics.Outline
//import androidx.compose.ui.graphics.Paint
//import androidx.compose.ui.graphics.PaintingStyle
//import androidx.compose.ui.graphics.RectangleShape
//import androidx.compose.ui.graphics.Shape
//import androidx.compose.ui.graphics.asComposePaint
//import androidx.compose.ui.graphics.drawOutline
//import androidx.compose.ui.graphics.drawscope.ContentDrawScope
//import androidx.compose.ui.graphics.drawscope.clipPath
//import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
//import androidx.compose.ui.graphics.painter.Painter
//import androidx.compose.ui.node.DrawModifierNode
//import androidx.compose.ui.node.ModifierNodeElement
//import androidx.compose.ui.platform.InspectorInfo
//import androidx.compose.ui.platform.debugInspectorInfo
//import androidx.compose.ui.unit.IntSize
//import androidx.compose.ui.unit.LayoutDirection
//
///**
// * Draws [shape] with a [painter] behind the content.
// *
// * @sample androidx.compose.foundation.samples.DrawBackgroundPainter
// *
// * @param painter Painter to be drawn behind the content
// * @param shape desired shape of the background
// */
//@Stable
//fun Modifier.background(
//    painter: Painter,
//    shape: Shape = RectangleShape
//): Modifier = this.then(
//    BackgroundPainterElement(
//        painter = painter,
//        shape = shape,
//        inspectorInfo = debugInspectorInfo {
//            name = "background"
//            properties["painter"] = painter
//            properties["shape"] = shape
//        }
//    )
//)
//
///**
// * Draws [shape] with an [ImageBitmap] behind the content.
// *
// * @sample androidx.compose.foundation.samples.DrawBackgroundImageBitmap
// *
// * @param image ImageBitmap to be drawn behind the content
// * @param shape desired shape of the background
// */
//@Stable
//fun Modifier.background(
//    image: ImageBitmap,
//    shape: Shape = RectangleShape
//): Modifier = this.then(
//    BackgroundBitmapElement(
//        image = image,
//        shape = shape,
//        inspectorInfo = debugInspectorInfo {
//            name = "background"
//            properties["image"] = image
//            properties["shape"] = shape
//        }
//    )
//)
//
//private class BackgroundPainterElement(
//    private val painter: Painter,
//    private val shape: Shape,
//    private val inspectorInfo: InspectorInfo.() -> Unit
//) : ModifierNodeElement<BackgroundNode>() {
//    override fun create(): BackgroundNode {
//        return BackgroundNode(
//            painter = painter,
//            shape = shape
//        )
//    }
//
//    override fun update(node: BackgroundNode) {
//        node.painter = painter
//        node.shape = shape
//    }
//
//    override fun InspectorInfo.inspectableProperties() {
//        inspectorInfo()
//    }
//
//    override fun hashCode(): Int {
//        var result = painter.hashCode()
//        result = 31 * result + shape.hashCode()
//        return result
//    }
//
//    override fun equals(other: Any?): Boolean {
//        val otherModifier = other as? BackgroundPainterElement ?: return false
//        return painter == otherModifier.painter &&
//                shape == otherModifier.shape
//    }
//}
//
//private class BackgroundBitmapElement(
//    private val image: ImageBitmap,
//    private val shape: Shape,
//    private val inspectorInfo: InspectorInfo.() -> Unit
//) : ModifierNodeElement<BackgroundNode>() {
//    override fun create(): BackgroundNode {
//        return BackgroundNode(
//            image = image,
//            shape = shape
//        )
//    }
//
//    override fun update(node: BackgroundNode) {
//        node.image = image
//        node.shape = shape
//    }
//
//    override fun InspectorInfo.inspectableProperties() {
//        inspectorInfo()
//    }
//
//    override fun hashCode(): Int {
//        var result = image.hashCode()
//        result = 31 * result + shape.hashCode()
//        return result
//    }
//
//    override fun equals(other: Any?): Boolean {
//        val otherModifier = other as? BackgroundBitmapElement ?: return false
//        return image == otherModifier.image &&
//                shape == otherModifier.shape
//    }
//}
//
//private class BackgroundNode(
//    var painter: Painter? = null,
//    var image: ImageBitmap? = null,
//    var shape: Shape,
//) : DrawModifierNode, Modifier.Node() {
//
//    // naive cache outline calculation if size is the same
//    private var lastSize: Size? = null
//    private var lastLayoutDirection: LayoutDirection? = null
//    private var lastOutline: Outline? = null
//    private var lastShape: Shape? = null
//
//    override fun ContentDrawScope.draw() {
//        if (shape === RectangleShape) {
//            // shortcut to avoid Outline calculation and allocation
//            drawRect()
//        } else {
//            drawOutline()
//        }
//        drawContent()
//    }
//
//    private fun ContentDrawScope.drawRect() {
//        if (painter != null) drawRect(painter = painter!!)
//        else if (image != null) drawRect(image = image!!)
//    }
//
//    private fun ContentDrawScope.drawOutline() {
//        val outline =
//            if (size == lastSize && layoutDirection == lastLayoutDirection && lastShape == shape) {
//                lastOutline!!
//            } else {
//                shape.createOutline(size, layoutDirection, this)
//            }
//        if (painter != null) drawOutline(outline, painter = painter!!)
//        else if (image != null) drawOutline(outline, image = image!!)
//        lastOutline = outline
//        lastSize = size
//        lastLayoutDirection = layoutDirection
//        lastShape = shape
//    }
//
//    private fun ContentDrawScope.drawRect(painter: Painter) {
//        drawIntoCanvas { canvas ->
//            val paint = Paint().asFrameworkPaint().apply {
//                isAntiAlias = true
//            }
//
//            canvas.drawRect(
//                0f,
//                0f,
//                size.width,
//                size.height,
//                paint.asComposePaint()
//            )
//            canvas.save()
//            // translate to local position
//            canvas.translate(
//                -this@drawRect.size.width / 2,
//                -this@drawRect.size.height / 2
//            )
////            painter.draw(canvas.toAndroidCanvas(), size)
//            canvas.restore()
//        }
//    }
//
//    private fun ContentDrawScope.drawRect(image: ImageBitmap) {
//        drawImage(
//            image = image,
//            dstSize = IntSize(size.width.toInt(), size.height.toInt())
//        )
//    }
//
//    private fun ContentDrawScope.drawOutline(outline: Outline, painter: Painter) {
////        clipPath(path = outline.path) {
////        }
//        drawRect(painter = painter)
//
//    }
//
//    private fun ContentDrawScope.drawOutline(outline: Outline, image: ImageBitmap) {
////        clipPath(path = outline.path) {
////
////        }
//        drawRect(image = image)
//    }
//}