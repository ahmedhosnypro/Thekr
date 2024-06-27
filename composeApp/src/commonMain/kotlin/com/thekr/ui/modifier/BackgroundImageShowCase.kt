package com.thekr.ui.modifier

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlignHorizontalCenter
import androidx.compose.material.icons.filled.AlignVerticalBottom
import androidx.compose.material.icons.filled.AlignVerticalCenter
import androidx.compose.material.icons.filled.AlignVerticalTop
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.SwipeLeft
import androidx.compose.material.icons.filled.SwipeVertical
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.thekr.data.proto.ThemeMode
import com.thekr.resources.Res
import com.thekr.resources.black_hex_pattern
import com.thekr.resources.block
import com.thekr.resources.golden_vector
import com.thekr.resources.mono_vector
import com.thekr.resources.wood
import com.thekr.ui.component.MultiLang
import com.thekr.ui.component.bottomBorder
import com.thekr.ui.theme.AppTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun BackgroundImageShowCase() {
    val contentScale: MutableState<ContentScale> = remember { mutableStateOf(ContentScale.None) }
    val drawableResource = remember { mutableStateOf(Res.drawable.golden_vector) }
    val shape = remember { mutableStateOf(RectangleShape) }
    val alignment = remember { mutableStateOf(Alignment.TopStart) }
    val repeat = remember { mutableStateOf(BackgroundRepeat.NoRepeat) }
    val alpha = remember { mutableFloatStateOf(1f) }
    val colorFilter: MutableState<ColorFilter?> = remember { mutableStateOf(null) }
    val drawBehind: MutableState<ContentDrawScope.() -> Unit> = remember { mutableStateOf({}) }
    val drawFront: MutableState<ContentDrawScope.() -> Unit> = remember { mutableStateOf({}) }

    val layoutDirection = remember { mutableStateOf(LayoutDirection.Ltr) }

    AppTheme (
        themeMode = ThemeMode.Dark,
    ){
        Surface {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                        .bottomBorder(Color.Black, 1f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LayoutDirection(layoutDirection)
                    Alpha(alpha, Modifier.fillMaxWidth(.5f))
                }

                LazyVerticalGrid(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    columns = GridCells.Adaptive(200.dp)
                ) {
                    item { Images(drawableResource) }
                    item { Scale(contentScale) }
                    item { Alignments(alignment) }
                    item { Repeats(repeat) }
//                    item { Shapes(shape) }
                }
                MultiLang(layoutDirection.value) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .backgroundImage(
                                painter = painterResource(drawableResource.value),
                                contentScale = contentScale.value,
                                shape = shape.value,
                                alignment = alignment.value,
                                repeat = repeat.value,
                                alpha = alpha.value,
                                colorFilter = colorFilter.value,
                                drawBehind = drawBehind.value,
                                drawFront = drawFront.value
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun Alpha(
    alpha: MutableState<Float>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
    ) {
        Text("Alpha:")
        Slider(
            value = alpha.value,
            onValueChange = { alpha.value = it },
        )
    }
}

@Composable
fun LayoutDirection(
    layoutDirection: MutableState<LayoutDirection>,
    modifier: Modifier = Modifier
) {
    val onClick = remember {
        {
            layoutDirection.value =
                if (layoutDirection.value == LayoutDirection.Ltr) LayoutDirection.Rtl
                else LayoutDirection.Ltr
        }
    }
    TextButton(
        modifier = modifier,
        shape = RectangleShape,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            IconButton(onClick) {
                Icon(Icons.Filled.Directions, contentDescription = null)
            }

            Button(onClick) {
                Text(
                    "Layout Direction:    " +
                            if (layoutDirection.value == LayoutDirection.Ltr) "LTR" else "RTL",
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun Scale(
    contentScale: MutableState<ContentScale>
) {
    CustomDropDownMenu(
        label = "Scale",
        value = Scales.entries.find {
            it.value == contentScale.value
        }?.description ?: "",
        dropDownMenu = { expanded ->
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                Scales.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(it.description) },
                        onClick = {
                            contentScale.value = it.value
                            expanded.value = false
                        }
                    )
                }
            }
        }
    )
}


@Composable
fun Shapes(
    shape: MutableState<Shape>
) {
    CustomDropDownMenu(
        label = "Shape (Not implemented yet)",
        value = Shapes.entries.find {
            it.shape == shape.value
        }?.description ?: "",
        dropDownMenu = { expanded ->
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                Shapes.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(it.description) },
                        onClick = {
                            shape.value = it.shape
                            expanded.value = false
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun Images(
    drawableResource: MutableState<DrawableResource>
) {
    CustomDropDownMenu(
        label = "Images",
        value = BackgroundImage.entries.find {
            it.drawableResource == drawableResource.value
        }?.description ?: "",
        dropDownMenu = { expanded ->
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                BackgroundImage.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(it.description) },
                        onClick = {
                            drawableResource.value = it.drawableResource
                            expanded.value = false
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun Alignments(
    alignment: MutableState<Alignment>
) {
    CustomDropDownMenu(
        leadingIcon = {
            Icon(
                when (alignment.value) {
                    Alignment.TopStart -> Icons.Filled.AlignVerticalTop
                    Alignment.TopCenter -> Icons.Filled.AlignVerticalCenter
                    Alignment.TopEnd -> Icons.Filled.AlignVerticalTop
                    Alignment.CenterStart -> Icons.Filled.AlignHorizontalCenter
                    Alignment.Center -> Icons.Filled.AlignHorizontalCenter
                    Alignment.CenterEnd -> Icons.Filled.AlignHorizontalCenter
                    Alignment.BottomStart -> Icons.Filled.AlignVerticalBottom
                    Alignment.BottomCenter -> Icons.Filled.AlignVerticalBottom
                    Alignment.BottomEnd -> Icons.Filled.AlignVerticalBottom
                    else -> Icons.Filled.AlignVerticalCenter
                },
                contentDescription = null,
            )
        },
        label = "Alignment",
        value = Alignments.entries.find {
            it.value == alignment.value
        }?.description ?: "",
        dropDownMenu = { expanded ->
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                Alignments.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(it.description) },
                        onClick = {
                            alignment.value = it.value
                            expanded.value = false
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun Repeats(
    repeat: MutableState<BackgroundRepeat>
) {
    CustomDropDownMenu(
        leadingIcon = {
            Icon(
                when (repeat.value) {
                    BackgroundRepeat.RepeatX -> Icons.Filled.SwipeLeft
                    BackgroundRepeat.RepeatY -> Icons.Filled.SwipeVertical
                    BackgroundRepeat.Repeat -> Icons.Filled.Repeat
                    BackgroundRepeat.NoRepeat -> Icons.Filled.RepeatOne
                },
                contentDescription = null,
            )
        },
        label = "Repeat",
        value = BackgroundRepeats.entries.find {
            it.value == repeat.value
        }?.description ?: "",
        dropDownMenu = { expanded ->
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                BackgroundRepeats.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(it.description) },
                        onClick = {
                            repeat.value = it.value
                            expanded.value = false
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun CustomDropDownMenu(
    label: String,
    value: String,
    dropDownMenu: @Composable (expanded: MutableState<Boolean>) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val expanded = remember { mutableStateOf(false) }
    OutlinedTextField(
        leadingIcon = leadingIcon,
        value = value,
        onValueChange = {},
        trailingIcon = {
            Box(modifier = Modifier.pointerHoverIcon(PointerIcon.Default)) {
                TextButton(
                    onClick = { expanded.value = true }
                ) {
                    Icon(
                        if (expanded.value) Icons.Filled.ArrowDropUp
                        else Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                    )
                }
                dropDownMenu(expanded)
            }
        },
        label = { Text(label) },
    )
}

enum class Scales(val value: ContentScale, val description: String) {
    Crop(ContentScale.Crop, "Crop"),
    FillBounds(ContentScale.FillBounds, "FillBounds"),
    FillHeight(ContentScale.FillHeight, "FillHeight"),
    FillWidth(ContentScale.FillWidth, "FillWidth"),
    Fit(ContentScale.Fit, "Fit"),
    Inside(ContentScale.Inside, "Inside"),
    None(ContentScale.None, "None")
}

enum class BackgroundImage(val drawableResource: DrawableResource, val description: String) {
    Wood(Res.drawable.wood, "Wood"),
    BlackHexPattern(Res.drawable.black_hex_pattern, "Black Hex Pattern"),
    Block(Res.drawable.block, "Block"),
    GoldenVector(Res.drawable.golden_vector, "Golden Vector"),
    MonoVector(Res.drawable.mono_vector, "Mono Vector"),
}

enum class Shapes(val shape: Shape, val description: String) {
    Rectangle(RectangleShape, "Rectangle"),
    Circle(CircleShape, "Circle"),
    Rounded(RoundedCornerShape(5.dp), "Rounded")
}

enum class Alignments(val value: Alignment, val description: String) {
    TopStart(Alignment.TopStart, "TopStart"),
    TopCenter(Alignment.TopCenter, "TopCenter"),
    TopEnd(Alignment.TopEnd, "TopEnd"),
    CenterStart(Alignment.CenterStart, "CenterStart"),
    Center(Alignment.Center, "Center"),
    CenterEnd(Alignment.CenterEnd, "CenterEnd"),
    BottomStart(Alignment.BottomStart, "BottomStart"),
    BottomCenter(Alignment.BottomCenter, "BottomCenter"),
    BottomEnd(Alignment.BottomEnd, "BottomEnd"),
}

enum class BackgroundRepeats(val value: BackgroundRepeat, val description: String) {
    RepeatX(BackgroundRepeat.RepeatX, "RepeatX"),
    RepeatY(BackgroundRepeat.RepeatY, "RepeatY"),
    Repeat(BackgroundRepeat.Repeat, "Repeat"),
    NoRepeat(BackgroundRepeat.NoRepeat, "NoRepeat"),
}