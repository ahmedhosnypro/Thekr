package com.thekr.ui.home.header

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thekr.data.settings.SettingsDetails
import com.thekr.ui.theme.AppTheme
import com.thekr.values.Dimensions.medium

@Composable
fun TopBarHeaderControls(
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val thekrColors = AppTheme.colors(settingsDetails)
    CardDefaults
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = medium, end = medium)
            .border(
                color = thekrColors.secondaryHeaderBorder,
                width = 2.dp,
                shape = MaterialTheme.shapes.small
            ), colors = CardDefaults.cardColors(
            containerColor = thekrColors.secondaryHeaderBackground,
            contentColor = thekrColors.onSecondaryHeader
        ),
        shape = MaterialTheme.shapes.small
    ) {
        content()
    }
}

@Composable
fun HeaderControlCard(
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val thekrColors = AppTheme.colors(settingsDetails)
    Card(
        modifier = modifier
            .border(
                color = thekrColors.secondaryHeaderBorder,
                width = 2.dp,
                shape = MaterialTheme.shapes.small
            ), colors = CardDefaults.cardColors(
            containerColor = thekrColors.secondaryHeaderBackground,
            contentColor = thekrColors.onSecondaryHeader
        ),
        shape = MaterialTheme.shapes.small
    ) {
        content()
    }
}
