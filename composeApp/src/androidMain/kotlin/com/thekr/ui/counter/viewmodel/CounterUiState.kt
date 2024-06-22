package com.thekr.ui.counter.viewmodel

import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel

data class CountStatistics(
    val currentModel: CartesianLayerModel.Partial = LineCartesianLayerModel.Partial(series = listOf()),
    val maxY: Float? = null,
    val minY: Float? = null,
)