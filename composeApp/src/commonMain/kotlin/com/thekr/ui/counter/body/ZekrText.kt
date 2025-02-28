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
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.counter.CounterHelper
import com.thekr.ui.counter.viewmodel.CounterUiState
import com.thekr.values.Dimensions.large
import com.thekr.values.Dimensions.small
import com.thekr.ui.theme.hacenTunisiaLt
import com.thekr.ui.theme.uthmanicScript
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.unit.dp

@Composable
fun ThekrText(
    tabIndex: Int,
    counterUiState: CounterUiState,
    categoryDetails: MutableState<CategoryDetails>,
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
) {
    // todo: implement layout direction for english text
    // check if text start with rtl letter, or ltr letter
    val thekrColors = AppTheme.colors(settingsDetails)
    val thekr = CounterHelper.getThekr(tabIndex).value

    val textStyle = TextStyle(
        fontSize = settingsDetails.fontSize.sp,

        textAlign = TextAlign.Start,
    )
    if (thekr.text.isEmpty()) {
        return
    }

    val listState = rememberLazyListState()
//    val coroutineScope = rememberCoroutineScope()

    //  todo: dynamic language
    LocalizedApp {
        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = large,
                    end = large,
                )
//                .pointerInput(Unit) {
//                    detectDragGestures { change, dragAmount ->
//                        change.consume()
//                        coroutineScope.launch {
//                            // Multiply by 2.5 for more natural feeling scroll speed
//                            listState.animateScrollBy(-dragAmount.y * 10f)
//                        }
//                    }
//                }
                ,
            verticalArrangement = Arrangement.spacedBy(large)
        ) {

            item {
                Spacer(Modifier.height(small))
            }

            item {
                Text(
                    text = thekr.text,
                    style = textStyle,
                    fontFamily = if (thekr.basmlaType != 0) uthmanicScript() else hacenTunisiaLt(),
                )
            }
            if (categoryDetails.value.fadlList.any { it.thekrId == counterUiState.currentThekrInstance.value.id }) {
                val fadlList =
                    categoryDetails.value.fadlList.filter { it.thekrId == counterUiState.currentThekrInstance.value.id }
                items(fadlList) { fadl ->
                    Text(
                        text = fadl.fadl,
                        style = textStyle,
                        lineHeight = if (thekr.basmlaType != 0) (settingsDetails.fontSize + 28).sp
                        else (settingsDetails.fontSize + 16).sp,
                        color = thekrColors.fadlText
                    )
                }
            }
            item {
                Spacer(Modifier.height(360.dp))
            }
        }
    }
}