//package com.thekr.ui.thekr.entry
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxWithConstraints
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.requiredHeight
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.material.DropdownMenu
//import androidx.compose.material.DropdownMenuItem
//import androidx.compose.material.Icon
//import androidx.compose.material.IconButton
//import androidx.compose.material.Surface
//import androidx.compose.material.TextButton
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material.icons.filled.DarkMode
//import androidx.compose.material.icons.filled.HdrAuto
//import androidx.compose.material.icons.filled.WbSunny
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Slider
//import androidx.compose.material3.SliderDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.drawBehind
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.RectangleShape
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Dialog
//import com.thekr.data.proto.ColorSchemeDetails
//import com.thekr.data.proto.PaletteStyle
//import com.thekr.data.proto.ThemeMode
//import com.thekr.data.settings.SettingsUiState
//import com.thekr.ui.component.MyHarmonyColorPicker
//import com.thekr.ui.theme.AppTheme
//
//
////@Composable
////fun ColorPickerDialog(
////    actions: ThekrEntryActions,
////    showColorPicker: MutableState<Boolean> = mutableStateOf(true),
////    settingsState: SettingsUiState
////) {
////    if (showColorPicker.value) {
////        // todo: use popup
////        Dialog(
////            onDismissRequest = {},
////        ) {
//////            modifier = Modifier.background(MaterialTheme.colorScheme.primary)
////            // to calculate the max width of the color picker
////            BoxWithConstraints(
////                modifier = Modifier
//////                    .fillMaxWidth()
////                    .background(MaterialTheme.colorScheme.primaryContainer)
////                    .padding(32.dp),
////            ) {
////                val maxWidth = this.maxWidth
////                Column(
////                    modifier = Modifier.fillMaxWidth(),
////                    horizontalAlignment = Alignment.CenterHorizontally,
////                    verticalArrangement = Arrangement.spacedBy(16.dp)
////                ) {
////                    BoxWithConstraints(
////                        modifier = Modifier
////                            .fillMaxWidth()
////                            .padding(horizontal = 16.dp, vertical = 8.dp),
////                        contentAlignment = Alignment.Center
////                    ) {
////                        val width = this.maxWidth
////
////                        MaterialColorPalette(
////                            width,
////                            width / 10,
////                            borderColor = MaterialTheme.colorScheme.onPrimaryContainer,
////                        )
////                    }
////
////                    StyleModeContrast(
////                        settingsState = settingsState,
////                        actions = actions
////                    )
////
////                    MyHarmonyColorPicker(
////                        colorSchemeDetails = settingsState.colorSchemeDetails,
////                        size = maxWidth,
////                        onChange = actions.onColorSchemeChange
////                    )
////
////                    AlertDialogButtons(
////                        showColorPicker,
////                        onDismiss = {
////                            showColorPicker.value = false
////                            actions.onThemeDismiss()
////                        },
////                        onDone = actions.onThemeDone
////                    )
////                }
////            }
////        }
////    }
////}
//
//@Composable
//fun StyleModeContrast(
//    settingsState: SettingsUiState,
//    actions: ThekrEntryActions
//) {
//    StyleAndMode(settingsState = settingsState, actions)
//    ContrastSlider(settingsState.colorSchemeDetails, actions)
//}
//
//
//@Composable
//private fun StyleAndMode(
//    settingsState: SettingsUiState,
//    actions: ThekrEntryActions,
//) {
//    val borderColor = MaterialTheme.colorScheme.onPrimaryContainer
//
//    Row(
//        Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        horizontalArrangement = Arrangement.SpaceAround,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        StyleDropDownMenu(settingsState.colorSchemeDetails, borderColor, actions.onColorSchemeChange)
//        LightMode(settingsState.themeMode, actions.onThemeModeChange, borderColor)
//    }
//}
//
//@Composable
//private fun LightMode(
//    themeMode: ThemeMode,
//    onChange: () -> Unit = {},
//    borderColor: Color,
//) {
//    IconButton(
//        onClick = onChange,
//        modifier = Modifier.drawBehind {
//            drawLine(
//                borderColor,
//                start = Offset(0f, size.height),
//                end = Offset(size.width, size.height),
//                strokeWidth = 5f
//            )
//        },
//    ) {
//
//        val icon = when (themeMode) {
//            ThemeMode.Light -> Icons.Filled.WbSunny
//            ThemeMode.Dark -> Icons.Filled.DarkMode
//            else -> Icons.Filled.HdrAuto
//        }
//        Icon(
//            icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer
//        )
//    }
//}
//
//@Composable
//private fun StyleDropDownMenu(
//    colorSchemeDetails: ColorSchemeDetails,
//    borderColor: Color,
//    onChange: (ColorSchemeDetails) -> Unit = {}
//) {
//    var showStyleMenu by rememberSaveable {
//        mutableStateOf(false)
//    }
//
//    val style = colorSchemeDetails.paletteStyle
//    Box {
//        TextButton(modifier = Modifier.drawBehind {
//            drawLine(
//                borderColor,
//                start = Offset(0f, size.height),
//                end = Offset(size.width, size.height),
//                strokeWidth = 5f
//            )
//        }, onClick = { showStyleMenu = true }) {
//            Text(
//                style.name, color = MaterialTheme.colorScheme.onPrimaryContainer
//            )
//            Icon(
//                Icons.Filled.ArrowDropDown,
//                contentDescription = "",
//                tint = MaterialTheme.colorScheme.onPrimaryContainer
//            )
//        }
//        DropdownMenu(
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.primaryContainer)
//                .border(
//                    1.dp, borderColor, RectangleShape
//                ),
//            expanded = showStyleMenu,
//            onDismissRequest = { showStyleMenu = false },
//            scrollState = rememberScrollState()
//        ) {
//            PaletteStyle.entries.filter {
//                it != PaletteStyle.UNRECOGNIZED
//            }.forEach {
//                DropdownMenuItem(onClick = {
//                    onChange(
//                        ColorSchemeDetails.newBuilder()
//                            .setHue(colorSchemeDetails.hue)
//                            .setSaturation(colorSchemeDetails.saturation)
//                            .setValue(colorSchemeDetails.value)
//                            .setAlpha(colorSchemeDetails.alpha)
//                            .setPaletteStyle(it)
//                            .build()
//                    )
//                }) {
//                    Text(
//                        text = it.name, color = MaterialTheme.colorScheme.onPrimaryContainer
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun ContrastSlider(
//    colorSchemeDetails: ColorSchemeDetails,
//    actions: ThekrEntryActions,
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 8.dp, end = 8.dp, top = 8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        Text(
//            text = "Contrast:",
//            modifier = Modifier.padding(horizontal = 16.dp),
//            style = MaterialTheme.typography.bodyLarge,
//            color = MaterialTheme.colorScheme.onPrimaryContainer
//        )
//
//        Slider(
//            modifier = Modifier.requiredHeight(10.dp),
//            value = colorSchemeDetails.contrast.toFloat(),
//            onValueChange = {
//                actions.onColorSchemeChange(
//                    ColorSchemeDetails.newBuilder()
//                        .setHue(colorSchemeDetails.hue)
//                        .setSaturation(colorSchemeDetails.saturation)
//                        .setValue(colorSchemeDetails.value)
//                        .setAlpha(colorSchemeDetails.alpha)
//                        .setPaletteStyle(colorSchemeDetails.paletteStyle)
//                        .setContrast(it.toDouble())
//                        .build()
//                )
//            },
//            colors = SliderDefaults.colors(
//                thumbColor = MaterialTheme.colorScheme.onPrimaryContainer,
//                // slider horizontal bar
//                activeTrackColor = MaterialTheme.colorScheme.onPrimaryContainer,
//                inactiveTrackColor = MaterialTheme.colorScheme.primary,
//            )
//        )
//    }
//}
//
//
//@Composable
//private fun AlertDialogButtons(
//    showColorPicker: MutableState<Boolean>,
//    onDismiss: () -> Unit,
//    onDone: () -> Unit,
//) {
//    Row(
//        modifier = Modifier
//            .padding(16.dp)
//            .fillMaxWidth(),
//        horizontalArrangement = Arrangement.End,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        TextButton(
//            onClick = onDismiss,
//            shape = RectangleShape
//        ) {
//            Text(
//                "Cancel",
//                color = MaterialTheme.colorScheme.onPrimaryContainer
//            )
//        }
//        TextButton(
//            onClick = {
//                showColorPicker.value = false
//                onDone()
//            },
//            shape = RectangleShape
//        ) {
//            Text(
//                "Done",
//                color = MaterialTheme.colorScheme.onPrimaryContainer
//            )
//        }
//    }
//}
//
//@Preview
//@Composable
//fun AlertDialogButtonsPreview() {
//    AppTheme {
//        Surface {
//            AlertDialogButtons(mutableStateOf(true), {}, {})
//        }
//    }
//}
//
//
//@Preview(widthDp = 400)
//@Composable
//fun ColorPickerDialogCompactPreview() {
//    AppTheme {
//        Surface {
//            ColorPickerDialog(
//                actions = ThekrEntryActions(),
//                showColorPicker = mutableStateOf(true),
//                settingsState = SettingsUiState(),
//            )
//        }
//    }
//}
//
//@Preview(widthDp = 700)
//@Composable
//fun ColorPickerDialogMediumPreview() {
//    AppTheme(ThemeMode.Dark) {
//        Surface {
//            ColorPickerDialog(
//                actions = ThekrEntryActions(),
//                showColorPicker = mutableStateOf(true),
//                settingsState = SettingsUiState(),
//            )
//        }
//    }
//}
//
//@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
//@Composable
//fun ColorPickerDialogExpandedPreview() {
//    AppTheme {
//        Surface {
//            ColorPickerDialog(
//                actions = ThekrEntryActions(),
//                showColorPicker = mutableStateOf(true),
//                settingsState = SettingsUiState(),
//            )
//        }
//    }
//}