package com.thekr.ui.home.tab.sebha

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.thekr.data.proto.ThemeMode
import com.thekr.ui.AzkarActions
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.theme.hacenTunisiaLt
import com.thekr.ui.component.RtlView
import com.thekr.ui.values.Dimensions.medium
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.cancel
import com.thekr.resources.create
import com.thekr.resources.create_zekr_group
import com.thekr.resources.group_name

/**
 * Displays a dialog for creating a new category.
 *
 * @param showCreateCategoryDialog State variable controlling the
 *     visibility of the dialog.
 * @param onCategorySave Callback invoked when a new category is saved,
 *     providing the index of the new category.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CreateCategoryDialog(
    showCreateCategoryDialog: MutableState<Boolean>,
    onCategorySave: (Int) -> Unit = {}
) {
    AnimatedVisibility(visible = showCreateCategoryDialog.value) {
        BasicAlertDialog(
            onDismissRequest = { showCreateCategoryDialog.value = false }
        ) {
            CreateCategoryDialogContent(
                onCategorySave = onCategorySave,
                onDismissRequest = remember { { showCreateCategoryDialog.value = false } }
            )
        }
    }
}

/**
 * Content of the new category dialog. Allows the user to enter a category
 * name and create a new category.
 *
 * @param onCategorySave Callback invoked when a new category is saved,
 *     providing the index of the new category.
 * @param onDismissRequest Callback invoked to dismiss the dialog.
 */
@Composable
private fun CreateCategoryDialogContent(
    onCategorySave: (Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    val isError by remember { derivedStateOf { !isValidCategoryName(categoryName) } }

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
            .padding(medium),
        verticalArrangement = Arrangement.spacedBy(medium, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CreateCategoryDialogTitle()

        CategoryNameInput(
            categoryName = categoryName,
            isError = isError,
            onCategoryNameChange = remember {
                { newName ->
                    if (!newName.contains("\n")) {
                        categoryName = newName
                    }
                }
            }
        )

        CreateCategoryDialogButtons(
            isError = isError,
            onDismissRequest = onDismissRequest,
            onCreateClick = {
                val savedCategoryIndex = AzkarActions.createNewUserCategory(categoryName)
                if (savedCategoryIndex != -1) {
                    onCategorySave(savedCategoryIndex)
                }
                onDismissRequest()
            }
        )
    }
}

/** Displays the title of the Create Category dialog. */
@Composable
private fun CreateCategoryDialogTitle() {
    Text(
        text = stringResource(Res.string.create_zekr_group),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall,
        fontFamily = hacenTunisiaLt()
    )
}

/**
 * Provides a TextField for entering the category name.
 *
 * @param categoryName The current category name.
 * @param isError Indicates if the category name is invalid.
 * @param onCategoryNameChange Callback invoked when the category name
 *     changes.
 */
@Composable
private fun CategoryNameInput(
    categoryName: String,
    isError: Boolean,
    onCategoryNameChange: (String) -> Unit
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = categoryName,
        onValueChange = onCategoryNameChange,
        isError = isError,
        label = {
            Text(
                text = stringResource(Res.string.group_name),
                fontFamily = hacenTunisiaLt()
            )
        },
        singleLine = true
    )
}

/**
 * Displays the Create and Cancel buttons for the dialog.
 *
 * @param isError Indicates if the current category name is invalid.
 * @param onDismissRequest Callback invoked to dismiss the dialog.
 * @param onCreateClick Callback invoked when the Create button is clicked.
 */
@Composable
private fun CreateCategoryDialogButtons(
    isError: Boolean,
    onDismissRequest: () -> Unit,
    onCreateClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            medium,
            Alignment.End
        )
    ) {
        // Cancel button
        OutlinedButton(
            onClick = onDismissRequest,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(text = stringResource(Res.string.cancel), fontFamily = hacenTunisiaLt())
        }

        // Create button
        OutlinedButton(
            onClick = { if (!isError) onCreateClick() }, // Only execute onCreateClick if no errors
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(text = stringResource(Res.string.create), fontFamily = hacenTunisiaLt())
        }
    }
}

/**
 * Checks if a category name is valid. A valid name is not empty, not
 * blank, and has a length between 3 and 20 characters.
 *
 * @param categoryName The category name to validate.
 * @return True if the category name is valid, false otherwise.
 */
fun isValidCategoryName(categoryName: String): Boolean {
    return categoryName.isNotEmpty() &&
            categoryName.isNotBlank() &&
            categoryName.length in 3..20
}

@Preview
@Composable
fun CreateCategoryDialogContentPreviewDark() {
    AppTheme(themeMode = ThemeMode.Dark) {
        Surface {
            RtlView {
                CreateCategoryDialogContent(
                    onCategorySave = {},
                    onDismissRequest = {}
                )
            }
        }
    }
}

@Preview
@Composable
fun CreateCategoryDialogContentPreview() {
    AppTheme(themeMode = ThemeMode.Light) {
        Surface {
            RtlView {
                CreateCategoryDialogContent(
                    onCategorySave = {},
                    onDismissRequest = {}
                )
            }
        }
    }
}