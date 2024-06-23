package com.thekr.ui.counter.footer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.zekr.count.ZekrCount
import com.thekr.data.zekr.instance.ZekrInstanceDetails
import com.thekr.data.zekr.zekr.ZekrDetails
import com.thekr.model.ZekrTargetStatus
import com.thekr.ui.component.AutoSizeText
import com.thekr.ui.values.Dimensions.normal
import com.thekr.ui.values.Dimensions.small
import com.thekr.ui.values.Dimensions.tiny
import com.thekr.ui.home.list.zekrInstanceList
import com.thekr.ui.home.list.zekrPreviewList
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.theme.ZekrColors
import com.thekr.ui.theme.ZekrTheme
import com.thekr.ui.component.RtlView
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.daily
import com.thekr.resources.monthly
import com.thekr.resources.total
import com.thekr.resources.weekly
import com.thekr.resources.yearly

/**
 * Displays detailed Zekr count information.
 *
 * @param zekrCount The state of the Zekr count data.
 * @param zekrInstanceDetailsMutableState The state of the Zekr instance
 *     details.
 * @param zekrDetails The state of the Zekr details.
 * @param settingsDetails Settings details for theming and customization.
 * @param modifier Modifier to be applied to the layout.
 */
@Composable
fun DetailedZekrCount(
    zekrCount: MutableState<ZekrCount>,
    zekrInstanceDetailsMutableState: MutableState<ZekrInstanceDetails>,
    zekrDetails: MutableState<ZekrDetails>,
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier
) {
    val count = zekrCount.value
    val zekrInstanceDetails = zekrInstanceDetailsMutableState.value
    val colors = ZekrTheme.colors(settingsDetails)

    Column(
        modifier = modifier
            .padding(
                vertical = tiny,
                horizontal = normal
            ),
        verticalArrangement = Arrangement.spacedBy(tiny, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Display counts based on visibility settings
        if (settingsDetails.showDailyCount) {
            CountItem(
                count = count.dailyCount,
                countLabel = stringResource(Res.string.daily),
                target = zekrInstanceDetails.dailyTarget,
                targetStatus = zekrInstanceDetails.dailyTargetStatus,
                zekrDetails = zekrDetails,
                colors = colors
            )
        }

        if (settingsDetails.showWeeklyCount) {
            CountItem(
                count = count.weeklyCount,
                countLabel = stringResource(Res.string.weekly),
                target = zekrInstanceDetails.weeklyTarget,
                targetStatus = zekrInstanceDetails.weeklyTargetStatus,
                zekrDetails = zekrDetails,
                colors = colors
            )
        }

        if (settingsDetails.showMonthlyCount) {
            CountItem(
                count = count.monthlyCount,
                countLabel = stringResource(Res.string.monthly),
                target = zekrInstanceDetails.monthlyTarget,
                targetStatus = zekrInstanceDetails.monthlyTargetStatus,
                zekrDetails = zekrDetails,
                colors = colors
            )
        }

        if (settingsDetails.showYearlyCount) {
            CountItem(
                count = count.yearlyCount,
                countLabel = stringResource(Res.string.yearly),
                target = zekrInstanceDetails.yearlyTarget,
                targetStatus = zekrInstanceDetails.yearlyTargetStatus,
                zekrDetails = zekrDetails,
                colors = colors
            )
        }

        if (settingsDetails.showTotalCount) {
            TotalCount(zekrCount = zekrCount, colors = colors)
        }
    }
}

/**
 * Displays the total count for the Zekr.
 *
 * @param zekrCount The state of the Zekr count data.
 * @param colors The color palette for the Zekr theme.
 */
@Composable
fun TotalCount(
    zekrCount: MutableState<ZekrCount>,
    colors: ZekrColors
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = colors.secondaryHeaderBorder, shape = CircleShape)
            .padding(vertical = small, horizontal = normal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.total),
            color = colors.primary,
            fontSize = 12.sp,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "${zekrCount.value.totalCount}",
            color = colors.primary,
            fontSize = 12.sp,
        )
    }
}

/**
 * Displays a single count item with label, progress bar, and count/target
 * values.
 *
 * @param count The current count value.
 * @param countLabel The label for the count (e.g., "Daily", "Weekly").
 * @param target The target count value.
 * @param targetStatus The status of the target (Enabled or Disabled).
 * @param zekrDetails The state of the Zekr details.
 * @param colors The color palette for the Zekr theme.
 * @param modifier Modifier to be applied to the layout.
 */
