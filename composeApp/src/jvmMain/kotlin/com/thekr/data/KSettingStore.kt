package com.thekr.data

import com.thekr.data.proto.Settings
import com.thekr.di.appStorage
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import okio.Path.Companion.toPath

actual val settingsStore: KStore<Settings> by lazy {
    storeOf("$appStorage/settings.json".toPath(), Settings())
}