package com.thekr.ui.settings.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.util.RtlView
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(Res.string.settings)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
    )
}


@Preview
@Composable
fun SettingsAppBarPreviewRtl() {
    RtlView {
        SettingsTopBar(
            canNavigateBack = true,
            onNavigateUp = {}
        )
    }
}

@Preview
@Composable
fun SettingsAppBarPreview() {
    AppTheme {
        Surface {
            SettingsTopBar(
                canNavigateBack = true,
                onNavigateUp = {}
            )
        }
    }
}


