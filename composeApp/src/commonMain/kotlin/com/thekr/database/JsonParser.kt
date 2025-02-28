package com.thekr.database

import com.thekr.data.settingsStore
import com.thekr.di.DatabaseProvider.database
import com.thekr.model.Category
import com.thekr.model.Thekr
import com.thekr.model.ThekrInstance
import com.thekr.resources.Res
import com.thekr.util.TimeHelper.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi


object JsonParser {
    val j = Json
    private val json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun importDataFromJson() {
        withContext(Dispatchers.IO) {
            try {
                val thekrList = readJsonFile<Thekr>("thekr.json")
                val thekrInstanceList = readJsonFile<ThekrInstance>("thekr_instance.json")
                val categoryList = readJsonFile<Category>("category.json")

                insertDataIntoDatabase(thekrList, thekrInstanceList, categoryList)
                settingsStore.update {
                    it?.copy(
                        dbInitialized = true,
                        lastUpdate = now()
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error, maybe log or notify user
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private suspend inline fun <reified T> readJsonFile(fileName: String): List<T> {
        val jsonString = Res.readBytes("files/database/json/$fileName")
            .decodeToString()

        return json.decodeFromString(jsonString)
    }

    private suspend fun insertDataIntoDatabase(
        thekrList: List<Thekr>, thekrInstanceList: List<ThekrInstance>, categoryList: List<Category>
    ) {
        withContext(Dispatchers.IO) {
            database.thekrDAO().insertAll(thekrList)
            database.thekrInstanceDao().insertAll(thekrInstanceList)
            database.categoryDao().insertAll(categoryList)
        }
    }
}