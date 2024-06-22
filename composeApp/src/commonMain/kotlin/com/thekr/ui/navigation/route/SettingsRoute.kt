package com.thekr.ui.navigation.route

import com.thekr.ui.navigation.NavigationRoute
import kotlinx.serialization.Serializable

object SettingsRoute : NavigationRoute {
    override val route = "settings"
    override val titleRes = "Settings"
}

//@Serializable
//object SettingsRoute : NavigationRoute(
//    titleRes = "Settings",
//    allowSameDestination = false,
//)