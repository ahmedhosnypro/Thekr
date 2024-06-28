package com.thekr.ui.modifier

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thekr.data.proto.ThemeMode
import com.thekr.resources.Res
import com.thekr.resources.background1
import com.thekr.resources.black_hex_pattern
import com.thekr.resources.block
import com.thekr.resources.dark_fabric
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
    val drawableResource = remember { mutableStateOf(Res.drawable.background1) }
    val shape = remember { mutableStateOf(RectangleShape) }
    val alignment = remember { mutableStateOf(Alignment.TopStart) }
    val repeat = remember { mutableStateOf(PaintingRepeat.NoRepeat) }
    val alpha = remember { mutableFloatStateOf(1f) }
    val colorFilter: MutableState<ColorFilter?> = remember { mutableStateOf(null) }
    val drawBehind: MutableState<ContentDrawScope.() -> Unit> = remember { mutableStateOf({}) }
    val drawFront: MutableState<ContentDrawScope.() -> Unit> = remember { mutableStateOf({}) }

    val heightFraction = remember { mutableFloatStateOf(1f) }
    val widthFraction = remember { mutableFloatStateOf(1f) }

    val layoutDirection = remember { mutableStateOf(LayoutDirection.Ltr) }

    AppTheme(
        themeMode = ThemeMode.Dark,
    ) {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(8.dp)
            ) {

                BoxWithConstraints {
                    val width = maxWidth
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                            .bottomBorder(Color.Black, 1f),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LayoutDirections(layoutDirection, Modifier.requiredWidth(width / 5))
                        CustomSlider("Alpha:", alpha, Modifier.requiredWidth(width / 5))
                        CustomSlider(
                            "Height ",
                            heightFraction,
                            Modifier.requiredWidth(width / 5),
                            0.3f..1f
                        )
                        CustomSlider("Width ", widthFraction, Modifier.requiredWidth(width / 5))
                    }
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

                Spacer(modifier = Modifier.height(8.dp))

                MultiLang(layoutDirection.value) {
                    val infiniteTransition = rememberInfiniteTransition()

                    // Animate the alpha value between 0.5f and 1f
                    val alpha1 by infiniteTransition.animateFloat(
                        initialValue = 0.1f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 1000),
                            repeatMode = RepeatMode.Reverse
                        )
                    )

                    // Define the neon color
                    val neonColor = Color.Gray
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(widthFraction.value)
                            .fillMaxHeight(heightFraction.value)
                            .verticalScroll(rememberScrollState())
//                            .backgroundImage(
//                                painter = painterResource(drawableResource.value),
//                                contentScale = contentScale.value,
//                                shape = shape.value,
//                                alignment = alignment.value,
//                                repeat = repeat.value,
//                                alpha = alpha.value,
//                                colorFilter = colorFilter.value,
//                                drawBehind = drawBehind.value,
//                                drawFront = drawFront.value
//                            )
                            .paint(
                                painter = painterResource(drawableResource.value),
                                contentScale = contentScale.value,
                                alpha = alpha.value,
                                colorFilter = colorFilter.value,
                                alignment = alignment.value,
                                repeat = repeat.value,
                            )

                    ) {
                        BlogContent()
                    }
                }
            }
        }
    }
}

@Composable
fun BlogContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BlogTitle()
        Spacer(modifier = Modifier.height(8.dp))
        BlogSubtitle()
        Spacer(modifier = Modifier.height(16.dp))
        BlogBodyText()
    }
}

@Composable
fun BlogTitle() {
    Text(
        text = "Welcome to My Blog",
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        textAlign = TextAlign.Center
    )
}


