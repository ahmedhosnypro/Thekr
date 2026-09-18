// @Composable functions are PascalCase per the Compose API guidelines (detekt
// exempts them via naming.FunctionNaming ignoreAnnotated; ktlint's
// function-naming rule has no working equivalent in this setup).
@file:Suppress("ktlint:standard:function-naming")

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.thekr.thekr.ThekrEntry
import com.thekr.data.thekr.thekr.ThekrEntryUiState
import com.thekr.model.ThekrTargetStatus
import com.thekr.resources.Res
import com.thekr.resources.add_thekr
import com.thekr.resources.coolDown
import com.thekr.resources.daily_goal
import com.thekr.resources.monthly_goal
import com.thekr.resources.save
import com.thekr.resources.thekr_content
import com.thekr.resources.weekly_goal
import com.thekr.resources.yearly_goal
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.component.bar.AppTopBar
import com.thekr.ui.component.bar.HeaderText
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.theme.AppColors
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.viewmodel.AppViewModelProvider
import com.thekr.values.Dimensions.large
import com.thekr.values.Dimensions.medium
import com.thekr.values.Dimensions.normal
import com.thekr.values.Dimensions.small
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource

// todo:val c = LocalSoftwareKeyboardController.current
@Composable
fun CounterEntryScreen(
    settingsDetails: SettingsDetails,
    viewModel: ThekrEntryViewModel = viewModel {
        AppViewModelProvider.Factory.create(ThekrEntryViewModel::class, this)
    },
) {
    DisposableEffect(viewModel) {
        ThekrEntryActions.initActions(viewModel)
        onDispose { ThekrEntryActions.clearActions() }
    }
    CounterEntry(
        viewState = viewModel.viewState,
        settingsDetails = settingsDetails,
    )
}

// The UI state is deliberately NOT collected at this level: each leaf below
// subscribes to the fields it renders, so a keystroke only invalidates the
// edited field's leaf (plus the save button when validity flips) instead of
// the whole screen.
@Composable
fun CounterEntry(
    viewState: StateFlow<ThekrEntryUiState>,
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
                SaveActionButton(
                    viewState = viewState,
                    thekrColors = thekrColors,
                )
            },
            settingsDetails = settingsDetails,
        )
    }) { innerPadding ->
        CounterEntryBody(viewState, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
private fun SaveActionButton(
    viewState: StateFlow<ThekrEntryUiState>,
    thekrColors: AppColors,
) {
    val uiState = viewState.collectAsState()
    val isEntryValid by remember { derivedStateOf { uiState.value.isEntryValid } }
    Button(
        onClick = { ThekrEntryActions.onSaveClick() },
        enabled = isEntryValid,
        colors = ButtonDefaults.textButtonColors(
            contentColor = thekrColors.onMainHeader,
            disabledContentColor = thekrColors.onMainHeaderDisabled,
        ),
    ) {
        Text(stringResource(Res.string.save))
    }
}

@Composable
private fun CounterEntryBody(
    viewState: StateFlow<ThekrEntryUiState>,
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
                top = small,
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        LabelField(viewState)

        ThekrGoals(
            viewState = viewState,
        )

        CoolDownField(viewState)
    }
}

@Composable
private fun LabelField(
    viewState: StateFlow<ThekrEntryUiState>,
) {
    val uiState = viewState.collectAsState()
    val text by remember { derivedStateOf { uiState.value.thekrEntry.text } }
    val isLabelValid by remember { derivedStateOf { uiState.value.isLabelValid } }
    MyTextField(
        stringResource(Res.string.thekr_content),
        text,
        isLabelValid,
        { ThekrEntryActions.onLabelChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = medium),
    )
}

@Composable
private fun CoolDownField(
    viewState: StateFlow<ThekrEntryUiState>,
) {
    val uiState = viewState.collectAsState()
    val coolDown by remember { derivedStateOf { uiState.value.thekrEntry.coolDown } }
    MyTextField(
        stringResource(Res.string.coolDown),
        coolDown.toString(),
        true,
        { ThekrEntryActions.onCoolDownChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = medium),
        keyboardType = KeyboardType.Number,
    )
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
    viewState: StateFlow<ThekrEntryUiState>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = large,
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // yearly goal
        ThekrGoalField(
            label = stringResource(Res.string.yearly_goal),
            viewState = viewState,
            target = { it.thekrEntry.yearlyTarget },
            status = { it.thekrEntry.yearlyTargetStatus },
            onValueChange = { ThekrEntryActions.onYearlyGoalChange(it) },
            onStatusChange = { entry, checked ->
                entry.copy(
                    yearlyTargetStatus = if (checked) ThekrTargetStatus.Enabled else ThekrTargetStatus.Disabled,
                )
            },
        )

        // monthly goal
        ThekrGoalField(
            label = stringResource(Res.string.monthly_goal),
            viewState = viewState,
            target = { it.thekrEntry.monthlyTarget },
            status = { it.thekrEntry.monthlyTargetStatus },
            onValueChange = { ThekrEntryActions.onMonthlyGoalChange(it) },
            onStatusChange = { entry, checked ->
                entry.copy(
                    monthlyTargetStatus = if (checked) ThekrTargetStatus.Enabled else ThekrTargetStatus.Disabled,
                )
            },
        )

        // weekly goal
        ThekrGoalField(
            label = stringResource(Res.string.weekly_goal),
            viewState = viewState,
            target = { it.thekrEntry.weeklyTarget },
            status = { it.thekrEntry.weeklyTargetStatus },
            onValueChange = { ThekrEntryActions.onWeeklyGoalChange(it) },
            onStatusChange = { entry, checked ->
                entry.copy(
                    weeklyTargetStatus = if (checked) ThekrTargetStatus.Enabled else ThekrTargetStatus.Disabled,
                )
            },
        )

        // daily goal
        ThekrGoalField(
            label = stringResource(Res.string.daily_goal),
            viewState = viewState,
            target = { it.thekrEntry.dailyTarget },
            status = { it.thekrEntry.dailyTargetStatus },
            onValueChange = { ThekrEntryActions.onDailyGoalChange(it) },
            onStatusChange = { entry, checked ->
                entry.copy(
                    dailyTargetStatus = if (checked) ThekrTargetStatus.Enabled else ThekrTargetStatus.Disabled,
                )
            },
        )
    }
}

@Composable
private fun ThekrGoalField(
    label: String,
    viewState: StateFlow<ThekrEntryUiState>,
    target: (ThekrEntryUiState) -> Long,
    status: (ThekrEntryUiState) -> ThekrTargetStatus,
    onValueChange: (String) -> Unit,
    onStatusChange: (ThekrEntry, Boolean) -> ThekrEntry,
) {
    val uiState = viewState.collectAsState()
    val targetValue by remember { derivedStateOf { target(uiState.value) } }
    val enabled by remember { derivedStateOf { status(uiState.value) == ThekrTargetStatus.Enabled } }
    ThekrGoalItem(
        label = label,
        value = targetValue.toString(),
        isValid = true,
        enabled = enabled,
        onValueChange = onValueChange,
        onCheckedChange = { checked: Boolean ->
            ThekrEntryActions.updateThekrEntry(
                onStatusChange(uiState.value.thekrEntry, checked),
            )
        },
    )
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = enabled,
            onCheckedChange = onCheckedChange,
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
                    viewState = MutableStateFlow(ThekrEntryUiState()),
                )
            }
        }
    }
}
