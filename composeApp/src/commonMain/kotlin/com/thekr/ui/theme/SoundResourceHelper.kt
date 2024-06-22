package com.thekr.ui.theme

import com.thekr.R

object SoundResourceHelper {
    private val fileNameToResourceIdMap = mutableMapOf<String, Int>()

    init {
        initialize()
    }

    fun getResourceIdFromFileName(fileName: String): Int? {
        return fileNameToResourceIdMap[fileName]
    }

    fun getRandomResourceId(): Int{
        val keys = fileNameToResourceIdMap.keys
        val randomIndex = (0 until keys.size).random()
        return fileNameToResourceIdMap[keys.elementAt(randomIndex)]!!
    }

    private fun initialize() {
        //allah_bless_and_grant_peace_to_muhammad_and_his_family.mp3
        //ask_forgiveness.mp3
        //click_1.mp3
        //click_2.mp3
        //glory_be_to_allah_praise_be_to_allah_there_is_no_god_but_allah_and_allah_is_great.mp3
        //hallel.mp3
        //huqqal.mp3
        //i_seek_forgiveness_from_god_besides_whom_there_is_no_god_the_ever_living_the_ever_subsisting_and_i_repent_to_him.mp3
        //kabber.mp3
        //peace_be_upon_him.mp3
        //ping.mp3
        //praise_due_to_allah_the_almighty.mp3
        //praise.mp3
        //there_is_no_god_but_god_alone_with_no_partner_to_him_belongs_sovereignty_and_to_him_is_praise_and_he_is_capable_of_all_things.mp3
        //there_is_no_power_or_strength_but_with_god.mp3
        fileNameToResourceIdMap["allah_bless_and_grant_peace_to_muhammad_and_his_family.mp3"] =
            R.raw.allah_bless_and_grant_peace_to_muhammad_and_his_family
        fileNameToResourceIdMap["ask_forgiveness.mp3"] = R.raw.ask_forgiveness
        fileNameToResourceIdMap["click_1.mp3"] = R.raw.click_1
        fileNameToResourceIdMap["click_2.mp3"] = R.raw.click_2
        fileNameToResourceIdMap["glory_be_to_allah_praise_be_to_allah_there_is_no_god_but_allah_and_allah_is_great.mp3"] =
            R.raw.glory_be_to_allah_praise_be_to_allah_there_is_no_god_but_allah_and_allah_is_great
        fileNameToResourceIdMap["hallel.mp3"] = R.raw.hallel
        fileNameToResourceIdMap["huqqal.mp3"] = R.raw.huqqal
        fileNameToResourceIdMap["i_seek_forgiveness_from_god_besides_whom_there_is_no_god_the_ever_living_the_ever_subsisting_and_i_repent_to_him.mp3"] =
            R.raw.i_seek_forgiveness_from_god_besides_whom_there_is_no_god_the_ever_living_the_ever_subsisting_and_i_repent_to_him
        fileNameToResourceIdMap["kabber.mp3"] = R.raw.kabber
        fileNameToResourceIdMap["peace_be_upon_him.mp3"] = R.raw.peace_be_upon_him
        fileNameToResourceIdMap["ping.mp3"] = R.raw.ping
        fileNameToResourceIdMap["praise_due_to_allah_the_almighty.mp3"] =
            R.raw.praise_due_to_allah_the_almighty
        fileNameToResourceIdMap["praise.mp3"] = R.raw.praise
        fileNameToResourceIdMap["there_is_no_god_but_god_alone_with_no_partner_to_him_belongs_sovereignty_and_to_him_is_praise_and_he_is_capable_of_all_things.mp3"] =
            R.raw.there_is_no_god_but_god_alone_with_no_partner_to_him_belongs_sovereignty_and_to_him_is_praise_and_he_is_capable_of_all_things
        fileNameToResourceIdMap["there_is_no_power_or_strength_but_with_god.mp3"] =
            R.raw.there_is_no_power_or_strength_but_with_god
    }
}