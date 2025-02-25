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
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.resources.Res
import com.thekr.resources.back
import com.thekr.ui.bar.top.ZekrBar
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.home.bar.top.HeaderText
import com.thekr.ui.home.list.ZekrList
import com.thekr.ui.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@OptIn(InternalVoyagerApi::class)
fun CategoryZekrList(
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
            ZekrBar(
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
        ZekrList(
            categoryDetails = category,
            categoryListOnClick = categoryListOnClick,
            modifier = Modifier.padding(innerPadding),
            settingsDetails = settingsDetails,
        )
    }
}


@Preview
@Composable
fun CategoryZekrListPreview() {
    AppTheme {
        Surface {
            LocalizedApp {
                CategoryZekrList(
                    category = mutableStateOf(
                        CategoryDetails(
                            name = "الأذكار اليومية",
//                        zekrList = previewList()
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
fun CategoryZekrListPreviewDark() {
    AppTheme(ThemeMode.Dark) {
        Surface {
            LocalizedApp {
                CategoryZekrList(
                    category = mutableStateOf(
                        CategoryDetails(
                            name = "الأذكار اليومية",
//                        zekrList = previewList(),
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