package com.thekr.ui.navigation.route

import com.thekr.ui.navigation.NavigationRoute
import kotlinx.serialization.Serializable

object CounterEditRoute : NavigationRoute {
    override val route = "counter_edit"
    override val titleRes = "Counter Edit"
    const val COUNTER_ID_ARG = "itemId"
    val routeWithArgs = "$route/{$COUNTER_ID_ARG}"
}

//@Serializable
//data class CounterEditRoute(
//    val counterId: Long
//) : NavigationRoute(
//    titleRes = "Counter Edit",
//    allowSameDestination = false
//)