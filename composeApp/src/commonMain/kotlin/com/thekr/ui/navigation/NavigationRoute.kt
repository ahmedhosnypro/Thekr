package com.thekr.ui.navigation

interface NavigationRoute {
    /** Unique name to define the path for a composable */
    val route: String

    /**
     * String resource id to that contains title to be displayed for the
     * screen.
     */
    val titleRes: String
}
