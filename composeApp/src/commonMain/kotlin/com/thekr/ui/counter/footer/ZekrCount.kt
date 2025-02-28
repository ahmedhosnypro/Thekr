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
import com.thekr.data.thekr.instance.ThekrInstanceDetails
import com.thekr.data.thekr.thekr.ThekrDetails
import com.thekr.model.ThekrTargetStatus
import com.thekr.ui.component.AutoSizeText
import com.thekr.ui.counter.CounterHelper
import com.thekr.values.Dimensions.medium
import com.thekr.values.Dimensions.normal
import com.thekr.values.Dimensions.xxLarge
import com.thekr.ui.home.list.ProgressState
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.theme.AppColors
import com.thekr.ui.component.LocalizedApp
import org.jetbrains.compose.ui.tooling.preview.Preview


const val strokeWidth = 8

@Composable
fun ThekrCount(
    settingsDetails: SettingsDetails,
    tabIndex: Int,
    modifier: Modifier = Modifier,
) {
    val count = CounterHelper.getThekrCount(tabIndex)
    val thekrInstanceDetails = CounterHelper.getThekrInstance(tabIndex)
    val thekrDetails = CounterHelper.getThekr(tabIndex)
    DetailedThekrCount(
        thekrCount = count,
        thekrInstanceDetailsMutableState = thekrInstanceDetails,
        thekrDetails = thekrDetails,
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

        val thekrInstance = CounterHelper.getThekrInstance(tabIndex).value
        val thekr = CounterHelper.getThekr(tabIndex).value
        val count = CounterHelper.getThekrCount(tabIndex).value.dailyCount
        val target = thekrInstance.dailyTarget
        if (thekrInstance.dailyTargetStatus == ThekrTargetStatus.Enabled && thekrInstance.dailyTarget > 0) {
            ProgressIndicator(
                target,
                count,
                settingsDetails,
                thekr,
                circleWidthDp
            )

        }
        ProgressText(count, thekrInstance, target, maxWidth, circleWidthDp)
    }
}

@Composable
private fun ProgressText(
    count: Long,
    thekrInstance: ThekrInstanceDetails,
    target: Long,
    maxWidth: Dp,
    circleWidthDp: MutableState<Dp>
) {
    AutoSizeText(
        text = count.toString(),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .width(
                if (thekrInstance.dailyTargetStatus == ThekrTargetStatus.Enabled && target > 0)
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
    thekr: ThekrDetails,
    circleWidthDp: MutableState<Dp>,
) {
    var progressTarget by rememberSaveable {
        mutableFloatStateOf(0f)
    }
    val progressState = progressState(target, count)
    val colors = AppTheme.colors(settingsDetails)
    val progressColor = progressColor(colors, progressState)
    val progressAnimate = animateFloatAsState(
        targetValue = progressTarget, animationSpec = tween(
            durationMillis = thekr.coolDown.toInt(),
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
    colors: AppColors, progressState: ProgressState
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
fun ThekrCountPreview() {
    AppTheme(themeMode = ThemeMode.Dark) {
        Surface(
            color = AppTheme.colors(
                SettingsDetails(
                    themeMode = ThemeMode.Dark
                )
            ).sheetBackgroundColor,
            contentColor = Color.White
        ) {
            LocalizedApp {
                with(CounterHelper) {
                    getThekrCount = {
                        mutableStateOf(
                            com.thekr.data.thekr.count.ThekrCount(
                                thekrInstanceId = 1,
                                dailyCount = 22,
                            )
                        )
                    }
                    getThekr = {
                        mutableStateOf(
                            ThekrDetails(
                                id = 1,
                                text = "سبحان الله وبحمده سبحان الله العظيم",
                                editable = true,
                                soundFileName = "sound.mp3",
                            )
                        )
                    }
                    getThekrInstance = {
                        mutableStateOf(
                            ThekrInstanceDetails(
                                id = 1,
                                thekrId = 1,
                                categoryId = 1,
                                dailyTarget = 100,
                                dailyTargetStatus = ThekrTargetStatus.Enabled,
                            )
                        )
                    }
                }
                ThekrCount(
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