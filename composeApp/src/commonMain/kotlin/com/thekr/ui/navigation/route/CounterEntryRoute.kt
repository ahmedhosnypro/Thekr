package com.thekr.ui.navigation.route

import com.thekr.ui.navigation.NavigationRoute

object CounterEntryRoute : NavigationRoute {
    override val route = "counter_entry"
    override val titleRes = "Create a Counter"
}

// @Serializable
// object CounterEntryRoute : NavigationRoute(
//    titleRes = "Create a Counter",
//    allowSameDestination = false
// )
