package com.thekr.ui.settings.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.ThemeModeDetails
import com.thekr.data.settings.ThemeModeOption
import com.thekr.ui.component.DefaultHorizontalDivider
import com.thekr.ui.settings.component.PopupSetting
import com.thekr.ui.settings.component.RadioButtonSetting
import org.jetbrains.compose.resources.stringResource
import thekr.composeapp.generated.resources.Res
import thekr.composeapp.generated.resources.cancel
import thekr.composeapp.generated.resources.theme

@Composable
fun ThemeModeSetting(
    modifier: Modifier = Modifier,
    selectedTheme: () -> ThemeMode = { ThemeMode.System },
    onThemeChange: (ThemeModeOption) -> Unit = {},
) {
    val visible = rememberSaveable {
        mutableStateOf(false)
    }

    val selectedThemeDetails = ThemeModeDetails.entries.first {
        it.name == selectedTheme().name
    }

    PopupSetting(
        title = stringResource(Res.string.theme),
        summary = stringResource(selectedThemeDetails.titleRes),
        onClick = {
            visible.value = true
        },
        modifier = modifier
    ) {
        ThemeModeMenuSetting(
            title = stringResource(Res.string.theme),
            options = ThemeModeDetails.entries.map {
                ThemeModeOption(
                    text = stringResource(it.titleRes),
                    value = ThemeMode.valueOf(it.name),
                    selected = it.name == selectedTheme().name
                )
            }.toMutableStateList(),
            onOptionSelected = onThemeChange,
            visible = visible,
        )
    }
}

@Composable
fun ThemeModeMenuSetting(
    title: String,
    options: SnapshotStateList<ThemeModeOption>,
    onOptionSelected: (ThemeModeOption) -> Unit,
    visible: MutableState<Boolean>,
) {
    if (visible.value) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth(.9f),
            onDismissRequest = { visible.value = false },
            confirmButton = { },
            dismissButton = {
                TextButton(
                    onClick = { visible.value = false },
                ) {
                    Text(text = stringResource(Res.string.cancel))
                }
            },
            text = {
                val horizontalPadding = 16.dp
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // header
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Default,
                        modifier = Modifier.padding(horizontal = horizontalPadding)
                    )
                    DefaultHorizontalDivider()
                    LazyColumn {
                        items(options) {
                            RadioButtonSetting(
                                selected = it.selected,
                                text = it.text,
                                description = it.description,
                                onClick = {
                                    onOptionSelected(it)
                                    visible.value = false
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}