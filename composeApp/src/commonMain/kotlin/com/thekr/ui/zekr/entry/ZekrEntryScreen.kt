package com.thekr.ui.thekr.entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.thekr.thekr.ThekrEntry
import com.thekr.data.thekr.thekr.ThekrEntryUiState
import com.thekr.model.ThekrTargetStatus
import com.thekr.ui.component.bar.AppTopBar
import com.thekr.ui.component.bar.HeaderText
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.component.LocalizedApp
import com.thekr.values.Dimensions.large
import com.thekr.values.Dimensions.medium
import com.thekr.values.Dimensions.normal
import com.thekr.values.Dimensions.small
import com.thekr.ui.viewmodel.AppViewModelProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.add_thekr
import com.thekr.resources.coolDown
import com.thekr.resources.daily_goal
import com.thekr.resources.monthly_goal
import com.thekr.resources.save
import com.thekr.resources.weekly_goal
import com.thekr.resources.yearly_goal
import com.thekr.resources.thekr_content

//todo:val c = LocalSoftwareKeyboardController.current
@Composable
fun CounterEntryScreen(
    settingsDetails: SettingsDetails,
    viewModel: ThekrEntryViewModel = viewModel {
        AppViewModelProvider.Factory.create(ThekrEntryViewModel::class, this)
    },
) {
    val uiState by viewModel.viewState.collectAsState()
    LaunchedEffect(Unit) {
        ThekrEntryActions.initActions(viewModel)
    }
    CounterEntry(
        uiState = uiState,
        settingsDetails = settingsDetails,
    )
}

@Composable
fun CounterEntry(
    uiState: ThekrEntryUiState,
    settingsDetails: SettingsDetails = SettingsDetails(),
) {
    val thekrColors = AppTheme.colors(settingsDetails)
    Scaffold(topBar = {
        AppTopBar(
            title = {
                HeaderText(
                    text = stringResource(Res.string.add_thekr),
                )
            },
            navigationIcon = {
                IconButton(onClick = { NavigationActions.navigateUp() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Back",
                    )
                }
            },
            actions = {
                Button(
                    onClick = {
                        ThekrEntryActions.onSaveClick()
                    }, enabled = uiState.isEntryValid, colors = ButtonDefaults.textButtonColors(
                        contentColor = thekrColors.onMainHeader,
                        disabledContentColor = thekrColors.onMainHeaderDisabled,
                    )
                ) {
                    Text(stringResource(Res.string.save))
                }
            },
            settingsDetails = settingsDetails,
        )
    }) { innerPadding ->
        CounterEntryBody(uiState, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
private fun CounterEntryBody(
    uiState: ThekrEntryUiState,
    modifier: Modifier = Modifier,
) {
//    val keyboardController = LocalSoftwareKeyboardController.current

    val scrollState = rememberScrollState()


    Column(
        modifier = modifier
            .imePadding()
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(
                start = normal,
                end = normal,
                top = small
            ), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val counterEntry = uiState.thekrEntry

        MyTextField(
            stringResource(Res.string.thekr_content),
            counterEntry.text,
            uiState.isLabelValid,
            { ThekrEntryActions.onLabelChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = medium),
        )

        ThekrGoals(
            thekrEntry = uiState.thekrEntry,
        )


        MyTextField(
            stringResource(Res.string.coolDown),
            counterEntry.coolDown.toString(),
            uiState.isLabelValid,
            { ThekrEntryActions.onCoolDownChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = medium)

        )
    }
}

@Composable
fun MyTextField(
    label: String,
    value: String,
    isValid: Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        disabledContainerColor = MaterialTheme.colorScheme.surface,
    ),
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { text: String -> onValueChange(text) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        label = { Text(if (isValid) label else "$label*") },
        colors = colors,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        isError = !isValid,
    )
}


@Composable
fun ThekrGoals(
    thekrEntry: ThekrEntry,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = large,
            ), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // yearly goal
        ThekrGoalItem(
            label = stringResource(Res.string.yearly_goal),
            value = thekrEntry.yearlyTarget.toString(),
            isValid = true,
            enabled = thekrEntry.yearlyTargetStatus == ThekrTargetStatus.Enabled,
            onValueChange = { ThekrEntryActions.onYearlyGoalChange(it) },
            onCheckedChange = {
                ThekrEntryActions.updateThekrEntry(
                    thekrEntry.copy(
                        yearlyTargetStatus = if (it) ThekrTargetStatus.Enabled else ThekrTargetStatus.Disabled
                    )
                )
            },
        )

        // monthly goal
        ThekrGoalItem(
            label = stringResource(Res.string.monthly_goal),
            value = thekrEntry.monthlyTarget.toString(),
            isValid = true,
            enabled = thekrEntry.monthlyTargetStatus == ThekrTargetStatus.Enabled,
            onValueChange = { ThekrEntryActions.onMonthlyGoalChange(it) },
            onCheckedChange = {
                ThekrEntryActions.updateThekrEntry(
                    thekrEntry.copy(
                        monthlyTargetStatus = if (it) ThekrTargetStatus.Enabled else ThekrTargetStatus.Disabled
                    )
                )
            },
        )

        // weekly goal
        ThekrGoalItem(
            label = stringResource(Res.string.weekly_goal),
            value = thekrEntry.weeklyTarget.toString(),
            isValid = true,
            enabled = thekrEntry.weeklyTargetStatus == ThekrTargetStatus.Enabled,
            onValueChange = { ThekrEntryActions.onWeeklyGoalChange(it) },
            onCheckedChange = {
                ThekrEntryActions.updateThekrEntry(
                    thekrEntry.copy(
                        weeklyTargetStatus = if (it) ThekrTargetStatus.Enabled else ThekrTargetStatus.Disabled
                    )
                )
            },
        )

        // daily goal
        ThekrGoalItem(
            label = stringResource(Res.string.daily_goal),
            value = thekrEntry.dailyTarget.toString(),
            isValid = true,
            enabled = thekrEntry.dailyTargetStatus == ThekrTargetStatus.Enabled,
            onValueChange = { ThekrEntryActions.onDailyGoalChange(it) },
            onCheckedChange = {
                ThekrEntryActions.updateThekrEntry(
                    thekrEntry.copy(
                        dailyTargetStatus = if (it) ThekrTargetStatus.Enabled else ThekrTargetStatus.Disabled
                    )
                )
            },
            keyboardType = KeyboardType.Number,
        )
    }
}


@Composable
fun ThekrGoalItem(
    label: String,
    value: String,
    isValid: Boolean,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    keyboardType: KeyboardType = KeyboardType.Number,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                end = normal,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = enabled, onCheckedChange = onCheckedChange
        )
        OutlinedTextField(
            value = value,
            onValueChange = { text: String -> onValueChange(text) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            label = { Text(if (isValid) label else "$label*") },
            colors = colors,
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            singleLine = true,
            isError = !isValid,
            enabled = enabled,
        )
    }
}

@Preview
@Composable
private fun CounterEntryScreenPreview() {
    AppTheme {
        Surface {
            LocalizedApp {
                CounterEntry(
                    uiState = ThekrEntryUiState(),
                )
            }
        }
    }
}