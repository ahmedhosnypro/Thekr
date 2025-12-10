package com.thekr.ui.home.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import com.thekr.ui.theme.AppColors
import androidx.compose.ui.tooling.preview.Preview


enum class ProgressState {
    EQUAL, SMALLER, BIGGER, TargetIsZero, ScoreIsZero
}

@Composable
fun ShowProgress(
    score: Long = 100,
    targetScore: Long = 100,
    appColors: AppColors = AppColors()
) {
    val progressState = when {
        targetScore == 0L -> ProgressState.TargetIsZero
        score == targetScore -> ProgressState.EQUAL
        score > targetScore -> ProgressState.BIGGER
        else -> ProgressState.SMALLER
    }


    val color = when (progressState) {
        ProgressState.EQUAL, ProgressState.BIGGER -> appColors.successColor
        ProgressState.SMALLER -> appColors.progressColor
        else -> appColors.listDivider
    }

    val progressFactor by remember(score) {
        mutableFloatStateOf(
            when (progressState) {
                ProgressState.TargetIsZero -> 0f
                ProgressState.SMALLER -> score.toFloat() / targetScore.toFloat()
                else -> 1f
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RectangleShape
            ), contentAlignment = Alignment.Center
    ) {

        LinearProgressIndicator(
            progress = { progressFactor },
            modifier = Modifier.fillMaxWidth(),
            color = color,
            trackColor = appColors.listDivider,
        )
    }
}


@Preview
@Composable
fun ShowProgress30() {
    ShowProgress(
        score = 30,
        targetScore = 100,
    )
}

@Preview
@Composable
fun ShowProgressScoreSmallerThanTarget() {
    ShowProgress(
        score = 50,
        targetScore = 100,
    )
}

@Preview
@Composable
fun ShowProgressScoreEqualsTarget() {
    ShowProgress(
        score = 100,
        targetScore = 100,
    )
}

@Preview
@Composable
fun ShowProgressScoreBiggerThanTarget() {
    ShowProgress(
        score = 600,
        targetScore = 100,
    )
}

@Preview
@Composable
fun ShowProgressTargetIsZero() {
    ShowProgress(
        score = 200,
        targetScore = 0,
    )
}