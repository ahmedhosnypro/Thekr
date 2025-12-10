package com.thekr.ui.settings.language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thekr.data.settings.Option
import com.thekr.ui.component.DefaultHorizontalDivider
import com.thekr.ui.settings.component.PopupSetting
import com.thekr.ui.settings.component.RadioButtonSetting
import com.thekr.ui.settings.component.SettingLabel
import com.thekr.ui.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.all_languages
import com.thekr.resources.app_language
import com.thekr.resources.arabic
import com.thekr.resources.cancel
import com.thekr.resources.current_language
import com.thekr.resources.english
import com.thekr.resources.local_ar
import com.thekr.resources.local_en


@Composable
fun getLanguageOptions(): SnapshotStateList<Option> {
    return remember {
        mutableStateListOf(
            Option(
                textRes = Res.string.arabic,
                descriptionTextRes = Res.string.local_ar,
                value = "ar",
            ),
            Option(
                textRes = Res.string.english,
                descriptionTextRes = Res.string.local_en,
                value = "en"
            ),
        )
    }
}

@Composable
fun LanguageSettings(
    modifier: Modifier = Modifier,
    selectedLanguage: () -> String = { "ar" },
    onLanguageChange: (Option) -> Unit = {},
) {
    val visible = rememberSaveable {
        mutableStateOf(false)
    }

    PopupSetting(
        title = stringResource(Res.string.app_language),
        summary = getLanguageOptions().firstOrNull { it.value == selectedLanguage() }?.let {
            stringResource(it.textRes)
        },
        onClick = {
            visible.value = true
        },
        modifier = modifier
    ) {
        MenuSetting(
            title = stringResource(Res.string.app_language),
            options = getLanguageOptions().map {
                it.copy(selected = it.value == selectedLanguage())
            }.toMutableStateList(),
            onOptionSelected = onLanguageChange,
            visible = visible,
        )
    }
}

@Composable
fun MenuSetting(
    title: String,
    options: SnapshotStateList<Option>,
    selectedOption: MutableState<Option>? = null,
    onOptionSelected: (Option) -> Unit,
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

                    if (selectedOption != null) {
                        // current language
                        // label
                        SettingLabel(
                            stringResource(Res.string.current_language),
                            horizontalPadding
                        )
                        val option = selectedOption.value
                        RadioButtonSetting(
                            selected = true,
                            text = stringResource(option.textRes),
                            description = option.descriptionTextRes?.let { stringResource(it) },
                            onClick = { visible.value = false }
                        )
                    }

                    SettingLabel(
                        label = stringResource(Res.string.all_languages),
                        horizontalPadding = horizontalPadding
                    )
                    // all languages (exclude current language)
                    LazyColumn {
                        items(options.filter { !it.selected }) {
                            RadioButtonSetting(
                                selected = false,
                                text = stringResource(it.textRes),
                                description = it.descriptionTextRes?.let { it1 -> stringResource(it1) },
                                onClick = { onOptionSelected(it) }
                            )
                        }
                    }
                }
            }
        )
    }
}


@Preview
@Composable
fun DropDownListSettingPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppTheme {
            Surface {
                MenuSetting(
                    title = stringResource(Res.string.app_language),
                    options = getLanguageOptions(),
                    selectedOption = mutableStateOf(getLanguageOptions()[0]),
                    onOptionSelected = {},
                    visible = remember {
                        mutableStateOf(true)
                    }
                )
            }
        }
    }
}
