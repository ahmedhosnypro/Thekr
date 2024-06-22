package com.thekr.ui.theme

import com.thekr.R

object ImageResourceHelper {

    private val fileNameToResourceIdMap = mutableMapOf<String, Int>()

    init {
        initialize()
    }
    private fun initialize() {
        fileNameToResourceIdMap["icon.png"] = R.drawable.icon
        fileNameToResourceIdMap["a_minus.png"] = R.drawable.a_minus
        fileNameToResourceIdMap["a_plus.png"] = R.drawable.a_plus
        fileNameToResourceIdMap["azkar_clothes.png"] = R.drawable.azkar_clothes
        fileNameToResourceIdMap["azkar_eating.png"] = R.drawable.azkar_eating
        fileNameToResourceIdMap["azkar_family.png"] = R.drawable.azkar_family
        fileNameToResourceIdMap["azkar_fear.png"] = R.drawable.azkar_fear
        fileNameToResourceIdMap["azkar_good.png"] = R.drawable.azkar_good
        fileNameToResourceIdMap["azkar_hej.png"] = R.drawable.azkar_hej
        fileNameToResourceIdMap["azkar_home.png"] = R.drawable.azkar_home
        fileNameToResourceIdMap["azkar_khalaa.png"] = R.drawable.azkar_khalaa
        fileNameToResourceIdMap["azkar_life.png"] = R.drawable.azkar_life
        fileNameToResourceIdMap["azkar_list_icon.png"] = R.drawable.azkar_list_icon
        fileNameToResourceIdMap["azkar_magic.png"] = R.drawable.azkar_magic
        fileNameToResourceIdMap["azkar_masaa.png"] = R.drawable.azkar_masaa
        fileNameToResourceIdMap["azkar_masjed.png"] = R.drawable.azkar_masjed
        fileNameToResourceIdMap["azkar_money.png"] = R.drawable.azkar_money
        fileNameToResourceIdMap["azkar_more.png"] = R.drawable.azkar_more
        fileNameToResourceIdMap["azkar_rasol.png"] = R.drawable.azkar_rasol
        fileNameToResourceIdMap["azkar_roqua.png"] = R.drawable.azkar_roqua
        fileNameToResourceIdMap["azkar_sabah.png"] = R.drawable.azkar_sabah
        fileNameToResourceIdMap["azkar_salah.png"] = R.drawable.azkar_salah
        fileNameToResourceIdMap["azkar_siaam.png"] = R.drawable.azkar_siaam
        fileNameToResourceIdMap["azkar_sick.png"] = R.drawable.azkar_sick
        fileNameToResourceIdMap["azkar_sleep.png"] = R.drawable.azkar_sleep
        fileNameToResourceIdMap["azkar_social.png"] = R.drawable.azkar_social
        fileNameToResourceIdMap["azkar_tahara.png"] = R.drawable.azkar_tahara
        fileNameToResourceIdMap["azkar_tasbeeh.png"] = R.drawable.azkar_tasbeeh
        fileNameToResourceIdMap["azkar_tathaob.png"] = R.drawable.azkar_tathaob
        fileNameToResourceIdMap["azkar_tawba.png"] = R.drawable.azkar_tawba
        fileNameToResourceIdMap["azkar_travel.png"] = R.drawable.azkar_travel
        fileNameToResourceIdMap["azkar_world.png"] = R.drawable.azkar_world
        fileNameToResourceIdMap["day_mode.png"] = R.drawable.day_mode
        fileNameToResourceIdMap["doaa_active.png"] = R.drawable.doaa_active
        fileNameToResourceIdMap["doaa_quran.png"] = R.drawable.doaa_quran
        fileNameToResourceIdMap["doaa_sunna.png"] = R.drawable.doaa_sunna
        fileNameToResourceIdMap["hesn_active.png"] = R.drawable.hesn_active
        fileNameToResourceIdMap["info_icon.png"] = R.drawable.info_icon
        fileNameToResourceIdMap["next_zekr.png"] = R.drawable.next_zekr
        fileNameToResourceIdMap["night_mode.png"] = R.drawable.night_mode
        fileNameToResourceIdMap["pause_sound.png"] = R.drawable.pause_sound
        fileNameToResourceIdMap["play_sound.png"] = R.drawable.play_sound
        fileNameToResourceIdMap["search_icon.png"] = R.drawable.search_icon
        fileNameToResourceIdMap["sound_azkar_active.png"] = R.drawable.sound_azkar_active
        fileNameToResourceIdMap["sound_azkar_inactive.png"] = R.drawable.sound_azkar_inactive
        fileNameToResourceIdMap["zekr_indicatorW.png"] = R.drawable.zekr_indicator
    }


    fun getResourceIdFromFileName(fileName: String): Int? {
        return fileNameToResourceIdMap[fileName]
    }
}
