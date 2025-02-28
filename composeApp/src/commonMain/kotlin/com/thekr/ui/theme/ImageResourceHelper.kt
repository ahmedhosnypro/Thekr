package com.thekr.ui.theme

import org.jetbrains.compose.resources.DrawableResource
import com.thekr.resources.*

object ImageResourceHelper {

    private val fileNameToResourceIdMap = mutableMapOf<String, DrawableResource>()

    init {
        initialize()
    }
    private fun initialize() {
        fileNameToResourceIdMap["icon.png"] = Res.drawable.icon
        fileNameToResourceIdMap["a_minus.png"] = Res.drawable.a_minus
        fileNameToResourceIdMap["a_plus.png"] = Res.drawable.a_plus
        fileNameToResourceIdMap["azkar_clothes.png"] = Res.drawable.azkar_clothes
        fileNameToResourceIdMap["azkar_eating.png"] = Res.drawable.azkar_eating
        fileNameToResourceIdMap["azkar_family.png"] = Res.drawable.azkar_family
        fileNameToResourceIdMap["azkar_fear.png"] = Res.drawable.azkar_fear
        fileNameToResourceIdMap["azkar_good.png"] = Res.drawable.azkar_good
        fileNameToResourceIdMap["azkar_hej.png"] = Res.drawable.azkar_hej
        fileNameToResourceIdMap["azkar_home.png"] = Res.drawable.azkar_home
        fileNameToResourceIdMap["azkar_khalaa.png"] = Res.drawable.azkar_khalaa
        fileNameToResourceIdMap["azkar_life.png"] = Res.drawable.azkar_life
        fileNameToResourceIdMap["azkar_list_icon.png"] = Res.drawable.azkar_list_icon
        fileNameToResourceIdMap["azkar_magic.png"] = Res.drawable.azkar_magic
        fileNameToResourceIdMap["azkar_masaa.png"] = Res.drawable.azkar_masaa
        fileNameToResourceIdMap["azkar_masjed.png"] = Res.drawable.azkar_masjed
        fileNameToResourceIdMap["azkar_money.png"] = Res.drawable.azkar_money
        fileNameToResourceIdMap["azkar_more.png"] = Res.drawable.azkar_more
        fileNameToResourceIdMap["azkar_rasol.png"] = Res.drawable.azkar_rasol
        fileNameToResourceIdMap["azkar_roqua.png"] = Res.drawable.azkar_roqua
        fileNameToResourceIdMap["azkar_sabah.png"] = Res.drawable.azkar_sabah
        fileNameToResourceIdMap["azkar_salah.png"] = Res.drawable.azkar_salah
        fileNameToResourceIdMap["azkar_siaam.png"] = Res.drawable.azkar_siaam
        fileNameToResourceIdMap["azkar_sick.png"] = Res.drawable.azkar_sick
        fileNameToResourceIdMap["azkar_sleep.png"] = Res.drawable.azkar_sleep
        fileNameToResourceIdMap["azkar_social.png"] = Res.drawable.azkar_social
        fileNameToResourceIdMap["azkar_tahara.png"] = Res.drawable.azkar_tahara
        fileNameToResourceIdMap["azkar_tasbeeh.png"] = Res.drawable.azkar_tasbeeh
        fileNameToResourceIdMap["azkar_tathaob.png"] = Res.drawable.azkar_tathaob
        fileNameToResourceIdMap["azkar_tawba.png"] = Res.drawable.azkar_tawba
        fileNameToResourceIdMap["azkar_travel.png"] = Res.drawable.azkar_travel
        fileNameToResourceIdMap["azkar_world.png"] = Res.drawable.azkar_world
        fileNameToResourceIdMap["day_mode.png"] = Res.drawable.day_mode
        fileNameToResourceIdMap["doaa_active.png"] = Res.drawable.doaa_active
        fileNameToResourceIdMap["doaa_quran.png"] = Res.drawable.doaa_quran
        fileNameToResourceIdMap["doaa_sunna.png"] = Res.drawable.doaa_sunna
        fileNameToResourceIdMap["hesn_active.png"] = Res.drawable.hesn_active
        fileNameToResourceIdMap["info_icon.png"] = Res.drawable.info_icon
        fileNameToResourceIdMap["next_thekr.png"] = Res.drawable.next_thekr
        fileNameToResourceIdMap["night_mode.png"] = Res.drawable.night_mode
        fileNameToResourceIdMap["pause_sound.png"] = Res.drawable.pause_sound
        fileNameToResourceIdMap["play_sound.png"] = Res.drawable.play_sound
        fileNameToResourceIdMap["search_icon.png"] = Res.drawable.search_icon
        fileNameToResourceIdMap["sound_azkar_active.png"] = Res.drawable.sound_azkar_active
        fileNameToResourceIdMap["sound_azkar_inactive.png"] = Res.drawable.sound_azkar_inactive
        fileNameToResourceIdMap["thekr_indicatorW.png"] = Res.drawable.thekr_indicator
    }


    fun getDrawableResourceIdFromFileName(fileName: String): DrawableResource? {
        return fileNameToResourceIdMap[fileName]
    }
}
