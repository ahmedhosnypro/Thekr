package com.thekr.ui.navigation.route

import com.thekr.ui.navigation.NavigationRoute
import kotlinx.serialization.Serializable


object HomeRoute : NavigationRoute {
    override val route = "home"
    override val titleRes = "Counter"
}


//@Serializable
//object HomeRoute : NavigationRoute(
//    titleRes = "Home",
//    allowSameDestination = false,
//)