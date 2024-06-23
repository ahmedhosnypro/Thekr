package com.thekr.ui.home.tab.sebha


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.values.Dimensions.large
import com.thekr.ui.values.Dimensions.small
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.create_zekr_group

/**
 * A button that triggers a dialog for creating a new category.
 *
 * @param width The width of the button.
 * @param calculatedTabHeight The calculated height of the tab.
 * @param onCategorySave Callback function invoked when a new category is saved.
 * The parameter is the index of the newly created category.
 */
@Composable
fun CreateCategoryButton(
    width: Dp,
    calculatedTabHeight: Dp,
    onCategorySave: (Int) -> Unit = {}
) {
    val showCreateCategoryDialog = remember { mutableStateOf(false) }

    IconButton(
        onClick = { showCreateCategoryDialog.value = true },
        modifier = Modifier
            .padding(horizontal = small)
            .requiredWidth(width)
            .requiredHeight(calculatedTabHeight),
    ) {
        Icon(
            modifier = Modifier.requiredSize(large),
            imageVector = Icons.Outlined.AddCircleOutline,
            contentDescription = stringResource(Res.string.create_zekr_group),
        )
    }

    CreateCategoryDialog(
        showCreateCategoryDialog = showCreateCategoryDialog,
        onCategorySave = onCategorySave
    )
}

@Preview
@Composable
fun CreateCategoryButtonPreview() {
    AppTheme {
        Surface {
            CreateCategoryButton(
                width = 30.dp,
                calculatedTabHeight = 30.dp,
            )
        }
    }
}