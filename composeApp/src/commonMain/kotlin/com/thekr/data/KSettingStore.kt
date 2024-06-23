package com.thekr.data

import com.thekr.data.proto.Settings
import io.github.xxfast.kstore.KStore


expect val settingsStore: KStore<Settings>
