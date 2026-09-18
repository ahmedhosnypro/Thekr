package com.thekr.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.thekr.resources.Kfgqpc_hafs_uthmanic_script_regular
import com.thekr.resources.Res
import com.thekr.resources.droid_kufi
import com.thekr.resources.droid_kufi_bold
import com.thekr.resources.hacen_tunisia_lt
import org.jetbrains.compose.resources.Font

@Composable
fun droidKufi(): FontFamily {
    val regular = Font(
        Res.font.droid_kufi,
    )
    val bold = Font(
        Res.font.droid_kufi_bold,
        weight = FontWeight.Bold,
    )
    return remember(regular, bold) { FontFamily(regular, bold) }
}

@Composable
fun hacenTunisia(): FontFamily {
    val font = Font(
        Res.font.hacen_tunisia_lt,
    )
    return remember(font) { FontFamily(font) }
}

@Composable
fun hacenTunisiaLt(): FontFamily {
    val font = Font(
        Res.font.hacen_tunisia_lt,
    )
    return remember(font) { FontFamily(font) }
}

@Composable
fun uthmanicScript(): FontFamily {
    val font = Font(
        Res.font.Kfgqpc_hafs_uthmanic_script_regular,
    )
    return remember(font) { FontFamily(font) }
}