@Composable
fun BlogSubtitle() {
    Text(
        text = "Exploring the beauty of Jetpack Compose",
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Medium,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun BlogBodyText() {
    Text(
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Purus semper eget duis at tellus at urna. Orci dapibus ultrices in iaculis nunc sed augue lacus viverra. Vitae aliquet nec ullamcorper sit amet risus nullam eget. Diam volutpat commodo sed egestas egestas fringilla phasellus faucibus scelerisque. Odio pellentesque diam volutpat commodo sed. Dui nunc mattis enim ut tellus elementum sagittis vitae et. Adipiscing tristique risus nec feugiat in fermentum posuere urna. Ut porttitor leo a diam. Ut pharetra sit amet aliquam id. Ut sem viverra aliquet eget. Iaculis at erat pellentesque adipiscing commodo elit at. Ut sem viverra aliquet eget sit amet tellus cras. Venenatis lectus magna fringilla urna porttitor rhoncus dolor purus non. Imperdiet proin fermentum leo vel orci porta non pulvinar neque. A iaculis at erat pellentesque.\n" +
                "\n" +
                "Sagittis eu volutpat odio facilisis. Massa id neque aliquam vestibulum morbi blandit. Enim nunc faucibus a pellentesque sit amet. Diam volutpat commodo sed egestas egestas fringilla phasellus faucibus scelerisque. Eu volutpat odio facilisis mauris sit amet massa. At volutpat diam ut venenatis tellus. Mauris augue neque gravida in. Tellus mauris a diam maecenas sed. Dui id ornare arcu odio ut sem nulla pharetra diam. Ullamcorper sit amet risus nullam eget felis eget nunc lobortis. Adipiscing enim eu turpis egestas pretium aenean pharetra magna ac. Risus ultricies tristique nulla aliquet. Elit ut aliquam purus sit amet luctus venenatis lectus.\n" +
                "\n" +
                "Id velit ut tortor pretium viverra suspendisse potenti nullam. Ullamcorper a lacus vestibulum sed arcu. In metus vulputate eu scelerisque felis. Laoreet sit amet cursus sit. Mattis vulputate enim nulla aliquet. Sit amet facilisis magna etiam tempor orci eu. Placerat in egestas erat imperdiet sed euismod nisi porta. Orci sagittis eu volutpat odio facilisis mauris. Donec ac odio tempor orci dapibus ultrices in. Ac odio tempor orci dapibus ultrices. Nec feugiat in fermentum posuere. Dis parturient montes nascetur ridiculus mus mauris vitae. Porttitor rhoncus dolor purus non enim praesent elementum. Dolor magna eget est lorem ipsum dolor sit. Mauris a diam maecenas sed enim ut sem viverra aliquet.\n" +
                "\n" +
                "Luctus accumsan tortor posuere ac ut consequat semper. Lectus quam id leo in vitae turpis massa. Augue interdum velit euismod in. Quis lectus nulla at volutpat diam. Mi bibendum neque egestas congue. Bibendum est ultricies integer quis auctor elit. Morbi tristique senectus et netus et malesuada fames. Volutpat sed cras ornare arcu. Aliquet risus feugiat in ante metus dictum at tempor commodo. Lorem dolor sed viverra ipsum nunc. Sed odio morbi quis commodo odio. Lacus vestibulum sed arcu non odio. Tortor at auctor urna nunc id cursus metus aliquam. Non tellus orci ac auctor augue mauris augue. Mattis molestie a iaculis at erat pellentesque. Morbi tristique senectus et netus. Viverra tellus in hac habitasse platea dictumst vestibulum. Cursus risus at ultrices mi tempus imperdiet nulla malesuada pellentesque. Proin nibh nisl condimentum id venenatis a. Vulputate mi sit amet mauris commodo quis.\n" +
                "\n" +
                "Turpis egestas maecenas pharetra convallis posuere morbi leo. Sodales neque sodales ut etiam sit amet nisl. Nunc non blandit massa enim nec dui nunc mattis. Sit amet consectetur adipiscing elit duis tristique sollicitudin nibh. Quam id leo in vitae turpis massa sed elementum tempus. Lectus urna duis convallis convallis tellus id. Sit amet nulla facilisi morbi tempus iaculis urna id. Nibh cras pulvinar mattis nunc sed blandit libero. Nulla at volutpat diam ut. Dolor sit amet consectetur adipiscing. Risus commodo viverra maecenas accumsan lacus vel facilisis. Condimentum mattis pellentesque id nibh tortor id. Eu augue ut lectus arcu. Nunc scelerisque viverra mauris in aliquam sem fringilla ut. Neque convallis a cras semper auctor neque vitae.\n" +
                "\n" +
                "Nunc non blandit massa enim nec. Nullam vehicula ipsum a arcu cursus vitae. Magna etiam tempor orci eu lobortis elementum nibh tellus molestie. Massa tincidunt dui ut ornare lectus sit amet est. Sodales neque sodales ut etiam sit. Turpis tincidunt id aliquet risus feugiat in. Tristique nulla aliquet enim tortor at auctor urna nunc. Metus vulputate eu scelerisque felis imperdiet. Ut placerat orci nulla pellentesque dignissim enim sit amet venenatis. Ultrices vitae auctor eu augue ut. Egestas integer eget aliquet nibh. At risus viverra adipiscing at in tellus. Accumsan lacus vel facilisis volutpat est velit egestas dui id. Ut aliquam purus sit amet luctus venenatis lectus magna. Habitasse platea dictumst quisque sagittis purus sit amet volutpat.\n" +
                "\n" +
                "Volutpat blandit aliquam etiam erat velit. Eget mi proin sed libero enim sed faucibus turpis. Luctus venenatis lectus magna fringilla urna porttitor rhoncus dolor. Lectus sit amet est placerat in egestas erat imperdiet. Diam sollicitudin tempor id eu nisl nunc. Feugiat nisl pretium fusce id. Eget lorem dolor sed viverra ipsum nunc aliquet bibendum. Aliquet nibh praesent tristique magna sit. Turpis cursus in hac habitasse platea dictumst quisque sagittis purus. Fermentum iaculis eu non diam phasellus vestibulum lorem. Et magnis dis parturient montes nascetur.\n" +
                "\n" +
                "Aliquam faucibus purus in massa. At tellus at urna condimentum mattis pellentesque. Scelerisque viverra mauris in aliquam sem fringilla ut morbi. Molestie at elementum eu facilisis sed odio morbi quis. Est ultricies integer quis auctor. Risus pretium quam vulputate dignissim. Ut diam quam nulla porttitor massa id neque aliquam vestibulum. Cum sociis natoque penatibus et magnis dis parturient. Facilisi nullam vehicula ipsum a arcu cursus. Interdum velit euismod in pellentesque massa placerat. Eros in cursus turpis massa tincidunt dui. Tortor condimentum lacinia quis vel eros donec ac. At volutpat diam ut venenatis. Sapien faucibus et molestie ac feugiat sed lectus. At elementum eu facilisis sed odio morbi. Habitant morbi tristique senectus et netus et. Imperdiet proin fermentum leo vel orci porta non pulvinar neque. Sit amet cursus sit amet. Malesuada nunc vel risus commodo. Augue eget arcu dictum varius duis at consectetur.\n" +
                "\n" +
                "Ac tincidunt vitae semper quis. Dignissim sodales ut eu sem integer. Sit amet commodo nulla facilisi. Dignissim sodales ut eu sem integer vitae justo eget. Vehicula ipsum a arcu cursus vitae congue. Nibh praesent tristique magna sit amet purus gravida quis blandit. Ac odio tempor orci dapibus ultrices in iaculis nunc sed. Mauris cursus mattis molestie a iaculis at. Egestas pretium aenean pharetra magna ac placerat vestibulum lectus mauris. Porta lorem mollis aliquam ut porttitor leo a diam. Sed risus ultricies tristique nulla aliquet enim tortor. Leo duis ut diam quam nulla. Diam sollicitudin tempor id eu nisl.\n" +
                "\n" +
                "Tellus cras adipiscing enim eu. Nisi porta lorem mollis aliquam ut porttitor leo. Dis parturient montes nascetur ridiculus mus mauris. Rutrum quisque non tellus orci ac. Donec ac odio tempor orci dapibus ultrices in iaculis nunc. Dui accumsan sit amet nulla. Lacinia at quis risus sed vulputate odio ut enim blandit. Mollis aliquam ut porttitor leo. Neque aliquam vestibulum morbi blandit. Volutpat maecenas volutpat blandit aliquam etiam erat velit. At erat pellentesque adipiscing commodo elit at imperdiet. Mattis pellentesque id nibh tortor id aliquet lectus proin nibh. Senectus et netus et malesuada.\n" +
                "\n" +
                "Interdum consectetur libero id faucibus nisl tincidunt. Vitae semper quis lectus nulla at. Leo integer malesuada nunc vel risus commodo viverra maecenas accumsan. Sed cras ornare arcu dui vivamus arcu felis. Arcu cursus euismod quis viverra nibh cras pulvinar mattis. Nec feugiat in fermentum posuere urna nec tincidunt. Lobortis scelerisque fermentum dui faucibus in ornare quam. Sit amet est placerat in egestas erat. Ut sem viverra aliquet eget. Id eu nisl nunc mi ipsum faucibus vitae aliquet. Sociis natoque penatibus et magnis dis parturient montes. Lobortis scelerisque fermentum dui faucibus in ornare quam viverra. Cras semper auctor neque vitae tempus quam. Vivamus arcu felis bibendum ut tristique et egestas quis ipsum. Sed euismod nisi porta lorem mollis aliquam ut porttitor. Cras tincidunt lobortis feugiat vivamus.\n" +
                "\n" +
                "Lacus sed turpis tincidunt id aliquet risus feugiat in ante. Massa ultricies mi quis hendrerit dolor magna eget est. Tellus elementum sagittis vitae et leo. Est velit egestas dui id ornare arcu. At volutpat diam ut venenatis tellus in. Posuere sollicitudin aliquam ultrices sagittis orci a. Morbi tempus iaculis urna id volutpat lacus laoreet. Metus aliquam eleifend mi in nulla posuere sollicitudin aliquam ultrices. Penatibus et magnis dis parturient montes nascetur ridiculus mus mauris. Sed id semper risus in hendrerit gravida rutrum quisque. Purus sit amet volutpat consequat mauris nunc congue. Magna eget est lorem ipsum dolor sit amet consectetur. At elementum eu facilisis sed odio morbi quis. Turpis in eu mi bibendum neque egestas congue quisque. Fringilla est ullamcorper eget nulla facilisi etiam. Viverra vitae congue eu consequat ac felis donec et. Ullamcorper sit amet risus nullam eget felis eget. Mattis aliquam faucibus purus in massa tempor. Nunc mi ipsum faucibus vitae aliquet.\n" +
                "\n" +
                "Volutpat maecenas volutpat blandit aliquam etiam erat velit scelerisque in. Dignissim cras tincidunt lobortis feugiat vivamus at augue. Sed id semper risus in hendrerit gravida rutrum quisque non. Quis eleifend quam adipiscing vitae proin sagittis nisl. Morbi quis commodo odio aenean sed adipiscing diam donec. Ut sem nulla pharetra diam sit. Pellentesque eu tincidunt tortor aliquam. At ultrices mi tempus imperdiet nulla malesuada pellentesque elit eget. Commodo quis imperdiet massa tincidunt nunc. Elementum curabitur vitae nunc sed velit dignissim sodales. In hac habitasse platea dictumst vestibulum rhoncus. Gravida rutrum quisque non tellus. Urna nec tincidunt praesent semper feugiat nibh. Amet risus nullam eget felis. Tristique magna sit amet purus gravida quis. Nascetur ridiculus mus mauris vitae ultricies leo integer malesuada nunc. Tempus urna et pharetra pharetra massa massa ultricies mi quis.\n" +
                "\n" +
                "Urna nunc id cursus metus. Sed enim ut sem viverra aliquet eget sit amet tellus. Nisl nisi scelerisque eu ultrices vitae auctor eu augue ut. Auctor urna nunc id cursus. Luctus venenatis lectus magna fringilla. Amet mauris commodo quis imperdiet massa tincidunt. Enim ut tellus elementum sagittis vitae et leo duis. Egestas congue quisque egestas diam. Venenatis cras sed felis eget velit aliquet sagittis id consectetur. Imperdiet proin fermentum leo vel. Elementum pulvinar etiam non quam lacus suspendisse faucibus interdum posuere. Maecenas volutpat blandit aliquam etiam erat velit scelerisque.\n" +
                "\n" +
                "A pellentesque sit amet porttitor eget dolor morbi non arcu. Senectus et netus et malesuada fames ac turpis egestas. Euismod quis viverra nibh cras. Lectus vestibulum mattis ullamcorper velit sed ullamcorper. Lacus sed turpis tincidunt id aliquet risus feugiat in ante. Nibh sit amet commodo nulla. Lobortis mattis aliquam faucibus purus in massa tempor. Quis enim lobortis scelerisque fermentum. Felis bibendum ut tristique et. Nibh venenatis cras sed felis eget velit aliquet. Leo vel fringilla est ullamcorper eget nulla facilisi. Rutrum tellus pellentesque eu tincidunt tortor aliquam. Elit scelerisque mauris pellentesque pulvinar pellentesque. Et malesuada fames ac turpis. Volutpat est velit egestas dui id ornare arcu odio. Nibh sed pulvinar proin gravida hendrerit lectus. Nisl suscipit adipiscing bibendum est ultricies integer quis auctor elit. Tortor at risus viverra adipiscing at in tellus integer. Quam lacus suspendisse faucibus interdum posuere lorem ipsum.\n" +
                "\n" +
                "Blandit volutpat maecenas volutpat blandit aliquam etiam. Quisque non tellus orci ac auctor augue. Id semper risus in hendrerit. In nibh mauris cursus mattis. Adipiscing enim eu turpis egestas pretium aenean pharetra. Fusce id velit ut tortor pretium viverra suspendisse potenti nullam. Etiam sit amet nisl purus in mollis. Elementum curabitur vitae nunc sed velit dignissim. Enim sed faucibus turpis in eu mi. Ut venenatis tellus in metus vulputate eu scelerisque. Velit sed ullamcorper morbi tincidunt ornare massa eget egestas purus. Sit amet porttitor eget dolor morbi. At lectus urna duis convallis. Augue lacus viverra vitae congue eu consequat ac felis donec. Facilisis mauris sit amet massa vitae tortor. Arcu vitae elementum curabitur vitae nunc sed. Vel orci porta non pulvinar neque laoreet suspendisse interdum. Vitae tortor condimentum lacinia quis vel eros donec ac odio.\n" +
                "\n" +
                "Sed velit dignissim sodales ut eu sem. Faucibus interdum posuere lorem ipsum dolor sit amet consectetur. Facilisi nullam vehicula ipsum a arcu cursus vitae congue. Consectetur a erat nam at lectus urna duis convallis. Sodales ut etiam sit amet nisl purus in. Blandit massa enim nec dui nunc. Phasellus vestibulum lorem sed risus ultricies tristique nulla aliquet. Nullam ac tortor vitae purus faucibus ornare suspendisse sed nisi. Aliquet bibendum enim facilisis gravida neque. Arcu dui vivamus arcu felis bibendum ut tristique et. Hendrerit dolor magna eget est lorem ipsum dolor. Quam pellentesque nec nam aliquam sem et. Pellentesque id nibh tortor id aliquet lectus proin. Viverra maecenas accumsan lacus vel facilisis volutpat est velit. Aliquam ultrices sagittis orci a.\n" +
                "\n" +
                "Dignissim cras tincidunt lobortis feugiat vivamus at. Pulvinar etiam non quam lacus. Arcu ac tortor dignissim convallis aenean. Tincidunt augue interdum velit euismod in pellentesque massa placerat. Lorem dolor sed viverra ipsum nunc aliquet. Neque sodales ut etiam sit amet. Eget nunc lobortis mattis aliquam faucibus purus. Nulla aliquet porttitor lacus luctus accumsan tortor posuere ac. Natoque penatibus et magnis dis parturient montes nascetur ridiculus mus. Ullamcorper sit amet risus nullam eget. Morbi leo urna molestie at elementum eu. Ipsum dolor sit amet consectetur adipiscing. Quam quisque id diam vel quam.\n" +
                "\n" +
                "Tortor vitae purus faucibus ornare suspendisse sed nisi lacus. Congue eu consequat ac felis donec. Arcu risus quis varius quam quisque id diam vel. Tellus at urna condimentum mattis pellentesque id nibh tortor id. Blandit massa enim nec dui nunc mattis. Sed turpis tincidunt id aliquet risus. Fermentum iaculis eu non diam phasellus vestibulum lorem sed risus. Mollis nunc sed id semper risus in hendrerit gravida rutrum. In hendrerit gravida rutrum quisque non tellus orci ac auctor. Blandit aliquam etiam erat velit scelerisque in dictum non consectetur. Cras ornare arcu dui vivamus arcu felis bibendum ut tristique. Sed risus ultricies tristique nulla aliquet enim. Enim praesent elementum facilisis leo vel fringilla est ullamcorper eget. Ut tristique et egestas quis ipsum suspendisse ultrices. Vulputate ut pharetra sit amet aliquam. Aenean euismod elementum nisi quis eleifend. Sed turpis tincidunt id aliquet risus. Amet justo donec enim diam vulputate ut pharetra sit amet.\n" +
                "\n" +
                "Lectus urna duis convallis convallis tellus id interdum. Feugiat nibh sed pulvinar proin. Eget lorem dolor sed viverra ipsum nunc aliquet. Ipsum a arcu cursus vitae congue mauris rhoncus. Ullamcorper eget nulla facilisi etiam dignissim diam quis enim lobortis. Vitae elementum curabitur vitae nunc sed velit dignissim sodales ut. Amet est placerat in egestas erat. Et pharetra pharetra massa massa ultricies mi quis hendrerit dolor. Id cursus metus aliquam eleifend mi in nulla posuere. Elit ut aliquam purus sit amet. Sit amet cursus sit amet dictum sit amet. Varius duis at consectetur lorem donec massa sapien faucibus. Quisque non tellus orci ac auctor augue mauris augue. Neque volutpat ac tincidunt vitae. Suspendisse in est ante in nibh mauris cursus. Posuere ac ut consequat semper. Nunc eget lorem dolor sed viverra ipsum. Dolor sit amet consectetur adipiscing elit duis tristique sollicitudin nibh.\n" +
                "\n" +
                "Bibendum arcu vitae elementum curabitur vitae nunc. Massa tempor nec feugiat nisl pretium fusce id velit. A scelerisque purus semper eget. Elementum tempus egestas sed sed risus. Elementum facilisis leo vel fringilla est ullamcorper eget nulla facilisi. Vehicula ipsum a arcu cursus vitae congue mauris. Sed cras ornare arcu dui vivamus arcu felis bibendum ut. Lectus sit amet est placerat in egestas erat. Diam donec adipiscing tristique risus nec feugiat. Vel turpis nunc eget lorem dolor. In pellentesque massa placerat duis ultricies lacus. Quis hendrerit dolor magna eget est lorem ipsum dolor. Mattis nunc sed blandit libero volutpat sed cras ornare. Et molestie ac feugiat sed lectus vestibulum. Dis parturient montes nascetur ridiculus mus mauris vitae.\n" +
                "\n" +
                "Posuere sollicitudin aliquam ultrices sagittis orci a scelerisque. Lorem ipsum dolor sit amet consectetur adipiscing elit ut. In pellentesque massa placerat duis. Velit ut tortor pretium viverra. Urna cursus eget nunc scelerisque viverra mauris. Habitasse platea dictumst quisque sagittis purus sit amet volutpat consequat. Id donec ultrices tincidunt arcu non sodales neque sodales. At imperdiet dui accumsan sit amet. Vestibulum mattis ullamcorper velit sed ullamcorper morbi tincidunt. Eu nisl nunc mi ipsum. Commodo elit at imperdiet dui accumsan sit. Augue interdum velit euismod in pellentesque massa placerat duis.\n" +
                "\n" +
                "Rutrum quisque non tellus orci ac. Porta nibh venenatis cras sed. Habitasse platea dictumst quisque sagittis purus sit amet. Odio euismod lacinia at quis risus sed. Sagittis aliquam malesuada bibendum arcu vitae elementum curabitur vitae nunc. Volutpat est velit egestas dui id ornare arcu odio ut. In ante metus dictum at tempor commodo ullamcorper a. In cursus turpis massa tincidunt. Lectus magna fringilla urna porttitor rhoncus dolor. Nulla facilisi nullam vehicula ipsum a arcu cursus. Porttitor leo a diam sollicitudin tempor id. Scelerisque in dictum non consectetur a erat nam at lectus. Ac placerat vestibulum lectus mauris. Porta nibh venenatis cras sed felis eget. Mi tempus imperdiet nulla malesuada pellentesque elit eget gravida. Urna id volutpat lacus laoreet non curabitur gravida. Facilisis magna etiam tempor orci eu. Sed pulvinar proin gravida hendrerit.\n" +
                "\n" +
                "Eget duis at tellus at urna condimentum. Congue quisque egestas diam in. Risus viverra adipiscing at in. Et leo duis ut diam quam. Vel elit scelerisque mauris pellentesque pulvinar. Quam nulla porttitor massa id neque aliquam. Eleifend quam adipiscing vitae proin sagittis. Faucibus et molestie ac feugiat sed lectus vestibulum mattis ullamcorper. Volutpat odio facilisis mauris sit amet. Ut eu sem integer vitae justo eget. Adipiscing tristique risus nec feugiat in fermentum posuere.\n" +
                "\n" +
                "In mollis nunc sed id semper risus in hendrerit gravida. Sed turpis tincidunt id aliquet risus feugiat in ante. Nisi lacus sed viverra tellus in hac. Auctor neque vitae tempus quam pellentesque nec nam aliquam. Sagittis vitae et leo duis ut diam quam. Turpis egestas sed tempus urna et pharetra pharetra. Adipiscing at in tellus integer feugiat scelerisque varius morbi. Et molestie ac feugiat sed lectus. Rhoncus urna neque viverra justo nec ultrices dui. Pharetra vel turpis nunc eget lorem dolor sed viverra. Nisi vitae suscipit tellus mauris a diam. Et odio pellentesque diam volutpat commodo sed. Quis blandit turpis cursus in hac habitasse. Urna duis convallis convallis tellus id interdum velit laoreet id. Aliquet porttitor lacus luctus accumsan tortor. Consequat id porta nibh venenatis. Volutpat consequat mauris nunc congue nisi vitae. Metus dictum at tempor commodo ullamcorper a lacus vestibulum sed. Ac tortor vitae purus faucibus ornare suspendisse sed nisi. Posuere urna nec tincidunt praesent.\n" +
                "\n" +
                "Tristique sollicitudin nibh sit amet. Nisi porta lorem mollis aliquam ut porttitor leo. Sed odio morbi quis commodo. Tristique nulla aliquet enim tortor at auctor. Habitant morbi tristique senectus et netus. Donec adipiscing tristique risus nec feugiat in fermentum. In nulla posuere sollicitudin aliquam ultrices. Iaculis urna id volutpat lacus laoreet non curabitur. Nam aliquam sem et tortor consequat id porta. Egestas maecenas pharetra convallis posuere morbi leo. Accumsan tortor posuere ac ut consequat semper viverra nam. Augue lacus viverra vitae congue eu. Risus nullam eget felis eget nunc lobortis mattis aliquam faucibus. Adipiscing bibendum est ultricies integer quis auctor. Orci ac auctor augue mauris augue.\n" +
                "\n" +
                "Eget mi proin sed libero. Gravida dictum fusce ut placerat orci nulla pellentesque dignissim enim. Sodales ut eu sem integer. Tempus quam pellentesque nec nam aliquam. Tincidunt augue interdum velit euismod in pellentesque. Tristique et egestas quis ipsum suspendisse ultrices. Non sodales neque sodales ut etiam sit amet nisl. Fringilla phasellus faucibus scelerisque eleifend donec. In nulla posuere sollicitudin aliquam ultrices sagittis orci a. Gravida cum sociis natoque penatibus et magnis dis parturient. Scelerisque purus semper eget duis at tellus.\n" +
                "\n" +
                "Feugiat in fermentum posuere urna nec tincidunt. Lacus vestibulum sed arcu non odio. Habitant morbi tristique senectus et netus. Mollis aliquam ut porttitor leo a diam. Justo eget magna fermentum iaculis eu non diam phasellus. Amet cursus sit amet dictum sit. Ornare massa eget egestas purus viverra accumsan in nisl nisi. A scelerisque purus semper eget duis. In aliquam sem fringilla ut morbi tincidunt augue. In dictum non consectetur a erat nam. Libero enim sed faucibus turpis in eu mi bibendum. Placerat duis ultricies lacus sed turpis. Nisl condimentum id venenatis a.\n" +
                "\n" +
                "Cras adipiscing enim eu turpis egestas. Diam vulputate ut pharetra sit amet aliquam id diam maecenas. Integer malesuada nunc vel risus commodo viverra maecenas. Enim facilisis gravida neque convallis a cras semper auctor neque. Tellus in hac habitasse platea dictumst. Facilisis mauris sit amet massa vitae tortor condimentum lacinia quis. Morbi tristique senectus et netus. Orci eu lobortis elementum nibh tellus. Tortor id aliquet lectus proin nibh nisl condimentum id venenatis. Egestas sed sed risus pretium.\n" +
                "\n" +
                "Risus sed vulputate odio ut enim blandit volutpat. Tortor condimentum lacinia quis vel eros donec. Diam in arcu cursus euismod. Sapien eget mi proin sed libero enim. Dui sapien eget mi proin sed libero. Sem et tortor consequat id porta nibh venenatis cras. Nec sagittis aliquam malesuada bibendum arcu vitae elementum curabitur. Nibh ipsum consequat nisl vel pretium lectus quam id. Eget egestas purus viverra accumsan in nisl nisi scelerisque eu. Cras tincidunt lobortis feugiat vivamus at augue eget arcu dictum. Sit amet purus gravida quis blandit turpis cursus. Morbi blandit cursus risus at ultrices mi tempus. Non quam lacus suspendisse faucibus interdum posuere lorem ipsum dolor.\n" +
                "\n" +
                "Neque laoreet suspendisse interdum consectetur libero id faucibus nisl. Sed pulvinar proin gravida hendrerit lectus. Id semper risus in hendrerit gravida rutrum. Ut ornare lectus sit amet. Amet cursus sit amet dictum sit amet justo donec enim. Nibh tellus molestie nunc non. Ipsum a arcu cursus vitae congue mauris rhoncus. Risus feugiat in ante metus dictum at tempor. Aliquam faucibus purus in massa tempor nec feugiat. Proin sed libero enim sed. Condimentum id venenatis a condimentum vitae sapien pellentesque. Ipsum suspendisse ultrices gravida dictum fusce ut placerat. Elit sed vulputate mi sit amet mauris commodo quis. Elit eget gravida cum sociis natoque. Dui id ornare arcu odio ut sem nulla pharetra. Porta nibh venenatis cras sed felis eget. Elementum facilisis leo vel fringilla est. Urna nec tincidunt praesent semper feugiat nibh. Habitant morbi tristique senectus et netus et malesuada.\n" +
                "\n" +
                "Sodales ut eu sem integer vitae justo. Nibh sit amet commodo nulla. Elementum pulvinar etiam non quam lacus suspendisse faucibus interdum posuere. Facilisis gravida neque convallis a cras semper auctor. Cum sociis natoque penatibus et magnis dis. Odio pellentesque diam volutpat commodo sed egestas egestas. Magna fringilla urna porttitor rhoncus dolor purus. Vestibulum rhoncus est pellentesque elit ullamcorper dignissim cras tincidunt. Ipsum consequat nisl vel pretium lectus quam. Hac habitasse platea dictumst vestibulum rhoncus est. Pellentesque nec nam aliquam sem et tortor. Iaculis at erat pellentesque adipiscing commodo elit at. Integer malesuada nunc vel risus commodo viverra. Euismod nisi porta lorem mollis aliquam ut porttitor. Laoreet sit amet cursus sit amet dictum sit amet justo. Urna molestie at elementum eu facilisis sed odio. Dui id ornare arcu odio ut sem. Augue interdum velit euismod in. Nisi scelerisque eu ultrices vitae auctor eu augue ut. Nascetur ridiculus mus mauris vitae ultricies.\n" +
                "\n" +
                "Tempor id eu nisl nunc mi ipsum. Egestas egestas fringilla phasellus faucibus scelerisque eleifend donec. Ut morbi tincidunt augue interdum velit euismod in pellentesque. Fermentum posuere urna nec tincidunt praesent semper. Urna neque viverra justo nec. Morbi blandit cursus risus at. Auctor urna nunc id cursus. Libero enim sed faucibus turpis in. Vel pharetra vel turpis nunc eget lorem dolor. Auctor augue mauris augue neque gravida in fermentum et. Quisque non tellus orci ac auctor augue mauris augue. Lacus viverra vitae congue eu consequat ac. Nisl suscipit adipiscing bibendum est ultricies integer quis auctor elit. Duis at tellus at urna condimentum mattis pellentesque. Phasellus vestibulum lorem sed risus ultricies. Dictum fusce ut placerat orci. Morbi quis commodo odio aenean sed. Vitae suscipit tellus mauris a.\n" +
                "\n" +
                "Velit dignissim sodales ut eu. Nisl condimentum id venenatis a condimentum vitae sapien pellentesque habitant. Posuere lorem ipsum dolor sit amet consectetur adipiscing. Nunc scelerisque viverra mauris in aliquam sem fringilla ut morbi. Et magnis dis parturient montes nascetur ridiculus mus mauris vitae. Sit amet facilisis magna etiam tempor orci eu. Sit amet nisl purus in mollis nunc sed id. Tortor id aliquet lectus proin nibh nisl. Lectus arcu bibendum at varius vel. Pellentesque pulvinar pellentesque habitant morbi tristique. Pretium fusce id velit ut tortor pretium. Non enim praesent elementum facilisis leo vel. Tortor posuere ac ut consequat semper viverra nam. Faucibus ornare suspendisse sed nisi. Massa tincidunt dui ut ornare lectus sit amet est placerat.\n" +
                "\n" +
                "Massa massa ultricies mi quis hendrerit. Viverra justo nec ultrices dui sapien eget mi proin sed. Aenean sed adipiscing diam donec adipiscing tristique risus nec. Id ornare arcu odio ut sem. Eget nunc lobortis mattis aliquam faucibus purus in. Lectus arcu bibendum at varius vel pharetra vel turpis. Enim nec dui nunc mattis. Etiam sit amet nisl purus in mollis nunc sed. Nisi est sit amet facilisis magna etiam tempor orci. Elit ullamcorper dignissim cras tincidunt lobortis feugiat vivamus at. Non nisi est sit amet facilisis magna etiam tempor. Sapien pellentesque habitant morbi tristique senectus et netus et. A arcu cursus vitae congue mauris rhoncus. Commodo sed egestas egestas fringilla phasellus faucibus scelerisque eleifend. Id ornare arcu odio ut sem nulla pharetra.\n" +
                "\n" +
                "Duis ut diam quam nulla porttitor. Est pellentesque elit ullamcorper dignissim cras tincidunt lobortis. Mauris augue neque gravida in fermentum. Vitae ultricies leo integer malesuada nunc. Bibendum ut tristique et egestas. Montes nascetur ridiculus mus mauris vitae. Nunc vel risus commodo viverra maecenas accumsan lacus vel facilisis. Tristique sollicitudin nibh sit amet. Aliquam vestibulum morbi blandit cursus risus at ultrices. Id aliquet risus feugiat in ante metus dictum at. Sit amet consectetur adipiscing elit duis tristique sollicitudin nibh sit. Lacus laoreet non curabitur gravida arcu ac tortor dignissim. Fusce id velit ut tortor pretium viverra suspendisse potenti nullam. Pretium viverra suspendisse potenti nullam. Proin nibh nisl condimentum id venenatis a condimentum vitae sapien. Lectus arcu bibendum at varius.\n" +
                "\n" +
                "Risus in hendrerit gravida rutrum quisque non tellus. Nunc lobortis mattis aliquam faucibus purus in. Nibh sit amet commodo nulla facilisi nullam vehicula ipsum a. Bibendum enim facilisis gravida neque convallis a. Eleifend mi in nulla posuere sollicitudin aliquam. Non arcu risus quis varius quam quisque. Faucibus purus in massa tempor. Etiam non quam lacus suspendisse faucibus interdum posuere lorem. Nunc lobortis mattis aliquam faucibus purus. Viverra aliquet eget sit amet tellus. Sit amet facilisis magna etiam tempor. Facilisis volutpat est velit egestas dui id ornare arcu. Ultricies tristique nulla aliquet enim tortor at auctor urna. Viverra nam libero justo laoreet sit amet cursus sit. Amet consectetur adipiscing elit ut aliquam purus sit. Eleifend quam adipiscing vitae proin sagittis. Tristique senectus et netus et malesuada fames ac. Tellus orci ac auctor augue. Consectetur adipiscing elit pellentesque habitant.\n" +
                "\n" +
                "Faucibus interdum posuere lorem ipsum dolor sit. Dolor sit amet consectetur adipiscing elit ut aliquam. Purus gravida quis blandit turpis cursus in hac habitasse. Id donec ultrices tincidunt arcu non sodales neque sodales. Lectus nulla at volutpat diam ut venenatis. Sit amet volutpat consequat mauris nunc congue nisi. Faucibus vitae aliquet nec ullamcorper sit. Vulputate mi sit amet mauris. Elit duis tristique sollicitudin nibh sit amet commodo. Sed odio morbi quis commodo odio aenean sed adipiscing. Sagittis vitae et leo duis ut diam quam. Sagittis eu volutpat odio facilisis mauris. Sit amet volutpat consequat mauris nunc congue nisi. Dictumst quisque sagittis purus sit amet volutpat consequat mauris. Placerat vestibulum lectus mauris ultrices. In vitae turpis massa sed elementum tempus egestas sed. Ac felis donec et odio pellentesque diam.\n" +
                "\n" +
                "Sollicitudin tempor id eu nisl nunc mi ipsum. A diam sollicitudin tempor id eu nisl nunc mi. Sit amet porttitor eget dolor morbi non arcu risus. Risus nullam eget felis eget nunc lobortis mattis aliquam. Amet mauris commodo quis imperdiet massa. Maecenas accumsan lacus vel facilisis volutpat est velit egestas. Neque sodales ut etiam sit amet nisl. Non tellus orci ac auctor augue mauris augue neque gravida. Vestibulum sed arcu non odio. Habitant morbi tristique senectus et. Sollicitudin nibh sit amet commodo nulla facilisi nullam vehicula.\n" +
                "\n" +
                "Viverra vitae congue eu consequat ac. Suspendisse interdum consectetur libero id faucibus nisl tincidunt. Feugiat in fermentum posuere urna nec tincidunt praesent. Molestie a iaculis at erat pellentesque adipiscing commodo. Odio ut sem nulla pharetra diam sit amet nisl suscipit. Viverra suspendisse potenti nullam ac. Eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada. In aliquam sem fringilla ut morbi tincidunt augue interdum velit. In tellus integer feugiat scelerisque varius morbi enim nunc faucibus. Aliquam vestibulum morbi blandit cursus risus at. Et malesuada fames ac turpis. Amet justo donec enim diam vulputate ut pharetra sit. Nisl vel pretium lectus quam id leo. Urna condimentum mattis pellentesque id. Tortor consequat id porta nibh venenatis. Varius vel pharetra vel turpis nunc eget lorem dolor. Felis eget velit aliquet sagittis id consectetur purus ut faucibus. Cras fermentum odio eu feugiat pretium nibh ipsum consequat.\n" +
                "\n" +
                "Pulvinar pellentesque habitant morbi tristique senectus et netus et. Sit amet nulla facilisi morbi tempus iaculis urna. In nulla posuere sollicitudin aliquam ultrices sagittis orci a. Eu lobortis elementum nibh tellus molestie nunc. Tellus integer feugiat scelerisque varius morbi enim. Mi ipsum faucibus vitae aliquet. Consectetur libero id faucibus nisl tincidunt eget nullam non nisi. Eget nullam non nisi est sit amet. Quis commodo odio aenean sed. Et tortor at risus viverra adipiscing. Amet luctus venenatis lectus magna. Nulla facilisi etiam dignissim diam quis enim lobortis scelerisque. Nibh ipsum consequat nisl vel.\n" +
                "\n" +
                "Aliquam vestibulum morbi blandit cursus risus. Arcu bibendum at varius vel pharetra. Diam in arcu cursus euismod quis viverra nibh cras. Leo a diam sollicitudin tempor id eu. Pellentesque id nibh tortor id aliquet lectus proin. Eu tincidunt tortor aliquam nulla facilisi cras fermentum odio. Erat velit scelerisque in dictum. Eu volutpat odio facilisis mauris. Sed sed risus pretium quam vulputate dignissim suspendisse. Rhoncus aenean vel elit scelerisque. Pharetra et ultrices neque ornare aenean euismod. Feugiat sed lectus vestibulum mattis. Tempus urna et pharetra pharetra massa.\n" +
                "\n" +
                "Diam vulputate ut pharetra sit. Tincidunt augue interdum velit euismod in pellentesque massa placerat duis. Erat nam at lectus urna duis convallis convallis tellus. Egestas integer eget aliquet nibh praesent tristique. Euismod in pellentesque massa placerat duis ultricies lacus sed. Sed turpis tincidunt id aliquet risus feugiat. Dictumst vestibulum rhoncus est pellentesque. Consequat id porta nibh venenatis cras sed felis eget. Augue neque gravida in fermentum et sollicitudin ac orci phasellus. Mauris a diam maecenas sed enim. Felis donec et odio pellentesque diam.\n" +
                "\n" +
                "Aliquam faucibus purus in massa tempor nec. Consectetur adipiscing elit duis tristique sollicitudin nibh. Porta nibh venenatis cras sed felis eget. Sit amet commodo nulla facilisi nullam. Rutrum tellus pellentesque eu tincidunt tortor aliquam nulla facilisi. Sed odio morbi quis commodo odio aenean sed adipiscing. At erat pellentesque adipiscing commodo elit at imperdiet dui. Egestas fringilla phasellus faucibus scelerisque eleifend. Arcu vitae elementum curabitur vitae nunc sed. Sed odio morbi quis commodo odio aenean sed adipiscing. Mollis nunc sed id semper risus in. Amet consectetur adipiscing elit ut.\n" +
                "\n" +
                "Donec ultrices tincidunt arcu non sodales neque sodales ut. Donec massa sapien faucibus et. Et tortor at risus viverra. Sit amet facilisis magna etiam. Commodo nulla facilisi nullam vehicula ipsum a arcu cursus vitae. Eu non diam phasellus vestibulum lorem. Sagittis vitae et leo duis ut diam quam nulla. Nulla facilisi nullam vehicula ipsum. Aliquam faucibus purus in massa tempor. Vel pretium lectus quam id leo in vitae. Fermentum posuere urna nec tincidunt praesent semper. Sed nisi lacus sed viverra tellus in hac. Vulputate eu scelerisque felis imperdiet proin fermentum leo vel. Non tellus orci ac auctor augue. Vulputate odio ut enim blandit volutpat maecenas volutpat blandit. Mauris a diam maecenas sed enim. Vitae tempus quam pellentesque nec nam aliquam sem et tortor. At volutpat diam ut venenatis tellus. Et malesuada fames ac turpis egestas sed tempus.\n" +
                "\n" +
                "Mauris ultrices eros in cursus. Leo vel orci porta non. Neque volutpat ac tincidunt vitae semper quis lectus nulla. Molestie ac feugiat sed lectus vestibulum mattis ullamcorper. Tortor at auctor urna nunc id cursus metus aliquam eleifend. Tempor orci dapibus ultrices in iaculis nunc. Eu nisl nunc mi ipsum faucibus vitae. Mattis vulputate enim nulla aliquet porttitor lacus. Tempus iaculis urna id volutpat. Pulvinar proin gravida hendrerit lectus a. Diam phasellus vestibulum lorem sed risus. Diam sollicitudin tempor id eu nisl nunc mi.\n" +
                "\n" +
                "Mauris vitae ultricies leo integer malesuada nunc. Sed nisi lacus sed viverra tellus in. Elit ut aliquam purus sit amet. Velit euismod in pellentesque massa placerat duis ultricies. Cursus euismod quis viverra nibh cras. Diam ut venenatis tellus in metus vulputate eu scelerisque. Purus in massa tempor nec feugiat nisl pretium fusce id. Vitae congue mauris rhoncus aenean vel elit. Elementum sagittis vitae et leo duis ut diam quam nulla. Tincidunt nunc pulvinar sapien et. Convallis tellus id interdum velit laoreet. A diam maecenas sed enim. Urna condimentum mattis pellentesque id nibh tortor id. Senectus et netus et malesuada fames ac turpis egestas. Id nibh tortor id aliquet lectus proin. Amet risus nullam eget felis. Faucibus nisl tincidunt eget nullam non nisi est.\n" +
                "\n" +
                "Leo urna molestie at elementum eu. Tincidunt eget nullam non nisi. Tempor orci eu lobortis elementum. Nunc sed blandit libero volutpat. Porttitor eget dolor morbi non arcu risus quis varius quam. Auctor elit sed vulputate mi. Ut pharetra sit amet aliquam id diam maecenas ultricies mi. Elementum pulvinar etiam non quam lacus suspendisse faucibus. Integer malesuada nunc vel risus commodo viverra. Placerat orci nulla pellentesque dignissim enim sit amet. Congue mauris rhoncus aenean vel elit scelerisque mauris pellentesque. Aliquam sem fringilla ut morbi tincidunt augue. Ut venenatis tellus in metus vulputate eu scelerisque. Elementum nisi quis eleifend quam adipiscing vitae proin sagittis nisl. Cras ornare arcu dui vivamus arcu felis bibendum ut tristique. Faucibus interdum posuere lorem ipsum dolor sit amet consectetur adipiscing.\n" +
                "\n" +
                "Ut porttitor leo a diam sollicitudin tempor id. Elit scelerisque mauris pellentesque pulvinar pellentesque habitant. Sed vulputate mi sit amet mauris commodo quis imperdiet. Quam elementum pulvinar etiam non quam lacus. Morbi tristique senectus et netus. Arcu non sodales neque sodales ut etiam sit amet. Fermentum et sollicitudin ac orci phasellus egestas tellus. Odio tempor orci dapibus ultrices in iaculis nunc sed augue. Dignissim sodales ut eu sem integer vitae justo eget. Sodales neque sodales ut etiam sit amet. Hac habitasse platea dictumst vestibulum rhoncus est pellentesque elit.\n" +
                "\n" +
                "Viverra vitae congue eu consequat ac felis donec et odio. Sed felis eget velit aliquet sagittis id. Dapibus ultrices in iaculis nunc. Nascetur ridiculus mus mauris vitae. Dictum non consectetur a erat nam at lectus urna duis. Tristique magna sit amet purus gravida quis blandit turpis. Pharetra pharetra massa massa ultricies. Purus in mollis nunc sed id. Nisi vitae suscipit tellus mauris a diam maecenas sed. Justo eget magna fermentum iaculis eu non diam phasellus vestibulum. Porttitor lacus luctus accumsan tortor posuere ac ut consequat semper. A diam sollicitudin tempor id eu nisl. Ultrices eros in cursus turpis massa. Turpis massa tincidunt dui ut ornare lectus sit amet. Leo vel fringilla est ullamcorper eget. Donec enim diam vulputate ut pharetra sit amet.\n" +
                "\n" +
                "Mi tempus imperdiet nulla malesuada pellentesque elit. In eu mi bibendum neque. Turpis egestas pretium aenean pharetra. Vulputate eu scelerisque felis imperdiet proin fermentum leo vel. Pellentesque adipiscing commodo elit at imperdiet dui accumsan sit. Tempor orci eu lobortis elementum nibh tellus. Eget arcu dictum varius duis. Morbi quis commodo odio aenean sed adipiscing diam. Id cursus metus aliquam eleifend. Odio morbi quis commodo odio aenean sed adipiscing diam donec. Arcu felis bibendum ut tristique et egestas quis ipsum suspendisse. Risus in hendrerit gravida rutrum quisque non. Vulputate odio ut enim blandit volutpat maecenas volutpat blandit. Eget sit amet tellus cras adipiscing enim. Habitant morbi tristique senectus et netus et malesuada fames ac. Libero volutpat sed cras ornare. Felis eget nunc lobortis mattis aliquam faucibus purus. Nunc sed augue lacus viverra. Facilisi cras fermentum odio eu feugiat pretium nibh ipsum consequat.\n" +
                "\n" +
                "Accumsan in nisl nisi scelerisque eu. Urna neque viverra justo nec ultrices. Nam libero justo laoreet sit. Tellus orci ac auctor augue mauris augue. In est ante in nibh mauris cursus. Pulvinar pellentesque habitant morbi tristique senectus. Adipiscing diam donec adipiscing tristique risus nec. Tincidunt id aliquet risus feugiat in. In nibh mauris cursus mattis. Dictum sit amet justo donec enim. Adipiscing diam donec adipiscing tristique risus nec. Elementum pulvinar etiam non quam lacus suspendisse faucibus.\n" +
                "\n" +
                "Pulvinar pellentesque habitant morbi tristique senectus et netus. Sit amet purus gravida quis blandit turpis cursus. Elementum eu facilisis sed odio morbi quis. Sodales ut eu sem integer vitae justo eget magna. Enim tortor at auctor urna nunc id cursus metus aliquam. Vitae ultricies leo integer malesuada nunc. Eleifend quam adipiscing vitae proin sagittis nisl rhoncus. Integer quis auctor elit sed vulputate mi sit. Ac felis donec et odio. Eget est lorem ipsum dolor sit amet. Cursus vitae congue mauris rhoncus aenean. Vitae turpis massa sed elementum tempus egestas sed. Proin sagittis nisl rhoncus mattis rhoncus urna neque viverra. Commodo elit at imperdiet dui accumsan.\n" +
                "\n" +
                "Non curabitur gravida arcu ac. Diam sollicitudin tempor id eu nisl nunc mi. Urna condimentum mattis pellentesque id. Scelerisque purus semper eget duis at tellus at. Praesent tristique magna sit amet purus gravida. Mauris commodo quis imperdiet massa. Urna porttitor rhoncus dolor purus. Neque sodales ut etiam sit amet nisl purus in. Facilisis volutpat est velit egestas dui id ornare. Laoreet sit amet cursus sit amet dictum sit amet justo. Malesuada bibendum arcu vitae elementum curabitur. Euismod in pellentesque massa placerat duis ultricies lacus sed. Tincidunt dui ut ornare lectus sit amet est placerat. Ipsum dolor sit amet consectetur adipiscing elit. In nibh mauris cursus mattis molestie a. Commodo quis imperdiet massa tincidunt nunc pulvinar sapien et ligula.\n" +
                "\n" +
                "At volutpat diam ut venenatis tellus in metus. Cras pulvinar mattis nunc sed blandit. Sed vulputate mi sit amet mauris commodo quis imperdiet. Pretium quam vulputate dignissim suspendisse in. Eget nulla facilisi etiam dignissim diam quis enim. Ipsum dolor sit amet consectetur adipiscing elit pellentesque. Rutrum tellus pellentesque eu tincidunt. Potenti nullam ac tortor vitae purus faucibus. Elit duis tristique sollicitudin nibh sit. Faucibus in ornare quam viverra orci sagittis eu volutpat odio. Imperdiet sed euismod nisi porta. Quis enim lobortis scelerisque fermentum dui faucibus in ornare. Commodo viverra maecenas accumsan lacus vel facilisis. Convallis tellus id interdum velit laoreet id donec.\n" +
                "\n" +
                "In hendrerit gravida rutrum quisque non tellus. Nunc id cursus metus aliquam eleifend mi. Nunc pulvinar sapien et ligula ullamcorper malesuada proin libero nunc. Eu turpis egestas pretium aenean pharetra magna ac placerat vestibulum. Morbi tristique senectus et netus. Accumsan tortor posuere ac ut. Egestas pretium aenean pharetra magna ac placerat vestibulum. Sed vulputate odio ut enim blandit volutpat maecenas volutpat blandit. Euismod elementum nisi quis eleifend quam. Egestas congue quisque egestas diam. Porttitor massa id neque aliquam vestibulum morbi blandit. Justo eget magna fermentum iaculis eu non diam phasellus vestibulum. Turpis egestas sed tempus urna et pharetra pharetra massa. Tortor condimentum lacinia quis vel eros. Urna porttitor rhoncus dolor purus non enim. Enim tortor at auctor urna nunc id cursus metus aliquam. Non nisi est sit amet. Habitant morbi tristique senectus et netus et.\n" +
                "\n" +
                "Sollicitudin aliquam ultrices sagittis orci. Pellentesque diam volutpat commodo sed egestas egestas. Eget mauris pharetra et ultrices neque ornare aenean euismod elementum. Euismod lacinia at quis risus. Tempus quam pellentesque nec nam aliquam sem et tortor. Vitae nunc sed velit dignissim sodales ut. Molestie ac feugiat sed lectus vestibulum. Sapien faucibus et molestie ac feugiat sed lectus. In egestas erat imperdiet sed euismod nisi porta lorem mollis. Purus non enim praesent elementum facilisis leo vel fringilla est. Commodo sed egestas egestas fringilla phasellus. Quis ipsum suspendisse ultrices gravida. Id diam maecenas ultricies mi eget mauris. At in tellus integer feugiat scelerisque varius morbi enim nunc. Rhoncus mattis rhoncus urna neque viverra justo nec ultrices.\n" +
                "\n" +
                "Cursus metus aliquam eleifend mi in nulla. Sed risus ultricies tristique nulla aliquet enim tortor at. Risus at ultrices mi tempus imperdiet. In metus vulputate eu scelerisque felis imperdiet proin fermentum leo. Semper viverra nam libero justo laoreet. Eget nullam non nisi est. Lectus magna fringilla urna porttitor rhoncus dolor purus. Vitae elementum curabitur vitae nunc sed velit dignissim sodales ut. Adipiscing commodo elit at imperdiet dui accumsan sit amet. Neque vitae tempus quam pellentesque. Duis at tellus at urna condimentum mattis pellentesque id. Dolor sit amet consectetur adipiscing elit. Justo laoreet sit amet cursus sit amet dictum. Dui sapien eget mi proin sed libero enim sed. Nibh praesent tristique magna sit amet purus gravida quis. Phasellus egestas tellus rutrum tellus. Pretium viverra suspendisse potenti nullam ac. Aliquam faucibus purus in massa tempor nec. Tortor at risus viverra adipiscing at.\n" +
                "\n" +
                "Urna porttitor rhoncus dolor purus non. Elementum integer enim neque volutpat ac tincidunt vitae semper quis. Ligula ullamcorper malesuada proin libero nunc consequat interdum. Egestas purus viverra accumsan in nisl. Potenti nullam ac tortor vitae. Phasellus egestas tellus rutrum tellus. Volutpat ac tincidunt vitae semper quis. Diam maecenas ultricies mi eget. Molestie nunc non blandit massa enim nec dui nunc. Aliquam purus sit amet luctus venenatis. Nec feugiat in fermentum posuere. Eget velit aliquet sagittis id. Id nibh tortor id aliquet lectus proin. Sed egestas egestas fringilla phasellus faucibus scelerisque eleifend. Ultrices eros in cursus turpis massa tincidunt. Porttitor leo a diam sollicitudin tempor id eu nisl. Turpis nunc eget lorem dolor sed viverra. Imperdiet dui accumsan sit amet nulla facilisi morbi tempus.\n" +
                "\n" +
                "Dignissim cras tincidunt lobortis feugiat vivamus at augue eget. At quis risus sed vulputate odio ut enim blandit. Eros donec ac odio tempor orci. Sed risus pretium quam vulputate dignissim suspendisse in est. Facilisis mauris sit amet massa vitae. Turpis egestas pretium aenean pharetra magna ac placerat. Nunc sed id semper risus in. Viverra aliquet eget sit amet tellus cras adipiscing enim eu. Id velit ut tortor pretium viverra suspendisse potenti nullam ac. Placerat orci nulla pellentesque dignissim. Mauris cursus mattis molestie a iaculis at. Tempor commodo ullamcorper a lacus vestibulum sed. Gravida arcu ac tortor dignissim convallis aenean et tortor at. Quisque egestas diam in arcu cursus euismod quis viverra nibh. Sagittis purus sit amet volutpat consequat. At in tellus integer feugiat scelerisque.\n" +
                "\n" +
                "Pellentesque elit eget gravida cum sociis natoque. Enim sit amet venenatis urna cursus eget nunc scelerisque viverra. Egestas quis ipsum suspendisse ultrices gravida dictum fusce. Facilisi etiam dignissim diam quis. Praesent semper feugiat nibh sed pulvinar proin gravida hendrerit lectus. Turpis massa tincidunt dui ut. Accumsan sit amet nulla facilisi morbi tempus iaculis. Convallis tellus id interdum velit. Rhoncus mattis rhoncus urna neque viverra justo nec ultrices. Mauris rhoncus aenean vel elit scelerisque mauris pellentesque. Libero justo laoreet sit amet cursus sit. Nunc lobortis mattis aliquam faucibus. Nunc lobortis mattis aliquam faucibus purus in massa tempor nec. Eget nunc lobortis mattis aliquam faucibus purus in massa tempor. Aliquet bibendum enim facilisis gravida neque convallis. Magna etiam tempor orci eu lobortis elementum nibh. Laoreet sit amet cursus sit amet dictum sit amet justo. Morbi enim nunc faucibus a. Mattis enim ut tellus elementum sagittis vitae et leo. Neque convallis a cras semper.\n" +
                "\n" +
                "Tincidunt ornare massa eget egestas purus viverra. Orci sagittis eu volutpat odio facilisis. Ac tortor vitae purus faucibus ornare suspendisse. Aliquet enim tortor at auctor urna. Ac turpis egestas integer eget aliquet nibh praesent tristique magna. Phasellus vestibulum lorem sed risus ultricies tristique nulla aliquet enim. Fames ac turpis egestas integer eget. Amet consectetur adipiscing elit pellentesque habitant morbi. Vel pharetra vel turpis nunc eget. In arcu cursus euismod quis viverra nibh cras pulvinar mattis. Rhoncus est pellentesque elit ullamcorper dignissim cras. Ipsum faucibus vitae aliquet nec. Pellentesque elit eget gravida cum sociis natoque penatibus et magnis. Vestibulum rhoncus est pellentesque elit ullamcorper dignissim cras tincidunt. Facilisis sed odio morbi quis commodo odio. Mauris nunc congue nisi vitae suscipit tellus mauris. Urna porttitor rhoncus dolor purus non enim praesent elementum. Aliquam malesuada bibendum arcu vitae elementum curabitur vitae. Ipsum dolor sit amet consectetur adipiscing.\n" +
                "\n" +
                "Praesent elementum facilisis leo vel fringilla est. Justo laoreet sit amet cursus. Phasellus vestibulum lorem sed risus ultricies tristique nulla. Porttitor eget dolor morbi non arcu risus quis varius. Aliquam sem et tortor consequat id. Tellus integer feugiat scelerisque varius morbi enim. Aliquam ultrices sagittis orci a scelerisque purus. Orci ac auctor augue mauris augue neque. Velit euismod in pellentesque massa placerat duis ultricies lacus. Orci porta non pulvinar neque laoreet suspendisse. Turpis cursus in hac habitasse. Velit ut tortor pretium viverra suspendisse potenti nullam ac tortor. Malesuada fames ac turpis egestas sed tempus urna et pharetra. Egestas egestas fringilla phasellus faucibus scelerisque eleifend donec pretium. Nec feugiat nisl pretium fusce id velit. A diam sollicitudin tempor id eu. Ipsum nunc aliquet bibendum enim facilisis. Diam maecenas sed enim ut sem.\n" +
                "\n" +
                "Sed arcu non odio euismod lacinia at quis. Scelerisque in dictum non consectetur a erat nam at lectus. Vitae et leo duis ut diam quam nulla porttitor. Est ullamcorper eget nulla facilisi etiam dignissim diam. Sed augue lacus viverra vitae congue. Interdum consectetur libero id faucibus nisl tincidunt eget nullam. Dolor sit amet consectetur adipiscing elit ut aliquam purus. Duis at tellus at urna condimentum. Risus in hendrerit gravida rutrum quisque non tellus orci ac. Massa enim nec dui nunc mattis enim.\n" +
                "\n" +
                "Aliquam eleifend mi in nulla posuere sollicitudin aliquam ultrices sagittis. Faucibus a pellentesque sit amet porttitor eget dolor morbi non. Amet mattis vulputate enim nulla aliquet. Viverra vitae congue eu consequat ac felis. Porta lorem mollis aliquam ut porttitor leo a diam. At risus viverra adipiscing at in tellus integer feugiat scelerisque. Sed arcu non odio euismod. Amet cursus sit amet dictum. Tristique senectus et netus et malesuada fames ac turpis. Et netus et malesuada fames ac. Tortor pretium viverra suspendisse potenti nullam ac tortor. Eu consequat ac felis donec et odio. Amet nulla facilisi morbi tempus iaculis urna id volutpat lacus. Consequat mauris nunc congue nisi. Pharetra sit amet aliquam id diam maecenas ultricies mi eget. Egestas fringilla phasellus faucibus scelerisque eleifend donec pretium. Magna eget est lorem ipsum dolor sit amet consectetur adipiscing. Eget velit aliquet sagittis id consectetur purus ut. Quisque sagittis purus sit amet volutpat consequat mauris nunc congue.\n" +
                "\n" +
                "Tortor condimentum lacinia quis vel eros. Pellentesque habitant morbi tristique senectus et. Turpis massa sed elementum tempus egestas sed sed risus. Mauris augue neque gravida in fermentum. Dignissim diam quis enim lobortis scelerisque. Semper quis lectus nulla at volutpat diam. Elit duis tristique sollicitudin nibh sit amet commodo nulla. Praesent semper feugiat nibh sed pulvinar. Nibh sit amet commodo nulla facilisi nullam vehicula ipsum. Diam ut venenatis tellus in. Pharetra vel turpis nunc eget. Enim sit amet venenatis urna. Egestas tellus rutrum tellus pellentesque eu tincidunt tortor aliquam nulla. Libero id faucibus nisl tincidunt eget nullam non nisi est. Morbi enim nunc faucibus a pellentesque sit amet porttitor. Commodo quis imperdiet massa tincidunt nunc pulvinar sapien. Nunc mattis enim ut tellus elementum sagittis vitae. Ultrices sagittis orci a scelerisque purus semper eget duis at. Sit amet commodo nulla facilisi nullam vehicula ipsum a. Dui ut ornare lectus sit amet.",
        style = MaterialTheme.typography.bodyLarge.copy(
            lineHeight = 24.sp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    )
}

@Composable
fun CustomSlider(
    label: String,
    value: MutableState<Float>,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
    ) {
        Text(label)
        Slider(
            value = value.value,
            onValueChange = { value.value = it },
            valueRange = valueRange
        )
    }
}

@Composable
fun LayoutDirections(
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
    paintingRepeat: MutableState<PaintingRepeat>
) {
    CustomDropDownMenu(
        leadingIcon = {
            Icon(
                when (paintingRepeat.value) {
                    PaintingRepeat.RepeatX -> Icons.Filled.SwipeLeft
                    PaintingRepeat.RepeatY -> Icons.Filled.SwipeVertical
                    PaintingRepeat.Repeat -> Icons.Filled.Repeat
                    PaintingRepeat.NoRepeat -> Icons.Filled.RepeatOne
                },
                contentDescription = null,
            )
        },
        label = "Repeat",
        value = BackgroundRepeats.entries.find {
            it.value == paintingRepeat.value
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
                            paintingRepeat.value = it.value
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
    Backgroudn1(Res.drawable.background1, "Background 1"),
    DarkFabric(Res.drawable.dark_fabric, "Dark Fabric"),
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

enum class BackgroundRepeats(val value: PaintingRepeat, val description: String) {
    RepeatX(PaintingRepeat.RepeatX, "RepeatX"),
    RepeatY(PaintingRepeat.RepeatY, "RepeatY"),
    Repeat(PaintingRepeat.Repeat, "Repeat"),
    NoRepeat(PaintingRepeat.NoRepeat, "NoRepeat"),
}