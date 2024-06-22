package com.thekr.ui.navigation

import androidx.navigation.NavController

object NavigationActions {
    private lateinit var navController: NavController

    fun initNavController(navController: NavController) {
        if (this::navController.isInitialized.not()) {
            this.navController = navController
        }
    }

    fun navigateUp(rout: String) {
        navController.popBackStack(rout, false)
    }

    fun navigate(route: String) {
        navController.navigate(route)

    }
}

//object NavigationActions {
//    @SuppressLint("StaticFieldLeak")
//    private lateinit var navController: NavController
//
//    fun initNavController(navController: NavController) {
//        if (this::navController.isInitialized.not()) {
//            this.navController = navController
//        }
//    }
//
//
//    // todo: use coming stable version of navigation when released
//    fun navigateUp(route: KClass<out NavigationRoute>? = null) {
//        if (route != null) {
//            route.qualifiedName?.let { routeName ->
//                Log.d("NavigationActions", "Navigating up to: ${route.qualifiedName}")
//                navController.popBackStack(
//                    route = routeName,
//                    inclusive = false,
//                    saveState = false
//                )
//            } ?: Log.d("NavigationActions", "Route name is null")
//        } else {
//            Log.d("NavigationActions", "Navigating up")
//            navController.navigateUp()
//        }
//    }
//
//    fun navigate(route: NavigationRoute) {
//        if (route.allowSameDestination) {
//            navController.navigate(route)
//            Log.d("NavigationActions", "Navigating to: ${route::class.qualifiedName}")
//        } else {
//            val currentRoute = navController.currentDestination?.route
//            val destinationRoute = route::class.qualifiedName
//            if (currentRoute != destinationRoute) {
//                navController.navigate(route)
//                Log.d("NavigationActions", "Navigating to: $destinationRoute")
//            } else {
//                Log.d("NavigationActions", "Already at route: $currentRoute")
//            }
//        }
//    }
//}