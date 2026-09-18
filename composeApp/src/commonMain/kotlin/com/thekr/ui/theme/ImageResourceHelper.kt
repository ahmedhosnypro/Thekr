package com.thekr.ui.theme

import com.thekr.resources.Res
import com.thekr.resources.a_minus
import com.thekr.resources.a_plus
import com.thekr.resources.azkar_clothes
import com.thekr.resources.azkar_eating
import com.thekr.resources.azkar_family
import com.thekr.resources.azkar_fear
import com.thekr.resources.azkar_good
import com.thekr.resources.azkar_hej
import com.thekr.resources.azkar_home
import com.thekr.resources.azkar_khalaa
import com.thekr.resources.azkar_life
import com.thekr.resources.azkar_list_icon
import com.thekr.resources.azkar_magic
import com.thekr.resources.azkar_masaa
import com.thekr.resources.azkar_masjed
import com.thekr.resources.azkar_money
import com.thekr.resources.azkar_more
import com.thekr.resources.azkar_rasol
import com.thekr.resources.azkar_roqua
import com.thekr.resources.azkar_sabah
import com.thekr.resources.azkar_salah
import com.thekr.resources.azkar_siaam
import com.thekr.resources.azkar_sick
import com.thekr.resources.azkar_sleep
import com.thekr.resources.azkar_social
import com.thekr.resources.azkar_tahara
import com.thekr.resources.azkar_tasbeeh
import com.thekr.resources.azkar_tathaob
import com.thekr.resources.azkar_tawba
import com.thekr.resources.azkar_travel
import com.thekr.resources.azkar_world
import com.thekr.resources.day_mode
import com.thekr.resources.doaa_active
import com.thekr.resources.doaa_quran
import com.thekr.resources.doaa_sunna
import com.thekr.resources.hesn_active
import com.thekr.resources.icon
import com.thekr.resources.info_icon
import com.thekr.resources.next_thekr
import com.thekr.resources.night_mode
import com.thekr.resources.pause_sound
import com.thekr.resources.play_sound
import com.thekr.resources.search_icon
import com.thekr.resources.sound_azkar_active
import com.thekr.resources.sound_azkar_inactive
import com.thekr.resources.thekr_indicator
import org.jetbrains.compose.resources.DrawableResource

object ImageResourceHelper {

    private val fileNameToResourceIdMap: Map<String, DrawableResource> = mapOf(
        "icon.png" to Res.drawable.icon,
        "a_minus.png" to Res.drawable.a_minus,
        "a_plus.png" to Res.drawable.a_plus,
        "azkar_clothes.png" to Res.drawable.azkar_clothes,
        "azkar_eating.png" to Res.drawable.azkar_eating,
        "azkar_family.png" to Res.drawable.azkar_family,
        "azkar_fear.png" to Res.drawable.azkar_fear,
        "azkar_good.png" to Res.drawable.azkar_good,
        "azkar_hej.png" to Res.drawable.azkar_hej,
        "azkar_home.png" to Res.drawable.azkar_home,
        "azkar_khalaa.png" to Res.drawable.azkar_khalaa,
        "azkar_life.png" to Res.drawable.azkar_life,
        "azkar_list_icon.png" to Res.drawable.azkar_list_icon,
        "azkar_magic.png" to Res.drawable.azkar_magic,
        "azkar_masaa.png" to Res.drawable.azkar_masaa,
        "azkar_masjed.png" to Res.drawable.azkar_masjed,
        "azkar_money.png" to Res.drawable.azkar_money,
        "azkar_more.png" to Res.drawable.azkar_more,
        "azkar_rasol.png" to Res.drawable.azkar_rasol,
        "azkar_roqua.png" to Res.drawable.azkar_roqua,
        "azkar_sabah.png" to Res.drawable.azkar_sabah,
        "azkar_salah.png" to Res.drawable.azkar_salah,
        "azkar_siaam.png" to Res.drawable.azkar_siaam,
        "azkar_sick.png" to Res.drawable.azkar_sick,
        "azkar_sleep.png" to Res.drawable.azkar_sleep,
        "azkar_social.png" to Res.drawable.azkar_social,
        "azkar_tahara.png" to Res.drawable.azkar_tahara,
        "azkar_tasbeeh.png" to Res.drawable.azkar_tasbeeh,
        "azkar_tathaob.png" to Res.drawable.azkar_tathaob,
        "azkar_tawba.png" to Res.drawable.azkar_tawba,
        "azkar_travel.png" to Res.drawable.azkar_travel,
        "azkar_world.png" to Res.drawable.azkar_world,
        "day_mode.png" to Res.drawable.day_mode,
        "doaa_active.png" to Res.drawable.doaa_active,
        "doaa_quran.png" to Res.drawable.doaa_quran,
        "doaa_sunna.png" to Res.drawable.doaa_sunna,
        "hesn_active.png" to Res.drawable.hesn_active,
        "info_icon.png" to Res.drawable.info_icon,
        "next_thekr.png" to Res.drawable.next_thekr,
        "night_mode.png" to Res.drawable.night_mode,
        "pause_sound.png" to Res.drawable.pause_sound,
        "play_sound.png" to Res.drawable.play_sound,
        "search_icon.png" to Res.drawable.search_icon,
        "sound_azkar_active.png" to Res.drawable.sound_azkar_active,
        "sound_azkar_inactive.png" to Res.drawable.sound_azkar_inactive,
        "thekr_indicatorW.png" to Res.drawable.thekr_indicator,
    )

    fun getDrawableResourceIdFromFileName(fileName: String): DrawableResource? = fileNameToResourceIdMap[fileName]
}
