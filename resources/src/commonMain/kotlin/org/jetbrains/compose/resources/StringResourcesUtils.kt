package org.jetbrains.compose.resources

import io.github.aakira.napier.Napier
import io.github.aakira.napier.Napier.d
import org.jetbrains.compose.resources.plural.PluralCategory
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

private val SimpleStringFormatRegex = Regex("""%(\d)\$[ds]""")
internal fun String.replaceWithArgs(args: List<String>) = SimpleStringFormatRegex.replace(this) { matchResult ->
    args[matchResult.groupValues[1].toInt() - 1]
}

internal sealed interface StringItem {
    data class Value(val text: String) : StringItem
    data class Plurals(val items: Map<PluralCategory, String>) : StringItem
    data class Array(val items: List<String>) : StringItem
}

private val stringItemsCache = AsyncCache<String, StringItem>()

//@TestOnly
internal fun dropStringItemsCache() {
    stringItemsCache.clear()
}

internal suspend fun getStringItem(
    resourceItem: ResourceItem,
    resourceReader: ResourceReader
): StringItem = stringItemsCache.getOrLoad(
    key = "${resourceItem.path}/${resourceItem.offset}-${resourceItem.size}"
) {
    val recordByteArray = resourceReader.readPart(
        resourceItem.path,
        resourceItem.offset,
        resourceItem.size
    )
    // read as sting in utf-8
    Napier.d(tag = "getStringItem") { ("recordByteArray: $recordByteArray") }
    val record = recordByteArray.decodeToString()
    Napier.d(tag = "getStringItem") { ("record: $record") }
    val recordItems = record.split('|')
    Napier.d(tag = "getStringItem") { ("recordItems: $recordItems") }
    val recordType = recordItems.first()
    Napier.d(tag = "getStringItem") { ("recordType: $recordType") }
    val recordData = recordItems.last()
    Napier.d(tag = "getStringItem") { ("recordData: $recordData") }
    when (recordType) {
        "plurals" -> {
            val plurals = recordData.decodeAsPlural()
            Napier.d(tag = "getStringItem") { ("plurals: $plurals") }
            plurals
        }

        "string-array" -> {
            val array = recordData.decodeAsArray()
            Napier.d(tag = "getStringItem") { ("array: $array") }
            array
        }

        else -> {
            val value = recordData.decodeAsString()
            Napier.d(tag = "getStringItem") { ("value: $value") }
            value
        }
    }
}

@OptIn(ExperimentalEncodingApi::class)
private fun String.decodeAsString(): StringItem.Value = StringItem.Value(
    Base64.decode(this).decodeToString()
)

@OptIn(ExperimentalEncodingApi::class)
private fun String.decodeAsArray(): StringItem.Array = StringItem.Array(
    split(",").map { item ->
        Base64.decode(item).decodeToString()
    }
)

@OptIn(ExperimentalEncodingApi::class)
private fun String.decodeAsPlural(): StringItem.Plurals = StringItem.Plurals(
    split(",").associate { item ->
        val category = item.substringBefore(':')
        val valueBase64 = item.substringAfter(':')
        PluralCategory.fromString(category)!! to Base64.decode(valueBase64).decodeToString()
    }
)
