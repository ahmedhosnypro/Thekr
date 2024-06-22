//package com.thekr.ui.counter.stats.chart
//
//import android.graphics.RectF
//import android.text.Spannable
//import android.text.style.ForegroundColorSpan
//import com.patrykandpatrick.vico.core.chart.dimensions.HorizontalDimensions
//import com.patrykandpatrick.vico.core.chart.insets.Insets
//import com.patrykandpatrick.vico.core.chart.values.ChartValues
//import com.patrykandpatrick.vico.core.component.Component
//import com.patrykandpatrick.vico.core.component.shape.LineComponent
//import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
//import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
//import com.patrykandpatrick.vico.core.component.text.TextComponent
//import com.patrykandpatrick.vico.core.component.text.VerticalPosition
//import com.patrykandpatrick.vico.core.context.DrawContext
//import com.patrykandpatrick.vico.core.context.MeasureContext
//import com.patrykandpatrick.vico.core.extension.appendCompat
//import com.patrykandpatrick.vico.core.extension.ceil
//import com.patrykandpatrick.vico.core.extension.doubled
//import com.patrykandpatrick.vico.core.extension.half
//import com.patrykandpatrick.vico.core.extension.orZero
//import com.patrykandpatrick.vico.core.extension.transformToSpannable
//import com.patrykandpatrick.vico.core.marker.Marker
//import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
//import com.patrykandpatrick.vico.core.model.CartesianLayerModel
//import com.patrykandpatrick.vico.core.model.ColumnCartesianLayerModel
//import com.patrykandpatrick.vico.core.model.LineCartesianLayerModel
//import java.text.NumberFormat
//import java.util.Locale
//
//open class MyMarkerComponent(
//    val label: TextComponent,
//    val indicator: Component?,
//    val guideline: LineComponent?,
//) : Marker {
//    private val tempBounds = RectF()
//
//    private val TextComponent.tickSizeDp: Float
//        get() = ((background as? ShapeComponent)?.shape as? MarkerCorneredShape)?.tickSizeDp.orZero
//
//    /** The indicator size (in dp). */
//    var indicatorSizeDp: Float = 0f
//
//    /**
//     * An optional lambda function that allows for applying the color
//     * associated with a given data entry to a [Component].
//     */
//    var onApplyEntryColor: ((entryColor: Int) -> Unit)? = null
//
//    /** The [MarkerLabelFormatter] for this marker. */
//    var labelFormatter: MarkerLabelFormatter = MyMarkerLabelFormatter()
//
//    override fun draw(
//        context: DrawContext,
//        bounds: RectF,
//        markedEntries: List<Marker.EntryModel>,
//        chartValues: ChartValues,
//    ): Unit =
//        with(context) {
//            drawGuideline(context, bounds, markedEntries)
//            val halfIndicatorSize = indicatorSizeDp.half.pixels
//
//            markedEntries.forEachIndexed { _, model ->
//                onApplyEntryColor?.invoke(model.color)
//                indicator?.draw(
//                    context,
//                    model.location.x - halfIndicatorSize,
//                    model.location.y - halfIndicatorSize,
//                    model.location.x + halfIndicatorSize,
//                    model.location.y + halfIndicatorSize,
//                )
//            }
//            drawLabel(context, bounds, markedEntries, chartValues)
//        }
//
//
//    // ensure that the label is in English Number format
//    private fun drawLabel(
//        context: DrawContext,
//        bounds: RectF,
//        markedEntries: List<Marker.EntryModel>,
//        chartValues: ChartValues,
//    ): Unit =
//        with(context) {
//            val text = labelFormatter.getLabel(markedEntries, chartValues)
//            val entryX = markedEntries.averageOf { it.location.x }
//            val labelBounds =
//                label.getTextBounds(
//                    context = context,
//                    text = text,
//                    width = bounds.width().toInt(),
//                    outRect = tempBounds,
//                )
//            val halfOfTextWidth = labelBounds.width().half
//            val x = overrideXPositionToFit(entryX, bounds, halfOfTextWidth)
//            this[MarkerCorneredShape.TICK_X_KEY] = entryX
//
//            label.drawText(
//                context = context,
//                text = text,
//                textX = x,
//                textY = bounds.top - labelBounds.height() - label.tickSizeDp.pixels,
//                verticalPosition = VerticalPosition.Bottom,
//                maxTextWidth = minOf(bounds.right - x, x - bounds.left).doubled.ceil.toInt(),
//            )
//        }
//
//    private fun overrideXPositionToFit(
//        xPosition: Float,
//        bounds: RectF,
//        halfOfTextWidth: Float,
//    ): Float =
//        when {
//            xPosition - halfOfTextWidth < bounds.left -> bounds.left + halfOfTextWidth
//            xPosition + halfOfTextWidth > bounds.right -> bounds.right - halfOfTextWidth
//            else -> xPosition
//        }
//
//    private fun drawGuideline(
//        context: DrawContext,
//        bounds: RectF,
//        markedEntries: List<Marker.EntryModel>,
//    ) {
//        markedEntries
//            .map { it.location.x }
//            .toSet()
//            .forEach { x ->
//                guideline?.drawVertical(
//                    context,
//                    bounds.top,
//                    bounds.bottom,
//                    x,
//                )
//            }
//    }
//
//    override fun getInsets(
//        context: MeasureContext,
//        outInsets: Insets,
//        horizontalDimensions: HorizontalDimensions,
//    ): Unit =
//        with(context) {
//            outInsets.top = label.getHeight(context) + label.tickSizeDp.pixels
//        }
//}
//
//private fun <T> Collection<T>.averageOf(selector: (T) -> Float): Float =
//    fold(0f) { sum, element ->
//        sum + selector(element)
//    } / size
//
//
//class MyMarkerLabelFormatter(private val colorCode: Boolean = true) : MarkerLabelFormatter {
//    private val numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH)
//
//    override fun getLabel(
//        markedEntries: List<Marker.EntryModel>,
//        chartValues: ChartValues,
//    ): CharSequence =
//        markedEntries.transformToSpannable(
//            prefix = if (markedEntries.size > 1) numberFormat.format(markedEntries.sumOf { it.entry.y }) + " (" else "",
//            postfix = if (markedEntries.size > 1) ")" else "",
//            separator = "; ",
//        ) { model ->
//            if (colorCode) {
//                appendCompat(
//                    numberFormat.format(model.entry.y),
//                    ForegroundColorSpan(model.color),
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
//                )
//            } else {
//                append(numberFormat.format(model.entry.y))
//            }
//        }
//
//    private val CartesianLayerModel.Entry.y
//        get() =
//            when (this) {
//                is ColumnCartesianLayerModel.Entry -> y
//                is LineCartesianLayerModel.Entry -> y
//                else -> throw IllegalArgumentException("Unexpected `CartesianLayerModel.Entry` implementation.")
//            }
//}
