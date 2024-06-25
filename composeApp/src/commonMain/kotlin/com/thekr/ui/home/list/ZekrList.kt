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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.resources.Res
import com.thekr.resources.zekr_indicator
import com.thekr.ui.component.RtlView
import com.thekr.ui.counter.CounterHelper
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.values.Dimensions.medium
import com.thekr.ui.values.Dimensions.xLarge
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Displays a list of Zekr items within a category.
 *
 * @param categoryDetails The state of the category details containing the
 *     Zekr items.
 * @param settingsDetails Settings details for theming and customization.
 * @param modifier Modifier to be applied to the LazyVerticalGrid.
 * @param tabIndex The index of the current tab.
 * @param homeOnClick Callback invoked when a Zekr item is clicked in the
 *     Category context.
 */
@Composable
fun ZekrList(
    categoryDetails: MutableState<CategoryDetails>,
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
    tabIndex: Int = 0,
    homeOnClick: (tabIndex: Int, categoryId: Long, zekrId: Long) -> Unit = { _, _, _ -> },
    categoryListOnClick: (tabIndex: Int) -> Unit = {},
) {
    val category = categoryDetails.value // Access the value once for optimization
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier
            .fillMaxSize()
            .padding(top = medium),
    ) {
        items(
            items = category.zekrInstanceList,
            key = { it.value.id }
        ) { item ->
            val index = category.zekrInstanceList.indexOf(item)
            val colorIndex = if (index < 6) index else index % 6

            // Simplify data retrieval using associateBy
            val zekrMap = category.zekrList.associateBy { it.value.id }
            val countMap = category.countList.associateBy { it.value.zekrInstanceId }

            val zekr = zekrMap[item.value.zekrId]?.value
            val count = countMap[item.value.zekrId]?.value?.dailyCount

            ZekrCard(
                text = zekr?.text ?: "",
                count = count ?: 0L,
                target = item.value.dailyTarget,
                homeOnClick = {
                    homeOnClick(tabIndex, item.value.categoryId, item.value.zekrId)
                },
                categoryListOnClick = {
                    categoryListOnClick(CounterHelper.tabIndexOf(item.value.id))
                },
                modifier = Modifier.padding(horizontal = medium),
                leadingIcon = {
                    Image(
                        painterResource(Res.drawable.zekr_indicator),
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
            Spacer(modifier = Modifier.height(xLarge))
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
                    categoryDetails = categoryDetailsPreviewState(),
                    settingsDetails = SettingsDetails(),
                )
            }
        }
    }
}