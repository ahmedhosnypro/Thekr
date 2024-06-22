package com.thekr.ui.home.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thekr.R
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.data.zekr.instance.ZekrInstanceDetails
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.util.RtlView

/**
 * Displays a list of Zekr items within a category.
 *
 * @param category The state of the category details containing the Zekr items.
 * @param settingsDetails Settings details for theming and customization.
 * @param modifier Modifier to be applied to the LazyVerticalGrid.
 * @param tabIndex The index of the current tab.
 * @param onHomeListItemClick Callback invoked when a Zekr item is clicked in the Home context.
 * @param onCategoryListItemClick Callback invoked when a Zekr item is clicked in the Category context.
 */
@Composable
fun ZekrList(
    category: MutableState<CategoryDetails>,
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
    tabIndex: Int = 0,
    onHomeListItemClick: (Int, Long, Long) -> Unit = { _, _, _ -> },
    onCategoryListItemClick: (MutableState<ZekrInstanceDetails>) -> Unit = {},
) {
    val categoryValue = category.value // Access the value once for optimization
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier
            .fillMaxSize()
            .padding(top = dimensionResource(id = R.dimen.padding_medium)),
    ) {
        items(
            items = categoryValue.zekrInstanceList,
            key = { it.value.id }
        ) { item ->
            val index = categoryValue.zekrInstanceList.indexOf(item)
            val colorIndex = if (index < 6) index else index % 6

            // Simplify data retrieval using associateBy
            val zekrMap = categoryValue.zekrList.associateBy { it.value.id }
            val countMap = categoryValue.countList.associateBy { it.value.zekrInstanceId }

            val zekr = zekrMap[item.value.zekrId]?.value
            val count = countMap[item.value.zekrId]?.value?.dailyCount

            ZekrCard(
                text = zekr?.text ?: "",
                count = count ?: 0L,
                target = item.value.dailyTarget,
                onItemClick = {
                    onHomeListItemClick(tabIndex, item.value.categoryId, item.value.zekrId)
                    // Simplify ZekrInstanceDetails retrieval
                    val zekrInstance =
                        categoryValue.zekrInstanceList.firstOrNull { it.value.id == item.value.zekrId }
                    onCategoryListItemClick(zekrInstance ?: mutableStateOf(ZekrInstanceDetails()))
                },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
                leadingIcon = {
                    Image(
                        painterResource(id = R.drawable.zekr_indicator),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(zekrIndicatorColor(colorIndex)),
                        modifier = Modifier.height(12.dp),
                    )
                },
                settingsDetails = settingsDetails,
            )
        }
        item {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_xlarge)))
        }
    }
}

@Preview
@Composable
fun ZekrListPreview() {
    AppTheme {
        Surface {
            RtlView {
                ZekrList(
                    category = categoryDetailsPreviewState(),
                    settingsDetails = SettingsDetails(),
                )
            }
        }
    }
}