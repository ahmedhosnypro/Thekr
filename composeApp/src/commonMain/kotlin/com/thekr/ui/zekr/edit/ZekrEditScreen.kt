package com.thekr.ui.zekr.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thekr.ui.component.DefaultHorizontalDivider
import com.thekr.ui.viewmodel.AppViewModelProvider
import com.thekr.ui.zekr.entry.MyTextField

@Composable
fun CounterEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: ZekrEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.counterEditUiState.value
    val counterEntry = uiState.zekrEntry

    Scaffold(topBar = {
//        CounterEntryAppBar(
//            actions = ZekrEntryActions(),
//            title = uiState.zekrEntry.text,
//            enableSave = uiState.isEntryValid,
//        )
    }) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DefaultHorizontalDivider()
            val colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondary,
                focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.onSecondary,
            )

            MyTextField(
                "label",
                counterEntry.text,
                uiState.isLabelValid,
                viewModel::updateLabel,
            )

            // color
//                ThemeEditor(
//                    windowSize = windowSize,
//                    themeDetails =  viewTheme,
//                    onDismiss = viewModel::onThemeDismiss,
//                    onDone = viewModel::onThemeDone,
//                )
        }
    }
}