@Composable
fun CountItem(
    count: Long,
    countLabel: String,
    target: Long,
    targetStatus: ZekrTargetStatus,
    zekrDetails: MutableState<ZekrDetails>,
    colors: ZekrColors,
    modifier: Modifier = Modifier,
) {
    if (targetStatus == ZekrTargetStatus.Enabled) {
        var progressTarget by rememberSaveable { mutableFloatStateOf(0f) }
        val progressState = progressState(target, count)
        val progressColor = progressColor(colors, progressState)

        val progressAnimate = animateFloatAsState(
            targetValue = progressTarget,
            animationSpec = tween(durationMillis = zekrDetails.value.coolDown.toInt()),
            label = "progressAnimate"
        )

        LaunchedEffect(count, target) {
            val newVal = count / target.toFloat()
            if (newVal >= progressTarget) {
                progressTarget = newVal
            }
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = colors.secondaryHeaderBorder, shape = CircleShape)
                .padding(vertical = small, horizontal = normal),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = countLabel,
                modifier = Modifier.fillMaxWidth(0.2f),
                color = colors.primary,
            )

            LinearProgressIndicator(
                modifier = Modifier
                    .requiredHeight(8.dp)
                    .weight(1f),
                progress = { progressAnimate.value },
                color = progressColor,
                trackColor = Color.Transparent,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )

            CountValues(count, target, colors, progressColor)
        }
    } // The else block was empty and unnecessary, so it's removed.
}

/**
 * Displays the current count and target values.
 *
 * @param count The current count value.
 * @param target The target count value.
 * @param colors The color palette for the Zekr theme.
 * @param progressColor The color for the progress bar and current count
 *     text.
 */
@Composable
private fun CountValues(
    count: Long,
    target: Long,
    colors: ZekrColors,
    progressColor: Color
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth(0.4f)) {
        val width = maxWidth / 2 - 8.sp.value.dp
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "$count",
                modifier = Modifier.requiredWidth(width),
                textAlign = TextAlign.Start,
                fontSize = 12.sp,
                color = progressColor,
                maxLines = 1
            )
            Text(text = "/", fontSize = 12.sp)
            AutoSizeText(
                text = "$target",
                modifier = Modifier.requiredWidth(width),
                textAlign = TextAlign.End,
                color = colors.successColor,
                maxLines = 1,
            )
        }
    }
}


@Preview
@Composable
fun CountPreview() {
    AppTheme {
        Surface {
            RtlView {
                CountItem(
                    count = 50,
                    countLabel = "العدد",
                    target = 100,
                    targetStatus = ZekrTargetStatus.Enabled,
                    zekrDetails = zekrPreviewList()[0],
                    colors = ZekrTheme.colors(SettingsDetails())
                )
            }
        }
    }
}

@Preview
@Composable
fun CountPreviewDark() {
    AppTheme(themeMode = ThemeMode.Dark) {
        Surface {
            RtlView {
                CountItem(
                    count = 100,
                    countLabel = "العدد",
                    target = 100,
                    targetStatus = ZekrTargetStatus.Enabled,
                    zekrDetails = zekrPreviewList()[0],
                    colors = ZekrTheme.colors(SettingsDetails(themeMode = ThemeMode.Dark))
                )
            }
        }
    }
}

@Preview
@Composable
fun DetailedZekrCountPreviewDark() {
    RtlView {
        Column {
            AppTheme(themeMode = ThemeMode.Dark) {
                Surface {
                    DetailedZekrCountPreview(
                        settingsDetails = SettingsDetails(
                            themeMode = ThemeMode.Dark
                        )
                    )
                }
                AppTheme(themeMode = ThemeMode.Light) {
                    Surface {
                        DetailedZekrCountPreview(
                            settingsDetails = SettingsDetails(
                                themeMode = ThemeMode.Light
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailedZekrCountPreview(
    settingsDetails: SettingsDetails = SettingsDetails()
) {
    DetailedZekrCount(
        settingsDetails = settingsDetails,
        zekrCount = remember {
            mutableStateOf(
                ZekrCount(
                    dailyCount = 50,
                    weeklyCount = 100,
                    monthlyCount = 200,
                    yearlyCount = 300,
                    totalCount = 650
                )
            )
        },
        zekrInstanceDetailsMutableState = zekrInstanceList()[0],
        zekrDetails = zekrPreviewList()[0],
    )
}

