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
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.ui.home.list.CategoryList
import com.thekr.ui.home.list.ZekrList
import com.thekr.ui.home.list.categoryDetailsPreviewState
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.util.RtlView
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Displays a tab content containing either a list of categories or a list of Zekrs,
 * depending on the content of the provided [categoryDetails].
 *
 * @param categoryDetails The state of the current category details.
 * @param settingsDetails The settings detail for theming and customization.
 * @param tabIndex The index of the current tab.
 * @param onZekrClick Callback invoked when a Zekr item is clicked.
 * @param onCategoryClick Callback invoked when a category is clicked.
 */
@Composable
fun ZekrTab(
    categoryDetails: MutableState<CategoryDetails>,
    settingsDetails: SettingsDetails,
    tabIndex: Int,
    onZekrClick: (Int, Long, Long) -> Unit = { _, _, _ -> },
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
        // Show ZekrList if there are Zekr items
        if (category.zekrList.isNotEmpty()) {
            ZekrList(
                category = categoryDetails,
                modifier = Modifier.fillMaxSize(),
                onHomeListItemClick = onZekrClick,
                settingsDetails = settingsDetails,
                tabIndex = tabIndex,
            )
        }
    }
}

@Preview
@Composable
fun ZekrTabPreview() {
    AppTheme {
        Surface {
            RtlView {
                ZekrTab(
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
fun ZekrTabPreviewZekrList() {
    AppTheme {
        Surface {
            RtlView {
                ZekrTab(
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