package com.thekr.ui.counter.footer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.zekr.instance.ZekrInstanceDetails
import com.thekr.data.zekr.zekr.ZekrDetails
import com.thekr.model.ZekrTargetStatus
import com.thekr.ui.component.AutoSizeText
import com.thekr.ui.counter.CounterHelper
import com.thekr.ui.values.Dimensions.medium
import com.thekr.ui.values.Dimensions.normal
import com.thekr.ui.values.Dimensions.xxLarge
import com.thekr.ui.home.list.ProgressState
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.theme.ZekrColors
import com.thekr.ui.theme.ZekrTheme
import com.thekr.ui.util.RtlView
import org.jetbrains.compose.ui.tooling.preview.Preview


const val strokeWidth = 8

@Composable
fun ZekrCount(
    settingsDetails: SettingsDetails,
    tabIndex: Int,
    modifier: Modifier = Modifier,
) {
    val count = CounterHelper.getZekrCount(tabIndex)
    val zekrInstanceDetails = CounterHelper.getZekrInstance(tabIndex)
    val zekrDetails = CounterHelper.getZekr(tabIndex)
    DetailedZekrCount(
        zekrCount = count,
        zekrInstanceDetailsMutableState = zekrInstanceDetails,
        zekrDetails = zekrDetails,
        settingsDetails = settingsDetails,
        modifier = modifier
    )
}

@Composable
fun CircularCount(
    settingsDetails: SettingsDetails,
    tabIndex: Int,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = medium),
        contentAlignment = Alignment.Center
    ) {
        val maxWidth = maxWidth
        // Create element height in dp state
        val circleWidthDp = remember {
            mutableStateOf(0.dp)
        }

        val zekrInstance = CounterHelper.getZekrInstance(tabIndex).value
        val zekr = CounterHelper.getZekr(tabIndex).value
        val count = CounterHelper.getZekrCount(tabIndex).value.dailyCount
        val target = zekrInstance.dailyTarget
        if (zekrInstance.dailyTargetStatus == ZekrTargetStatus.Enabled && zekrInstance.dailyTarget > 0) {
            ProgressIndicator(
                target,
                count,
                settingsDetails,
                zekr,
                circleWidthDp
            )

        }
        ProgressText(count, zekrInstance, target, maxWidth, circleWidthDp)
    }
}

@Composable
private fun ProgressText(
    count: Long,
    zekrInstance: ZekrInstanceDetails,
    target: Long,
    maxWidth: Dp,
    circleWidthDp: MutableState<Dp>
) {
    AutoSizeText(
        text = count.toString(),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .width(
                if (zekrInstance.dailyTargetStatus == ZekrTargetStatus.Enabled && target > 0)
                    (maxWidth - (maxWidth - circleWidthDp.value) -
                            (2 * strokeWidth).dp - (circleWidthDp.value / 4))
                        .coerceAtMost(
                            circleWidthDp.value
                        )
                else maxWidth - xxLarge * 2
            )
            .padding(
                horizontal = normal,
            ),
    )
}

@Composable
private fun ProgressIndicator(
    target: Long,
    count: Long,
    settingsDetails: SettingsDetails,
    zekr: ZekrDetails,
    circleWidthDp: MutableState<Dp>,
) {
    var progressTarget by rememberSaveable {
        mutableFloatStateOf(0f)
    }
    val progressState = progressState(target, count)
    val colors = ZekrTheme.colors(settingsDetails)
    val progressColor = progressColor(colors, progressState)
    val progressAnimate = animateFloatAsState(
        targetValue = progressTarget, animationSpec = tween(
            durationMillis = zekr.coolDown.toInt(),
        ), label = "progressAnimate"
    )
    LaunchedEffect(count, target) {
        val newVal = count / target.toFloat()
        if (newVal >= progressTarget) {
            progressTarget = newVal
        }
    }
    val localDensity = LocalDensity.current
    CircularProgressIndicator(
        progress = { progressAnimate.value },
        modifier = Modifier
            .aspectRatio(1f)
            .onGloballyPositioned { coordinates ->
                circleWidthDp.value = with(localDensity) {
                    (coordinates.size.width).toDp()
                }
            }
            .background(
                color = colors.progressBackgroundColor,
                shape = CircleShape
            ),
        color = progressColor,
        strokeWidth = strokeWidth.dp,
        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
        strokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap,
    )
}

@Composable
fun progressColor(
    colors: ZekrColors, progressState: ProgressState
): Color {
    val progressColor = when (progressState) {
        ProgressState.EQUAL, ProgressState.BIGGER -> colors.successColor
        ProgressState.SMALLER -> colors.progressColor
        else -> MaterialTheme.colorScheme.onSurface
    }
    return progressColor
}

@Composable
fun progressState(
    target: Long, count: Long
): ProgressState {
    val progressState = when {
        target == 0L -> ProgressState.TargetIsZero
        count == 0L -> ProgressState.ScoreIsZero
        count == target -> ProgressState.EQUAL
        count > target -> ProgressState.BIGGER
        else -> ProgressState.SMALLER
    }
    return progressState
}

@Preview
@Composable
fun ZekrCountPreview() {
    AppTheme(themeMode = ThemeMode.Dark) {
        Surface(
            color = ZekrTheme.colors(
                SettingsDetails(
                    themeMode = ThemeMode.Dark
                )
            ).sheetBackgroundColor,
            contentColor = Color.White
        ) {
            RtlView {
                with(CounterHelper) {
                    getZekrCount = {
                        mutableStateOf(
                            com.thekr.data.zekr.count.ZekrCount(
                                zekrInstanceId = 1,
                                dailyCount = 22,
                            )
                        )
                    }
                    getZekr = {
                        mutableStateOf(
                            ZekrDetails(
                                id = 1,
                                text = "سبحان الله وبحمده سبحان الله العظيم",
                                editable = true,
                                soundFileName = "sound.mp3",
                            )
                        )
                    }
                    getZekrInstance = {
                        mutableStateOf(
                            ZekrInstanceDetails(
                                id = 1,
                                zekrId = 1,
                                categoryId = 1,
                                dailyTarget = 100,
                                dailyTargetStatus = ZekrTargetStatus.Enabled,
                            )
                        )
                    }
                }
                ZekrCount(
                    settingsDetails = SettingsDetails(
                        themeMode = ThemeMode.Dark,
                        showCount = true
                    ),
                    tabIndex = 0
                )
            }
        }
    }
}