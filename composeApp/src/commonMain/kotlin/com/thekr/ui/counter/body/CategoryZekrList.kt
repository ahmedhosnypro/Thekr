package com.thekr.ui.counter.body

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.internal.BackHandler
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.resources.Res
import com.thekr.resources.back
import com.thekr.ui.component.bar.AppTopBar
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.home.bar.top.HeaderText
import com.thekr.ui.home.list.ThekrList
import com.thekr.ui.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@OptIn(InternalVoyagerApi::class)
fun CategoryThekrList(
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
    category: MutableState<CategoryDetails> = mutableStateOf(CategoryDetails()),
    onNavigateUp: () -> Unit = {},
    canNavigateUp: () -> Boolean = { true },
    categoryListOnClick: (tabIndex: Int) -> Unit = {},
) {
    BackHandler(true) {
        onNavigateUp()
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = { HeaderText(text = category.value.name) },
                navigationIcon = {
                    if (canNavigateUp()) {
                        IconButton(onClick = onNavigateUp) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBackIos,
                                contentDescription = stringResource(Res.string.back),
                            )
                        }
                    }
                },
                settingsDetails = settingsDetails,
            )
        },
    ) { innerPadding ->
        ThekrList(
            categoryDetails = category,
            categoryListOnClick = categoryListOnClick,
            modifier = Modifier.padding(innerPadding),
            settingsDetails = settingsDetails,
        )
    }
}


@Preview
@Composable
fun CategoryThekrListPreview() {
    AppTheme {
        Surface {
            LocalizedApp {
                CategoryThekrList(
                    category = mutableStateOf(
                        CategoryDetails(
                            name = "الأذكار اليومية",
//                        thekrList = previewList()
                        )
                    ),
                    settingsDetails = SettingsDetails()
                )
            }
        }
    }
}

@Preview
@Composable
fun CategoryThekrListPreviewDark() {
    AppTheme(ThemeMode.Dark) {
        Surface {
            LocalizedApp {
                CategoryThekrList(
                    category = mutableStateOf(
                        CategoryDetails(
                            name = "الأذكار اليومية",
//                        thekrList = previewList(),
                        )
                    ),
                    settingsDetails = SettingsDetails(
                        themeMode = ThemeMode.Dark
                    )
                )
            }
        }
    }
}