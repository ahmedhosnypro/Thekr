package com.thekr.ui.counter.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.ui.theme.ZekrTheme
import com.thekr.ui.counter.CounterHelper
import com.thekr.ui.counter.viewModel.CounterUiState
import com.thekr.ui.values.Dimensions.large
import com.thekr.ui.values.Dimensions.medium
import com.thekr.ui.values.Dimensions.small
import com.thekr.ui.theme.hacenTunisiaLt
import com.thekr.ui.theme.uthmanicScript

@Composable
fun ZekrText(
    tabIndex: Int,
    counterUiState: CounterUiState,
    categoryDetails: MutableState<CategoryDetails>,
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
) {
    // todo: implement layout direction for english text
    // check if text start with rtl letter, or ltr letter
    val zekrColors = ZekrTheme.colors(settingsDetails)
    val zekr = CounterHelper.getZekr(tabIndex).value

    val textStyle = TextStyle(
        fontSize = settingsDetails.fontSize.sp,

        textAlign = TextAlign.Start,
    )
    if (zekr.text.isEmpty()) {
        return
    }
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = large,
                end = large,
            ),
        verticalArrangement = Arrangement.spacedBy(large)
    ) {

        item {
            Spacer(Modifier.height(small))
        }

        item {
            Text(
                text = zekr.text,
                style = textStyle,
                fontFamily = if (zekr.bsmalaType != 0) uthmanicScript else hacenTunisiaLt,
            )
        }
        if (categoryDetails.value.fadlList.any { it.zekrId == counterUiState.currentZekrInstance.value.id }) {
            val fadlList =
                categoryDetails.value.fadlList.filter { it.zekrId == counterUiState.currentZekrInstance.value.id }
            items(fadlList) { fadl ->
                Text(
                    text = fadl.fadl,
                    style = textStyle,
                    lineHeight = if (zekr.bsmalaType != 0) (settingsDetails.fontSize + 28).sp
                    else (settingsDetails.fontSize + 16).sp,
                    color = zekrColors.fadlText
                )
            }
        }
        item {
            Spacer(Modifier.height(medium))
        }
    }
}