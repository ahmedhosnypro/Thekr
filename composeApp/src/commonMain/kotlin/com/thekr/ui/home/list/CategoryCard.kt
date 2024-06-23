package com.thekr.ui.home.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.ui.component.DefaultHorizontalDivider
import com.thekr.ui.values.Dimensions.medium
import com.thekr.ui.theme.*
import com.thekr.ui.theme.ImageResourceHelper.getDrawableResourceIdFromFileName
import com.thekr.ui.util.NoRippleInteractionSource
import com.thekr.ui.component.RtlView
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.zekr_indicator

/**
 * Displays a list of categories in a grid layout.
 *
 * @param categoryList The list of categories to display.
 * @param settingsDetails The settings detail for theming and
 *     customization.
 * @param modifier Modifier to be applied to the LazyVerticalGrid.
 * @param onClick Callback function to be invoked when a category is
 *     clicked.
 */
@Composable
fun CategoryList(
    categoryList: SnapshotStateList<MutableState<CategoryDetails>>,
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
    onClick: (MutableState<CategoryDetails>) -> Unit = {},
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier
            .fillMaxSize()
            .padding(top = medium),
        verticalArrangement = Arrangement.spacedBy(medium),
    ) {
        // Use items instead of itemsIndexed for better performance
        items(items = categoryList, key = { it.value.id }) { item ->
            CategoryListItem(item, settingsDetails, onClick)
        }
    }
}

/**
 * Represents a single item in the category list. Displays a CategoryCard
 * and a divider.
 *
 * @param item The category details for the list item.
 * @param settingsDetails The settings detail for theming and
 *     customization.
 * @param onClick Callback function to be invoked when the category card is
 *     clicked.
 */
@Composable
private fun CategoryListItem(
    item: MutableState<CategoryDetails>,
    settingsDetails: SettingsDetails,
    onClick: (MutableState<CategoryDetails>) -> Unit
) {
    val indicatorColor = zekrIndicatorColor(item.value.id.toInt())
    Column {
        CategoryCard(
            category = item,
            onClick = { onClick(item) },
            modifier = Modifier.padding(horizontal = medium),
            leadingIcon = {
                Image(
                    painterResource(Res.drawable.zekr_indicator),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(indicatorColor),
                    modifier = Modifier.height(12.dp),
                )
            },
            settingsDetails = settingsDetails
        )
        DefaultHorizontalDivider(
            thickness = 4.dp,
            color = ZekrTheme.colors(settingsDetails).listDivider
        )
    }
}

/**
 * Displays a category card with an optional icon, title, and navigation
 * arrow.
 *
 * @param settingsDetails The settings detail for theming and
 *     customization.
 * @param modifier Modifier to be applied to the card layout.
 * @param category The category details for the card.
 * @param onClick Callback function to be invoked when the card is clicked.
 * @param leadingIcon An optional composable function to display a leading
 *     icon.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryCard(
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
    category: MutableState<CategoryDetails>,
    onClick: () -> Unit = {},
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    val contentColor = MaterialTheme.colorScheme.onSurface
    val zekrColors = ZekrTheme.colors(settingsDetails)

    Row(
        modifier = modifier
            .combinedClickable(
                interactionSource = NoRippleInteractionSource(),
                indication = null,
                onClick = onClick // Simplified onClick
            )
            .fillMaxWidth()
            .padding(horizontal = medium, vertical = medium),
        horizontalArrangement = Arrangement.spacedBy(medium, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val iconFileName = category.value.iconFileName
        if (iconFileName != null) {
            val iconResId = getDrawableResourceIdFromFileName(fileName = iconFileName)
            if (iconResId != null) {
                Image(
                    painter = painterResource(iconResId),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(.1f),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (isDark(settingsDetails)) ColorFilter.tint(zekrColors.cardIconTint) else null
                )
            }
        } else {
            leadingIcon?.invoke() // Use invoke() instead of null check and then call
        }

        Text(
            text = category.value.name,
            color = contentColor,
            fontFamily = droidKufi(),
            maxLines = 2,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Filled.ArrowBackIosNew,
            contentDescription = null,
            tint = zekrColors.cardCallToActionIcon,
        )
    }
}

@Preview
@Composable
fun CategoryCardPreview() {
    AppTheme(ThemeMode.Dark) {
        RtlView {
            Surface {
                CategoryCard(
                    category = remember {
                        mutableStateOf(
                            CategoryDetails(
                                name = "أذكار الصباح",
                                iconFileName = "azkar_masaa.png"
                            )
                        )
                    },
                    settingsDetails = SettingsDetails(themeMode = ThemeMode.Dark)
                )
            }
        }
    }
}

@Preview
@Composable
fun CategoryCardNoIconPreview() {
    AppTheme(ThemeMode.Dark) {
        RtlView {
            Surface {
                CategoryCard(
                    category = remember { mutableStateOf(CategoryDetails(name = "أذكار الصباح")) },
                    settingsDetails = SettingsDetails(themeMode = ThemeMode.Dark)
                )
            }
        }
    }
}

@Preview
@Composable
fun CategoryListPreview() {
    AppTheme {
        RtlView {
            Surface {
                CategoryList(
                    categoryList = remember {
                        mutableStateListOf(
                            mutableStateOf(
                                CategoryDetails(
                                    name = "أذكار الصباح",
                                    iconFileName = "azkar_masaa1.png"
                                )
                            )
                        )
                    },
                    settingsDetails = SettingsDetails(themeMode = ThemeMode.Dark)
                )
            }
        }
    }
}