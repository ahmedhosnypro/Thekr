package com.thekr.ui.home.bar.top

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.ui.values.Dimensions.medium
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.theme.ZekrTheme
import com.thekr.ui.component.RtlView
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TopBarHeaderControls(
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val zekrColors = ZekrTheme.colors(settingsDetails)
    CardDefaults
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = medium, end = medium)
            .border(
                color = zekrColors.secondaryHeaderBorder,
                width = 2.dp,
                shape = MaterialTheme.shapes.small
            ), colors = CardDefaults.cardColors(
            containerColor = zekrColors.secondaryHeaderBackground,
            contentColor = zekrColors.onSecondaryHeader
        ),
        shape = MaterialTheme.shapes.small
    ) {
        content()
    }
}

@Composable
fun HeaderControlCard(
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val zekrColors = ZekrTheme.colors(settingsDetails)
    Card(
        modifier = modifier
            .border(
                color = zekrColors.secondaryHeaderBorder,
                width = 2.dp,
                shape = MaterialTheme.shapes.small
            ), colors = CardDefaults.cardColors(
            containerColor = zekrColors.secondaryHeaderBackground,
            contentColor = zekrColors.onSecondaryHeader
        ),
        shape = MaterialTheme.shapes.small
    ) {
        content()
    }
}

@Preview
@Composable
fun TopBarHeaderControlsPreview() {
    AppTheme(themeMode = ThemeMode.Light) {
        Surface {
            RtlView {
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
            RtlView {
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
    RtlView {
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