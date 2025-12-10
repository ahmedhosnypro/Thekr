package com.thekr.ui.home.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.ui.home.list.CategoryList
import com.thekr.ui.home.list.ThekrList
import com.thekr.ui.home.list.categoryDetailsPreviewState
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.home.HomeActions
import androidx.compose.ui.tooling.preview.Preview

/**
 * Displays a tab content containing either a list of categories or a list of Thekrs,
 * depending on the content of the provided [categoryDetails].
 *
 * @param categoryDetails The state of the current category details.
 * @param settingsDetails The settings detail for theming and customization.
 * @param tabIndex The index of the current tab.
 * @param onCategoryClick Callback invoked when a category is clicked.
 */
@Composable
fun ThekrTab(
    categoryDetails: MutableState<CategoryDetails>,
    settingsDetails: SettingsDetails,
    tabIndex: Int,
    onCategoryClick: (MutableState<CategoryDetails>) -> Unit = {},
) {
    val category = categoryDetails.value // Access the value only once for optimization

    Column {
        // Show CategoryList if there are child categories
        if (category.childCategories.isNotEmpty()) {
            CategoryList(
                categoryList = category.childCategories,
                onClick = onCategoryClick,
                settingsDetails = settingsDetails,
            )
        }
        // Show ThekrList if there are Thekr items
        if (category.thekrList.isNotEmpty()) {
            ThekrList(
                categoryDetails = categoryDetails,
                modifier = Modifier.fillMaxSize(),
                settingsDetails = settingsDetails,
                tabIndex = tabIndex,
                homeOnClick = {tabIndex1: Int, categoryId: Long, thekrId: Long ->
                    HomeActions.onThekrClick(tabIndex1, categoryId, thekrId)
                }
            )
        }
    }
}

@Preview
@Composable
fun ThekrTabPreview() {
    AppTheme {
        Surface {
            LocalizedApp {
                ThekrTab(
                    categoryDetails = categoryDetailsPreviewState(),
                    settingsDetails = SettingsDetails(),
                    tabIndex = 0,
                )
            }
        }
    }
}

@Preview
@Composable
fun ThekrTabPreviewThekrList() {
    AppTheme {
        Surface {
            LocalizedApp {
                ThekrTab(
                    categoryDetails = categoryDetailsPreviewState(
                        childCategories = remember { mutableStateListOf() }
                    ),
                    settingsDetails = SettingsDetails(),
                    tabIndex = 0,
                )
            }
        }
    }
}