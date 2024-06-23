package com.thekr.ui.home.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.ui.component.DefaultHorizontalDivider
import com.thekr.ui.values.Dimensions.medium
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.theme.ImageResourceHelper
import com.thekr.ui.theme.droidKufi
import com.thekr.ui.util.NoRippleInteractionSource
import com.thekr.ui.component.RtlView
import com.thekr.ui.values.Colors.listDivider
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Displays the Dua tab content, which can show a list of Dua categories or
 * a list of Zekr.
 *
 * @param duaStack The stack of Dua categories, representing the navigation
 *     hierarchy.
 * @param settingsDetails The settings detail for theming and
 *     customization.
 * @param onZekrClick Callback function invoked when a Zekr item is
 *     clicked.
 * @param onCategoryClick Callback function invoked when a Dua category is
 *     clicked.
 * @param tabIndex The index of the current tab.
 */
@Composable
fun DuaTab(
    duaStack: SnapshotStateList<MutableState<CategoryDetails>>,
    settingsDetails: SettingsDetails,
    onZekrClick: (Int, Long, Long) -> Unit = { _, _, _ -> },
    onCategoryClick: (MutableState<CategoryDetails>) -> Unit = {},
    tabIndex: Int = 0,
) {
    val categoryDetails = duaStack.last()
    when (duaStack.size) {
        1 -> DuaCategoryList(categoryDetails, onCategoryClick)
        else -> { // duaStack.size > 1
            if (categoryDetails.value.childCategories.isNotEmpty()) {
                CategoryList(
                    categoryList = categoryDetails.value.childCategories,
                    onClick = onCategoryClick,
                    settingsDetails = settingsDetails,
                )
            }
            if (categoryDetails.value.zekrList.isNotEmpty()) {
                ZekrList(
                    modifier = Modifier.fillMaxSize(),
                    tabIndex = tabIndex,
                    category = categoryDetails,
                    onHomeListItemClick = onZekrClick,
                    settingsDetails = settingsDetails,
                )
            }
        }
    }
}

/**
 * Displays a list of Dua categories.
 *
 * @param categoryDetails The state of the current category details.
 * @param onCategoryClick Callback function invoked when a category is
 *     clicked.
 */
@Composable
private fun DuaCategoryList(
    categoryDetails: MutableState<CategoryDetails>,
    onCategoryClick: (MutableState<CategoryDetails>) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = medium),
        verticalArrangement = Arrangement.Center
    ) {
        items(categoryDetails.value.childCategories, key = { it.value.id }) { childCategory ->
            DuaCategoryCard(
                category = childCategory,
                onClick = { onCategoryClick(childCategory) }
            )
            if (childCategory != categoryDetails.value.childCategories.last()) {
                DefaultHorizontalDivider(thickness = 2.dp, color = listDivider)
            }
        }
    }
}

/**
 * Displays a card representing a single Dua category.
 *
 * @param modifier Modifier to be applied to the card layout.
 * @param category The state of the category details.
 * @param onClick Callback function invoked when the card is clicked.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DuaCategoryCard(
    modifier: Modifier = Modifier,
    category: MutableState<CategoryDetails>,
    onClick: () -> Unit = {},
) {
    val contentColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier
            .combinedClickable(
                interactionSource = NoRippleInteractionSource(),
                indication = null,
                onClick = onClick
            )
            .fillMaxWidth()
            .padding(horizontal = medium, vertical = medium),
        verticalArrangement = Arrangement.spacedBy(medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val iconFileName = category.value.iconFileName
        if (iconFileName != null) {
            val iconResId =
                ImageResourceHelper.getDrawableResourceIdFromFileName(fileName = iconFileName)
            if (iconResId != null) {
                Image(
                    painter = painterResource(iconResId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .aspectRatio(1f), // Default aspect ratio if image not available
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Text(
            text = category.value.name,
            color = contentColor,
            fontFamily = droidKufi(),
            maxLines = 2,
        )
    }
}

@Preview
@Composable
fun ZekrTabPreview() {
    AppTheme(themeMode = ThemeMode.Dark) {
        Surface {
            RtlView {
                DuaTab(
                    duaStack = duaStackPreview(),
                    settingsDetails = SettingsDetails(),
                )
            }
        }
    }
}


@Composable
private fun duaStackPreview() = remember {
    mutableStateListOf(
        mutableStateOf(
            CategoryDetails(
                childCategories = mutableStateListOf(
                    mutableStateOf(
                        CategoryDetails(
                            id = 1,
                            name = "أدعية من القرآن",
                            iconFileName = "doaa_quran.png"
                        )
                    ),
                    mutableStateOf(
                        CategoryDetails(
                            id = 2,
                            name = "أدعية من السنة",
                            iconFileName = "doaa_sunna.png"
                        )
                    ),
                )
            ),
        )
    )
}