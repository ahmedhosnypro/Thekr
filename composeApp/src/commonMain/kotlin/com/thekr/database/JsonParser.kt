package com.thekr.database

import com.thekr.database.DatabaseProvider.database
import com.thekr.model.Category
import com.thekr.model.Zekr
import com.thekr.model.ZekrInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.jetbrains.compose.resources.ExperimentalResourceApi
import thekr.composeapp.generated.resources.Res

object JsonParser {
    val json = Json {
        ignoreUnknownKeys = true
        serializersModule = SerializersModule {
            contextual(BooleanAsIntSerializer)
        }
    }

    suspend fun importDataFromJson() {
        withContext(Dispatchers.IO) {
            try {
                val zekrList = readJsonFile<Zekr>("zekr.json")
                val zekrInstanceList = readJsonFile<ZekrInstance>("zekr_instance.json")
                val categoryList = readJsonFile<Category>("category.json")

                insertDataIntoDatabase(zekrList, zekrInstanceList, categoryList)
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error, maybe log or notify user
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private suspend inline fun <reified T> readJsonFile(fileName: String): List<T> {
        val jsonString = Res.readBytes("files/database/json/$fileName.json")
            .decodeToString()

        return json.decodeFromString(jsonString)
    }

    fun insertDataIntoDatabase(
        zekrList: List<Zekr>, zekrInstanceList: List<ZekrInstance>, categoryList: List<Category>
    ) {
        // todo: runIntTransaction not available in multiplatform
//        database.runInTransaction {
        database.zekrDAO().insertAll(zekrList)
        database.zekrInstanceDao().insertAll(zekrInstanceList)
        database.categoryDao().insertAll(categoryList)
//        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = Boolean::class)
    object BooleanAsIntSerializer : KSerializer<Boolean> {
        override fun serialize(encoder: Encoder, value: Boolean) {
            encoder.encodeInt(if (value) 1 else 0)
        }

        override fun deserialize(decoder: Decoder): Boolean {
            return decoder.decodeInt() != 0
        }
    }
}