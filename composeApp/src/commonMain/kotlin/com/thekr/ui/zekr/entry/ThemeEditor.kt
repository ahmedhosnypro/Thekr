package com.thekr.ui.thekr.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.thekr.data.settings.SettingsDetails


@Composable
fun ThemeEditor(
    actions: ThekrEntryActions,
    settingsState: SettingsDetails,
) {
    val showColorPicker = rememberSaveable {
        mutableStateOf(false)
    }
    // Container for Counter Form theme Editor Button
    Surface(
        onClick = {
            showColorPicker.value = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth, minHeight = TextFieldDefaults.MinHeight
            ),
        shape = RectangleShape,
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        // Row of the label and the button to open the color picker
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Theme",
                modifier = Modifier.weight(0.5f),
            )

            BoxWithConstraints(
                modifier = Modifier
                    .weight(.2f)
                    .requiredHeight(TextFieldDefaults.MinHeight),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .border(
                            width = 1.dp, color = Color.White, shape = RectangleShape
                        )
                        .requiredSize(maxWidth, maxHeight / 2), shape = RectangleShape
                ) {
                    BoxWithConstraints {
                        MaterialColorPalette(
                            maxWidth, maxHeight, borderColor = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }


//            ColorPickerDialog(
//                actions = actions,
//                showColorPicker = showColorPicker,
//                settingsState = settingsState,
//            )
        }
    }
}


@Composable
fun MaterialColorPalette(maxWidth: Dp, height: Dp, borderColor: Color = Color.White) {
    val width by remember {
        derivedStateOf { maxWidth / 16 }
    }
    Row(
        Modifier
            .requiredSize(maxWidth, height)
            .border(
                width = 2.dp, color = borderColor, shape = RectangleShape
            )
    ) {
        MaterialTheme.colorScheme.apply {
            ColorPaletteItem(primary, width, height = height)
            ColorPaletteItem(onPrimary, width, height)
            ColorPaletteItem(primaryContainer, width, height)
            ColorPaletteItem(onPrimaryContainer, width, height)
            ColorPaletteItem(secondary, width, height)
            ColorPaletteItem(onSecondary, width, height)
            ColorPaletteItem(secondaryContainer, width, height)
            ColorPaletteItem(onSecondaryContainer, width, height)
            ColorPaletteItem(tertiary, width, height)
            ColorPaletteItem(onTertiary, width, height)
            ColorPaletteItem(tertiaryContainer, width, height)
            ColorPaletteItem(onTertiaryContainer, width, height)
            ColorPaletteItem(error, width, height)
            ColorPaletteItem(onError, width, height)
            ColorPaletteItem(surface, width, height)
            ColorPaletteItem(onSurface, width, height)
        }
    }
}

@Composable
fun ColorPaletteItem(
    color: Color, width: Dp, height: Dp
) {
    Box(
        Modifier
            .background(color)
            .requiredSize(width, height)
    )
}
