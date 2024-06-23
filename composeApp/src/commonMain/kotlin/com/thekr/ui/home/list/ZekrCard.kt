package com.thekr.ui.home.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.theme.ZekrColors
import com.thekr.ui.theme.ZekrTheme
import com.thekr.ui.theme.droidKufi
import com.thekr.ui.theme.hacenTunisia
import com.thekr.ui.util.NoRippleInteractionSource
import com.thekr.ui.component.RtlView
import com.thekr.ui.values.Dimensions.small
import com.thekr.ui.values.Dimensions.tiny
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.zekr_indicator

/**
 * Displays a card representing a Zekr item.
 *
 * @param settingsDetails The settings detail for theming and
 *     customization.
 * @param modifier Modifier to be applied to the card layout.
 * @param text The text of the Zekr.
 * @param count The current count for the Zekr.
 * @param target The target count for the Zekr.
 * @param onItemClick Callback function invoked when the card is clicked.
 * @param onLongCLick Callback function invoked when the card is
 *     long-clicked.
 * @param leadingIcon An optional composable function to display a leading
 *     icon.
 * @param trailingIcon An optional composable function to display a
 *     trailing icon.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ZekrCard(
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
    text: String = "",
    count: Long = 0,
    target: Long = 0,
    onItemClick: () -> Unit = {},
    onLongCLick: () -> Unit = {},
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val zekrColors = ZekrTheme.colors(settingsDetails)

    Card(
        onClick = onItemClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = small)
            .combinedClickable(
                onClick = onItemClick,
                onLongClick = onLongCLick,
            )
            .padding(top = small),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = CardDefaults.shape,
        interactionSource = NoRippleInteractionSource(),
    ) {
        ZekrCardContent(
            text = text,
            count = count,
            target = target,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            zekrColors = zekrColors
        )
    }
}

/**
 * Displays the content of a ZekrCard.
 *
 * @param text The text of the Zekr.
 * @param count The current count for the Zekr.
 * @param target The target count for the Zekr.
 * @param leadingIcon An optional composable function to display a leading
 *     icon.
 * @param trailingIcon An optional composable function to display a
 *     trailing icon.
 * @param zekrColors The color palette for the Zekr card.
 */
@Composable
private fun ZekrCardContent(
    text: String,
    count: Long,
    target: Long,
    leadingIcon: (@Composable () -> Unit)?,
    trailingIcon: (@Composable () -> Unit)?,
    zekrColors: ZekrColors
) {
    Column(
        modifier = Modifier.requiredHeight(56.dp),
        verticalArrangement = Arrangement.spacedBy(tiny, Alignment.CenterVertically),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            horizontalArrangement = Arrangement.spacedBy(small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ZekrTextAndIcon(
                text = text,
                leadingIcon = leadingIcon,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            DailyCount(
                count = count,
                target = target,
                successColor = zekrColors.successColor,
                contentColor = MaterialTheme.colorScheme.onSurface,
                progressColor = zekrColors.progressColor,
                modifier = Modifier.fillMaxWidth(0.15f)
            )

            if (trailingIcon != null) {
                trailingIcon()
            } else {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = null,
                    tint = zekrColors.cardCallToActionIcon,
                )
            }
        }

        ShowProgress(
            score = count,
            targetScore = target,
            zekrColors = zekrColors,
        )
    }
}

/**
 * Displays the Zekr text and its leading icon.
 *
 * @param text The text of the Zekr.
 * @param leadingIcon An optional composable function to display a leading
 *     icon.
 * @param contentColor The color of the Zekr text.
 */
@Composable
private fun ZekrTextAndIcon(
    text: String,
    leadingIcon: (@Composable () -> Unit)?,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(small),
        verticalAlignment = Alignment.Top,
        modifier = modifier.fillMaxHeight(),
    ) {
        leadingIcon?.let {
            Box(modifier = Modifier.padding(top = tiny)) { it() }
        }
        Text(
            text = text,
            color = contentColor,
            fontFamily = droidKufi(),
            fontSize = 12.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        )
    }
}

/**
 * Displays the daily count and target for a Zekr.
 *
 * @param count The current count.
 * @param target The target count.
 * @param successColor The color to use when the count equals or exceeds
 *     the target.
 * @param contentColor The default content color.
 * @param progressColor The color to use when the count is less than the
 *     target.
 * @param modifier Modifier to be applied to the Text composable.
 */
@Composable
private fun DailyCount(
    count: Long,
    target: Long,
    successColor: Color,
    contentColor: Color,
    progressColor: Color,
    modifier: Modifier = Modifier,
) {
    val progressState = when {
        target == 0L -> ProgressState.TargetIsZero
        count == 0L -> ProgressState.ScoreIsZero
        count == target -> ProgressState.EQUAL
        count > target -> ProgressState.BIGGER
        else -> ProgressState.SMALLER
    }

    val text = if (progressState == ProgressState.TargetIsZero) "" else "$count/$target"

    val color = when (progressState) {
        ProgressState.EQUAL, ProgressState.BIGGER -> successColor
        ProgressState.ScoreIsZero -> contentColor
        ProgressState.SMALLER -> progressColor
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Text(
        text = text,
        color = color,
        fontFamily = hacenTunisia(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.End,
        modifier = modifier
    )
}


@Preview
@Composable
fun CounterCardPreviewDarkMode() {
    RtlView {
        AppTheme(ThemeMode.Dark) {
            Surface {
                ZekrCard(
                    text = "سبحان الله",
                    count = 50,
                    target = 100,
                    leadingIcon = {
                        Image(
                            painterResource(Res.drawable.zekr_indicator),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.height(12.dp),
                        )
                    },
                    settingsDetails = SettingsDetails(
                        themeMode = ThemeMode.Dark,
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
fun CounterCardPreview() {
    AppTheme {
        Surface {
            RtlView {
                ZekrCard(
                    text = "سبحان الله وبحمده سبحان الله العظيم سبحان الله وبحمده سبحان الله العظيم",
                    count = 50,
                    target = 100,
                    leadingIcon = {
                        Image(
                            painterResource(Res.drawable.zekr_indicator),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.height(12.dp),
                        )
                    },
                    settingsDetails = SettingsDetails(
                        themeMode = ThemeMode.Light,
                    ),
                )
            }
        }
    }
}

