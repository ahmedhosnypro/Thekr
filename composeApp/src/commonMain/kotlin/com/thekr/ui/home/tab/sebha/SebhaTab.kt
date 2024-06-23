@file:OptIn(ExperimentalFoundationApi::class)

package com.thekr.ui.home.tab.sebha

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.ui.AzkarActions
import com.thekr.ui.home.list.categoryDetailsListPreviewState
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.util.RtlView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.create_zekr_group

/**
 * Represents the Sebha tab content on the Home screen.
 *
 * @param userAzkar List of user-created Azkar categories.
 * @param settingsDetails Settings details for theming and customization.
 */
@Composable
fun SebhaTab(
    userAzkar: SnapshotStateList<MutableState<CategoryDetails>>,
    settingsDetails: SettingsDetails,
) {
    val pagerState = rememberPagerState(
        initialPage = AzkarActions.sebhaTabSubTabsInitialPage(),
        pageCount = { userAzkar.size }
    )
    val coroutineScope = rememberCoroutineScope()

    // Callback for handling new category creation
    val onNewCategorySave = remember(coroutineScope, userAzkar, pagerState) {
        { index: Int ->
            handleNewCategorySave(index, userAzkar, pagerState, coroutineScope)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        AzkarActions.updateCurrentSebhaViewedCategory(pagerState.currentPage)
    }

    Column {
        if (userAzkar.isEmpty()) {
            // Show new category button if there are no categories
            ShowCreateCategoryDialog(onNewCategorySave)
        } else {
            // Show SebhaTabBar and SebhaPages if there are categories
            SebhaTabBar(
                userAzkar = userAzkar,
                pagerState = pagerState,
                onCategorySave = onNewCategorySave
            )
            SebhaPages(
                userAzkar = userAzkar,
                pagerState = pagerState,
                settingsDetails = settingsDetails,
            )
        }
    }
}

/**
 * Displays a dialog for creating a new category when there are no existing categories.
 */
@Composable
private fun ShowCreateCategoryDialog(
    onNewCategorySave: (Int) -> Unit
) {
    val showCreateNewDialog = remember { mutableStateOf(false) }
    SebhaAddNewButton(
        onCLick = { showCreateNewDialog.value = true },
        text = stringResource(Res.string.create_zekr_group)
    )
    CreateCategoryDialog(
        showCreateCategoryDialog = showCreateNewDialog,
        onCategorySave = onNewCategorySave
    )
}

/**
 * Handles the logic for saving a new category and scrolling to it in the pager.
 */
private fun handleNewCategorySave(
    index: Int,
    userAzkar: List<MutableState<CategoryDetails>>,
    pagerState: PagerState,
    coroutineScope: CoroutineScope
) {
    if (index in userAzkar.indices && index < pagerState.pageCount) {
        coroutineScope.launch {
            if (pagerState.pageCount == userAzkar.size) {
                pagerState.animateScrollToPage(index)
            }
        }
    } else {
        //todo:
//        Log.d("SebhaTab", "onNewCategorySave: index is out of bounds")
    }
}


@Preview
@Composable
fun SebhaTabPreview() {
    AppTheme(themeMode = ThemeMode.Dark) {
        Surface {
            RtlView {
                SebhaTab(
                    userAzkar = categoryDetailsListPreviewState(),
                    settingsDetails = SettingsDetails(),
                )
            }
        }
    }
}

@Preview
@Composable
fun SebhaTabPreviewEmptyCategoryList() {
    AppTheme(themeMode = ThemeMode.Dark) {
        Surface {
            RtlView {
                SebhaTab(
                    userAzkar = remember { mutableStateListOf() },
                    settingsDetails = SettingsDetails(),
                )
            }
        }
    }
}
