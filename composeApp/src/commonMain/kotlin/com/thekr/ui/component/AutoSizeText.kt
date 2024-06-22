package com.thekr.ui.component

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import com.thekr.ui.modifier.drawWithContentIfReady

/**
 * Composable function that displays text with automatic font size
 * adjustment to prevent overflow.
 *
 * @param text The text to be displayed.
 * @param modifier Modifier for styling the composable.
 * @param color The color of the text.
 * @param fontSize The size of the text font.
 * @param fontStyle The style of the font, such as italic or normal.
 * @param fontWeight The weight of the font, such as bold or light.
 * @param fontFamily The font family used for the text.
 * @param letterSpacing The spacing between characters.
 * @param textDecoration The decorations to apply to the text, such as
 *     underline or strikethrough.
 * @param textAlign The alignment of the text within its bounds.
 * @param lineHeight Line height for the paragraph.
 * @param overflow How visual overflow should be handled.
 * @param softWrap Whether the text should break at soft line breaks.
 * @param maxLines An optional maximum number of lines for the text to
 *     span, wrapping if necessary.
 * @param minLines The minimum height in terms of the minimum number of
 *     visible lines.
 * @param style The style to be applied to the Text composable.
 * @param fontSizeMultiplier The multiplier to decrease the font size when
 *     the text overflows.
 */
@Composable
fun AutoSizeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    style: TextStyle = LocalTextStyle.current,
    fontSizeMultiplier: Float = 0.95f,
) {
    var fontSizeState by remember { mutableStateOf(fontSize) }
    var shouldDraw by remember { mutableStateOf(false) }

    val typography = MaterialTheme.typography

    Text(
        text = text,
        color = color,
        modifier = modifier.drawWithContentIfReady(shouldDraw),
        fontSize = fontSizeState,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                if (fontSizeState.isUnspecified) {
                    fontSizeState = typography.bodyMedium.fontSize
                }
                fontSizeState = (fontSizeState.value * fontSizeMultiplier).sp
            } else {
                shouldDraw = true
            }
        },
        style = style
    )
}