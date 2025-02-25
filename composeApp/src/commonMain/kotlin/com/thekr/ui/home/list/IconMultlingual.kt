package com.thekr.ui.home.list

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import com.thekr.util.isRtlLanguage

@Composable
fun LanguageIconMirrored(
    imageVector: ImageVector,
    contentDescription: String?,
    tint: Color,
    languageTage: String,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier.graphicsLayer {
            if (isRtlLanguage(languageTage).not()) {
                scaleX = -1f
            }
        }
    )
}