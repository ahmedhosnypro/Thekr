package com.thekr.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.thekr.resources.Kfgqpc_hafs_uthmanic_script_regular
import com.thekr.resources.Res
import com.thekr.resources.abdo_master_demi_bold
import com.thekr.resources.abdo_master_regular
import com.thekr.resources.alrafidain_art
import com.thekr.resources.arial_bold
import com.thekr.resources.arial_narrow
import com.thekr.resources.arial_regular
import com.thekr.resources.battar_font
import com.thekr.resources.droid_kufi
import com.thekr.resources.droid_kufi_bold
import com.thekr.resources.ge_ss_two_bold
import com.thekr.resources.ge_ss_two_light
import com.thekr.resources.ge_ss_two_medium
import com.thekr.resources.hacen_liner_print_out
import com.thekr.resources.hacen_liner_print_out_light
import com.thekr.resources.hacen_tunisia_lt
import com.thekr.resources.helvetica_world_bold
import com.thekr.resources.jf_flat_regular
import com.thekr.resources.montserrat_bold
import com.thekr.resources.nassim_arabic_regular
import com.thekr.resources.roboto_bold
import com.thekr.resources.roboto_light
import com.thekr.resources.roboto_regular
import com.thekr.resources.uthman_tn_v2
import org.jetbrains.compose.resources.Font

@Composable
fun abdoMaster(): FontFamily {
    val demiBold = Font(
        Res.font.abdo_master_demi_bold,
        weight = FontWeight.Bold,
    )
    val regular = Font(
        Res.font.abdo_master_regular,
    )
    return remember(demiBold, regular) { FontFamily(demiBold, regular) }
}

@Composable
fun alrafidainArt(): FontFamily {
    val font = Font(
        Res.font.alrafidain_art,
    )
    return remember(font) { FontFamily(font) }
}

@Composable
fun arial(): FontFamily {
    val regular = Font(
        Res.font.arial_regular,
    )
    val bold = Font(
        Res.font.arial_bold,
        weight = FontWeight.Bold,
    )
    val narrow = Font(
        Res.font.arial_narrow,
    )
    return remember(regular, bold, narrow) { FontFamily(regular, bold, narrow) }
}

@Composable
fun battar(): FontFamily {
    val font = Font(
        Res.font.battar_font,
    )
    return remember(font) { FontFamily(font) }
}

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
fun geSsTwo(): FontFamily {
    val light = Font(
        Res.font.ge_ss_two_light,
        weight = FontWeight.Light,
    )
    val medium = Font(
        Res.font.ge_ss_two_medium,
        weight = FontWeight.Medium,
    )
    val bold = Font(
        Res.font.ge_ss_two_bold,
        weight = FontWeight.Bold,
    )
    return remember(light, medium, bold) { FontFamily(light, medium, bold) }
}

@Composable
fun hacenLinerPrintOut(): FontFamily {
    val light = Font(
        Res.font.hacen_liner_print_out_light,
        weight = FontWeight.Light,
    )
    val regular = Font(
        Res.font.hacen_liner_print_out,
    )
    return remember(light, regular) { FontFamily(light, regular) }
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
fun helveticaWorld(): FontFamily {
    val font = Font(
        Res.font.helvetica_world_bold,
        weight = FontWeight.Bold,
    )
    return remember(font) { FontFamily(font) }
}

@Composable
fun jfFlat(): FontFamily {
    val font = Font(
        Res.font.jf_flat_regular,
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

@Composable
fun uthmanTahaNaskh(): FontFamily {
    val font = Font(
        Res.font.uthman_tn_v2,
    )
    return remember(font) { FontFamily(font) }
}

@Composable
fun montserrat(): FontFamily {
    val font = Font(
        Res.font.montserrat_bold,
        weight = FontWeight.Bold,
    )
    return remember(font) { FontFamily(font) }
}

@Composable
fun nassimArabic(): FontFamily {
    val font = Font(
        Res.font.nassim_arabic_regular,
    )
    return remember(font) { FontFamily(font) }
}

@Composable
fun roboto(): FontFamily {
    val light = Font(
        Res.font.roboto_light,
        weight = FontWeight.Light,
    )
    val regular = Font(
        Res.font.roboto_regular,
    )
    val bold = Font(
        Res.font.roboto_bold,
        weight = FontWeight.Bold,
    )
    return remember(light, regular, bold) { FontFamily(light, regular, bold) }
}

@Composable
fun helvetica(): FontFamily {
    val font = Font(
        Res.font.helvetica_world_bold,
        weight = FontWeight.Bold,
    )
    return remember(font) { FontFamily(font) }
}
