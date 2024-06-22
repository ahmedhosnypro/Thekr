package com.thekr.ui.navigation

import kotlinx.serialization.Serializable

interface NavigationRoute {
    /** Unique name to define the path for a composable */
    val route: String

    /**
     * String resource id to that contains title to be displayed for the
     * screen.
     */
    val titleRes: String
}

///** Interface to describe the navigation destinations for the app */
//@Serializable
//abstract class NavigationRoute(
//    /**
//     * String resource id to that contains title to be displayed for the
//     * screen.
//     */
//    val titleRes: String,
//    val allowSameDestination: Boolean,
//)