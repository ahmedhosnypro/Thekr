package com.thekr.ui.zekr.entry

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
import com.thekr.data.zekr.zekr.ZekrEntry
import com.thekr.data.zekr.zekr.ZekrEntryUiState
import com.thekr.model.ZekrTargetStatus
import com.thekr.ui.bar.top.ZekrBar
import com.thekr.ui.home.bar.top.HeaderText
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.theme.ZekrTheme
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.values.Dimensions.large
import com.thekr.ui.values.Dimensions.medium
import com.thekr.ui.values.Dimensions.normal
import com.thekr.ui.values.Dimensions.small
import com.thekr.ui.viewmodel.AppViewModelProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.add_zekr
import com.thekr.resources.coolDown
import com.thekr.resources.daily_goal
import com.thekr.resources.monthly_goal
import com.thekr.resources.save
import com.thekr.resources.weekly_goal
import com.thekr.resources.yearly_goal
import com.thekr.resources.zekr_content

//todo:val c = LocalSoftwareKeyboardController.current
@Composable
fun CounterEntryScreen(
    settingsDetails: SettingsDetails,
    viewModel: ZekrEntryViewModel = viewModel {
        AppViewModelProvider.Factory.create(ZekrEntryViewModel::class, this)
    },
) {
    val uiState by viewModel.viewState.collectAsState()
    LaunchedEffect(Unit) {
        ZekrEntryActions.initActions(viewModel)
    }
    CounterEntry(
        uiState = uiState,
        settingsDetails = settingsDetails,
    )
}

@Composable
fun CounterEntry(
    uiState: ZekrEntryUiState,
    settingsDetails: SettingsDetails = SettingsDetails(),
) {
    val zekrColors = ZekrTheme.colors(settingsDetails)
    Scaffold(topBar = {
        ZekrBar(
            title = {
                HeaderText(
                    text = stringResource(Res.string.add_zekr),
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
                        ZekrEntryActions.onSaveClick()
                    }, enabled = uiState.isEntryValid, colors = ButtonDefaults.textButtonColors(
                        contentColor = zekrColors.onMainHeader,
                        disabledContentColor = zekrColors.onMainHeaderDisabled,
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
    uiState: ZekrEntryUiState,
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
        val counterEntry = uiState.zekrEntry

        MyTextField(
            stringResource(Res.string.zekr_content),
            counterEntry.text,
            uiState.isLabelValid,
            { ZekrEntryActions.onLabelChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = medium),
        )

        ZekrGoals(
            zekrEntry = uiState.zekrEntry,
        )


        MyTextField(
            stringResource(Res.string.coolDown),
            counterEntry.coolDown.toString(),
            uiState.isLabelValid,
            { ZekrEntryActions.onCoolDownChange(it) },
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
fun ZekrGoals(
    zekrEntry: ZekrEntry,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = large,
            ), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // yearly goal
        ZekrGoalItem(
            label = stringResource(Res.string.yearly_goal),
            value = zekrEntry.yearlyTarget.toString(),
            isValid = true,
            enabled = zekrEntry.yearlyTargetStatus == ZekrTargetStatus.Enabled,
            onValueChange = { ZekrEntryActions.onYearlyGoalChange(it) },
            onCheckedChange = {
                ZekrEntryActions.updateZekrEntry(
                    zekrEntry.copy(
                        yearlyTargetStatus = if (it) ZekrTargetStatus.Enabled else ZekrTargetStatus.Disabled
                    )
                )
            },
        )

        // monthly goal
        ZekrGoalItem(
            label = stringResource(Res.string.monthly_goal),
            value = zekrEntry.monthlyTarget.toString(),
            isValid = true,
            enabled = zekrEntry.monthlyTargetStatus == ZekrTargetStatus.Enabled,
            onValueChange = { ZekrEntryActions.onMonthlyGoalChange(it) },
            onCheckedChange = {
                ZekrEntryActions.updateZekrEntry(
                    zekrEntry.copy(
                        monthlyTargetStatus = if (it) ZekrTargetStatus.Enabled else ZekrTargetStatus.Disabled
                    )
                )
            },
        )

        // weekly goal
        ZekrGoalItem(
            label = stringResource(Res.string.weekly_goal),
            value = zekrEntry.weeklyTarget.toString(),
            isValid = true,
            enabled = zekrEntry.weeklyTargetStatus == ZekrTargetStatus.Enabled,
            onValueChange = { ZekrEntryActions.onWeeklyGoalChange(it) },
            onCheckedChange = {
                ZekrEntryActions.updateZekrEntry(
                    zekrEntry.copy(
                        weeklyTargetStatus = if (it) ZekrTargetStatus.Enabled else ZekrTargetStatus.Disabled
                    )
                )
            },
        )

        // daily goal
        ZekrGoalItem(
            label = stringResource(Res.string.daily_goal),
            value = zekrEntry.dailyTarget.toString(),
            isValid = true,
            enabled = zekrEntry.dailyTargetStatus == ZekrTargetStatus.Enabled,
            onValueChange = { ZekrEntryActions.onDailyGoalChange(it) },
            onCheckedChange = {
                ZekrEntryActions.updateZekrEntry(
                    zekrEntry.copy(
                        dailyTargetStatus = if (it) ZekrTargetStatus.Enabled else ZekrTargetStatus.Disabled
                    )
                )
            },
            keyboardType = KeyboardType.Number,
        )
    }
}


@Composable
fun ZekrGoalItem(
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
                    uiState = ZekrEntryUiState(),
                )
            }
        }
    }
}