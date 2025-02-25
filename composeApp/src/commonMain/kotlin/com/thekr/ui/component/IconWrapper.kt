package com.thekr.ui.component

import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun IconWrapper(
    icon: Any,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color? = null
) {
    when (icon) {
        is ImageVector -> Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint ?: Color.Unspecified
        )
        is DrawableResource -> Image(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            modifier = modifier,
            colorFilter = tint?.let { ColorFilter.tint(it) }
        )
    }
}
