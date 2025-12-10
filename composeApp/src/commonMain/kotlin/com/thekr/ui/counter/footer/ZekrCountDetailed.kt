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
import com.thekr.data.thekr.count.ThekrCount
import com.thekr.data.thekr.instance.ThekrInstanceDetails
import com.thekr.data.thekr.thekr.ThekrDetails
import com.thekr.model.ThekrTargetStatus
import com.thekr.ui.component.AutoSizeText
import com.thekr.values.Dimensions.normal
import com.thekr.values.Dimensions.small
import com.thekr.values.Dimensions.tiny
import com.thekr.ui.home.list.thekrInstanceList
import com.thekr.ui.home.list.thekrPreviewList
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.theme.AppColors
import com.thekr.ui.component.LocalizedApp
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.daily
import com.thekr.resources.monthly
import com.thekr.resources.total
import com.thekr.resources.weekly
import com.thekr.resources.yearly

/**
 * Displays detailed Thekr count information.
 *
 * @param thekrCount The state of the Thekr count data.
 * @param thekrInstanceDetailsMutableState The state of the Thekr instance
 *     details.
 * @param thekrDetails The state of the Thekr details.
 * @param settingsDetails Settings details for theming and customization.
 * @param modifier Modifier to be applied to the layout.
 */
@Composable
fun DetailedThekrCount(
    thekrCount: MutableState<ThekrCount>,
    thekrInstanceDetailsMutableState: MutableState<ThekrInstanceDetails>,
    thekrDetails: MutableState<ThekrDetails>,
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier
) {
    val count = thekrCount.value
    val thekrInstanceDetails = thekrInstanceDetailsMutableState.value
    val colors = AppTheme.colors(settingsDetails)

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
                target = thekrInstanceDetails.dailyTarget,
                targetStatus = thekrInstanceDetails.dailyTargetStatus,
                thekrDetails = thekrDetails,
                colors = colors
            )
        }

        if (settingsDetails.showWeeklyCount) {
            CountItem(
                count = count.weeklyCount,
                countLabel = stringResource(Res.string.weekly),
                target = thekrInstanceDetails.weeklyTarget,
                targetStatus = thekrInstanceDetails.weeklyTargetStatus,
                thekrDetails = thekrDetails,
                colors = colors
            )
        }

        if (settingsDetails.showMonthlyCount) {
            CountItem(
                count = count.monthlyCount,
                countLabel = stringResource(Res.string.monthly),
                target = thekrInstanceDetails.monthlyTarget,
                targetStatus = thekrInstanceDetails.monthlyTargetStatus,
                thekrDetails = thekrDetails,
                colors = colors
            )
        }

        if (settingsDetails.showYearlyCount) {
            CountItem(
                count = count.yearlyCount,
                countLabel = stringResource(Res.string.yearly),
                target = thekrInstanceDetails.yearlyTarget,
                targetStatus = thekrInstanceDetails.yearlyTargetStatus,
                thekrDetails = thekrDetails,
                colors = colors
            )
        }

        if (settingsDetails.showTotalCount) {
            TotalCount(thekrCount = thekrCount, colors = colors)
        }
    }
}

/**
 * Displays the total count for the Thekr.
 *
 * @param thekrCount The state of the Thekr count data.
 * @param colors The color palette for the Thekr theme.
 */
@Composable
fun TotalCount(
    thekrCount: MutableState<ThekrCount>,
    colors: AppColors
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
            text = "${thekrCount.value.totalCount}",
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
 * @param thekrDetails The state of the Thekr details.
 * @param colors The color palette for the Thekr theme.
 * @param modifier Modifier to be applied to the layout.
 */
@Composable
fun CountItem(
    count: Long,
    countLabel: String,
    target: Long,
    targetStatus: ThekrTargetStatus,
    thekrDetails: MutableState<ThekrDetails>,
    colors: AppColors,
    modifier: Modifier = Modifier,
) {
    if (targetStatus == ThekrTargetStatus.Enabled) {
        var progressTarget by rememberSaveable { mutableFloatStateOf(0f) }
        val progressState = progressState(target, count)
        val progressColor = progressColor(colors, progressState)

        val progressAnimate = animateFloatAsState(
            targetValue = progressTarget,
            animationSpec = tween(durationMillis = thekrDetails.value.coolDown.toInt()),
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
 * @param colors The color palette for the Thekr theme.
 * @param progressColor The color for the progress bar and current count
 *     text.
 */
@Composable
private fun CountValues(
    count: Long,
    target: Long,
    colors: AppColors,
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
            LocalizedApp {
                CountItem(
                    count = 50,
                    countLabel = "العدد",
                    target = 100,
                    targetStatus = ThekrTargetStatus.Enabled,
                    thekrDetails = thekrPreviewList()[0],
                    colors = AppTheme.colors(SettingsDetails())
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
            LocalizedApp {
                CountItem(
                    count = 100,
                    countLabel = "العدد",
                    target = 100,
                    targetStatus = ThekrTargetStatus.Enabled,
                    thekrDetails = thekrPreviewList()[0],
                    colors = AppTheme.colors(SettingsDetails(themeMode = ThemeMode.Dark))
                )
            }
        }
    }
}

@Preview
@Composable
fun DetailedThekrCountPreviewDark() {
    LocalizedApp {
        Column {
            AppTheme(themeMode = ThemeMode.Dark) {
                Surface {
                    DetailedThekrCountPreview(
                        settingsDetails = SettingsDetails(
                            themeMode = ThemeMode.Dark
                        )
                    )
                }
                AppTheme(themeMode = ThemeMode.Light) {
                    Surface {
                        DetailedThekrCountPreview(
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
fun DetailedThekrCountPreview(
    settingsDetails: SettingsDetails = SettingsDetails()
) {
    DetailedThekrCount(
        settingsDetails = settingsDetails,
        thekrCount = remember {
            mutableStateOf(
                ThekrCount(
                    dailyCount = 50,
                    weeklyCount = 100,
                    monthlyCount = 200,
                    yearlyCount = 300,
                    totalCount = 650
                )
            )
        },
        thekrInstanceDetailsMutableState = thekrInstanceList()[0],
        thekrDetails = thekrPreviewList()[0],
    )
}

