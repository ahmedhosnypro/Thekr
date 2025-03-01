package com.thekr.ui.home.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.ui.component.LocalizedApp
import com.thekr.ui.theme.AppTheme

@Preview
@Composable
fun TopBarHeaderControlsPreview() {
    AppTheme(themeMode = ThemeMode.Light) {
        Surface {
            LocalizedApp {
                TopBarHeaderControls(
                    settingsDetails = SettingsDetails(
                        themeMode = ThemeMode.Light
                    ),
                    content = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun TopBarHeaderControlsPreviewDark() {
    AppTheme(themeMode = ThemeMode.Dark) {
        Surface {
            LocalizedApp {
                TopBarHeaderControls(
                    settingsDetails = SettingsDetails(
                        themeMode = ThemeMode.Dark
                    ),
                    content = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun HeaderControlCardPreview() {
    LocalizedApp {
        Surface {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AppTheme(themeMode = ThemeMode.Dark) {
                    HeaderControlCard(
                        settingsDetails = SettingsDetails(
                            themeMode = ThemeMode.Dark
                        ),
                        content = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        }
                    )
                }
                AppTheme(themeMode = ThemeMode.Light) {
                    HeaderControlCard(
                        settingsDetails = SettingsDetails(
                            themeMode = ThemeMode.Light
                        ),
                        content = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}