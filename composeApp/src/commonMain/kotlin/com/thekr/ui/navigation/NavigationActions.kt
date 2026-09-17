package com.thekr.ui.navigation

import androidx.navigation.NavController

object NavigationActions {
    private lateinit var navController: NavController

    fun initNavController(navController: NavController) {
        NavigationActions.navController = navController
    }

    fun navigateUp(route: String) {
        navController.popBackStack(route, false)
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun navigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }
}
