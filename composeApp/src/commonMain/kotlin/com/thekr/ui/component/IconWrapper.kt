package com.thekr.ui.component

import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
@Suppress("ktlint:standard:function-naming")
fun IconWrapper(
    icon: ThekrIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color? = null,
) {
    when (icon) {
        is ThekrIcon.Vector -> Icon(
            imageVector = icon.imageVector,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint ?: Color.Unspecified,
        )
        is ThekrIcon.Drawable -> Image(
            painter = painterResource(icon.drawableResource),
            contentDescription = contentDescription,
            modifier = modifier,
            colorFilter = tint?.let { ColorFilter.tint(it) },
        )
    }
}

@Stable
sealed interface ThekrIcon {
    data class Vector(val imageVector: ImageVector) : ThekrIcon

    data class Drawable(val drawableResource: DrawableResource) : ThekrIcon
}
