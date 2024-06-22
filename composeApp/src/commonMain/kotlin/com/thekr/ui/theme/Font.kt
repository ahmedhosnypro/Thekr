package com.thekr.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import thekr.composeapp.generated.resources.Res
import thekr.composeapp.generated.resources.abdo_master_demi_bold
import thekr.composeapp.generated.resources.abdo_master_regular
import thekr.composeapp.generated.resources.alrafidain_art
import thekr.composeapp.generated.resources.arial_bold
import thekr.composeapp.generated.resources.arial_narrow
import thekr.composeapp.generated.resources.arial_regular
import thekr.composeapp.generated.resources.battar_font
import thekr.composeapp.generated.resources.droid_kufi
import thekr.composeapp.generated.resources.droid_kufi_bold
import thekr.composeapp.generated.resources.ge_ss_two_bold
import thekr.composeapp.generated.resources.ge_ss_two_light
import thekr.composeapp.generated.resources.ge_ss_two_medium
import thekr.composeapp.generated.resources.hacen_liner_print_out
import thekr.composeapp.generated.resources.hacen_liner_print_out_light
import thekr.composeapp.generated.resources.hacen_tunisia_lt
import thekr.composeapp.generated.resources.helvetica_world_bold
import thekr.composeapp.generated.resources.jf_flat_regular
import thekr.composeapp.generated.resources.kfgqpchafs_uthmanic_script_regula_svg
import thekr.composeapp.generated.resources.montserrat_bold
import thekr.composeapp.generated.resources.nassim_arabic_regular
import thekr.composeapp.generated.resources.roboto_bold
import thekr.composeapp.generated.resources.roboto_light
import thekr.composeapp.generated.resources.roboto_regular

@Composable
fun abdoMaster() =  FontFamily(
    Font(
        Res.font.abdo_master_demi_bold,
        weight = FontWeight.Bold,
    ),
    Font(
        Res.font.abdo_master_regular,
    )
)


@Composable
fun alrafidainArt () = FontFamily(
    Font(
        Res.font.alrafidain_art,
    ),
)

@Composable
fun arial () = FontFamily(
    Font(
        Res.font.arial_regular,
    ),
    Font(
        Res.font.arial_bold,
        weight = FontWeight.Bold
    ),
    Font(
        Res.font.arial_narrow,
    ),
)

@Composable
fun battar () = FontFamily(
    Font(
        Res.font.battar_font,
    ),
)

@Composable
fun droidKufi () = FontFamily(
    Font(
        Res.font.droid_kufi,
    ),
    Font(
        Res.font.droid_kufi_bold,
        weight = FontWeight.Bold
    ),
)


@Composable
fun geSsTwo() = FontFamily(
    Font(
        Res.font.ge_ss_two_light,
        weight = FontWeight.Light
    ),
    Font(
        Res.font.ge_ss_two_medium,
        weight = FontWeight.Medium
    ),
    Font(
        Res.font.ge_ss_two_bold,
        weight = FontWeight.Bold
    )
)

@Composable
fun hacenLinerPrintOut () = FontFamily(
    Font(
        Res.font.hacen_liner_print_out_light,
        weight = FontWeight.Light
    ),
    Font(
        Res.font.hacen_liner_print_out,
    ),
)

@Composable
fun hacenTunisia () = FontFamily(
    Font(
        Res.font.hacen_tunisia_lt
    )
)

@Composable
fun hacenTunisiaLt () = FontFamily(
    Font(
        Res.font.hacen_tunisia_lt,
    )
)

@Composable
fun helveticaWorld () = FontFamily(
    Font(
        Res.font.helvetica_world_bold,
        weight = FontWeight.Bold
    ),
)

@Composable
fun jfFlat () = FontFamily(
    Font(
        Res.font.jf_flat_regular
    ),
)

@Composable
fun uthmanicScript () = FontFamily(
    Font(
        Res.font.kfgqpchafs_uthmanic_script_regula_svg
    )
)

@Composable
fun montserrat () = FontFamily(
    Font(
        Res.font.montserrat_bold,
        weight = FontWeight.Bold
    ),
)


@Composable
fun nassimArabic () = FontFamily(
    Font(
        Res.font.nassim_arabic_regular
    )
)

@Composable
fun roboto () = FontFamily(
    Font(
        Res.font.roboto_light,
        weight = FontWeight.Light
    ),
    Font(
        Res.font.roboto_regular
    ),
    Font(
        Res.font.roboto_bold,
        weight = FontWeight.Bold
    ),
)

@Composable
fun helvetica () = FontFamily(
    Font(
        Res.font.helvetica_world_bold,
        weight = FontWeight.Bold
    )
)




