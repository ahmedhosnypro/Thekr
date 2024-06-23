package com.thekr.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thekr.data.settings.SettingsDetails
import com.thekr.ui.counter.ZekrScreen
import com.thekr.ui.home.HomeScreen
import com.thekr.ui.navigation.route.CounterEditRoute
import com.thekr.ui.navigation.route.CounterEntryRoute
import com.thekr.ui.navigation.route.HomeRoute
import com.thekr.ui.navigation.route.SettingsRoute
import com.thekr.ui.navigation.route.ZekrScreenRoute
import com.thekr.ui.settings.SettingsScreen
import com.thekr.ui.viewmodel.AzkarState
import com.thekr.ui.zekr.edit.CounterEditScreen
import com.thekr.ui.zekr.entry.CounterEntryScreen

@Composable
fun CounterNavyHost(
    azkarState: AzkarState,
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    // use when navigating up after finishing a process (background process)
    val navigateBack = { navController.popBackStack() }
    // when a user clicks on the back button
    val onNavigateUp = { navController.navigateUp() }

    LaunchedEffect(Unit) {
        NavigationActions.initNavController(navController)
    }

    NavHost(
        navController = navController,
        startDestination = HomeRoute.route,
        modifier = modifier
    ) {
        // home screen
        composable(HomeRoute.route) {
            HomeScreen(
                azkarState = azkarState,
                settingsDetails = settingsDetails,
            )
        }
        // counter entry screen
        composable(CounterEntryRoute.route) {
            CounterEntryScreen(
                settingsDetails = settingsDetails,
            )
        }

        // counter details screen
        composable(
            ZekrScreenRoute.routeWithArgs,
            arguments = listOf(
                navArgument(ZekrScreenRoute.CATEGORY_ID_ARG) {
                    type = NavType.LongType
                },
                navArgument(ZekrScreenRoute.ZEKR_ID_ARG) {
                    type = NavType.LongType
                },
                navArgument(ZekrScreenRoute.INITIAL_PAGE_ARG) {
                    type = NavType.IntType
                },
                navArgument(ZekrScreenRoute.PAGE_COUNT_ARG) {
                    type = NavType.IntType
                },
            )
        ) {
            ZekrScreen(
                settingsDetails = settingsDetails,
            )
        }

        // counter-edit screen
        composable(
            CounterEditRoute.routeWithArgs,
            arguments = listOf(
                navArgument(CounterEditRoute.COUNTER_ID_ARG) {
                    type = NavType.LongType
                },
            )
        ) {
            // todo: use NavigationActions to navigate back
            CounterEditScreen(
                navigateBack = { navigateBack() },
                onNavigateUp = { onNavigateUp() }
            )
        }

        // counter-statistics screen
//        composable(
//            CounterStatisticsDestination.routeWithArgs,
//            arguments = listOf(
//                navArgument(CounterStatisticsDestination.counterIdArg) {
//                    type = NavType.LongType
//                }
//            )) {
//            CounterStatisticsScreen(navigateBack = { navigateBack() })
//        }
//        settings screen
        composable(SettingsRoute.route) {
            SettingsScreen(
                settingsDetails = settingsDetails,
            )
        }
    }
}

//@Composable
//fun CounterNavyHost(
//    azkarState: AzkarState,
//    settingsDetails: SettingsDetails,
//    modifier: Modifier = Modifier,
//) {
//    val navController = rememberNavController()
//    // use when navigating up after finishing a process (background process)
//    val navigateBack = { navController.popBackStack() }
//    // when a user clicks on the back button
//    val onNavigateUp = { navController.navigateUp() }
//
//    LaunchedEffect(Unit) {
//        NavigationActions.initNavController(navController)
//    }
//
//    NavHost(
//        navController = navController,
//        startDestination = HomeRoute,
//        modifier = modifier
//    ) {
//        // home screen
//        composable<HomeRoute> {
//            HomeScreen(
//                azkarState = azkarState,
//                settingsDetails = settingsDetails,
//            )
//        }
//        // counter entry screen
//        composable<CounterEntryRoute> {
//            CounterEntryScreen(
//                settingsDetails = settingsDetails,
//            )
//        }
//
//        // counter details screen
//        composable<ZekrScreenRoute> {
//            ZekrScreen(
//                settingsDetails = settingsDetails,
//            )
//        }
//
//        // counter-edit screen
//        composable<CounterEditRoute> {
//            // todo: use NavigationActions to navigate back
//            CounterEditScreen(
//                navigateBack = { navigateBack() },
//                onNavigateUp = { onNavigateUp() }
//            )
//        }
//
//        // counter-statistics screen
////        composable(
////            CounterStatisticsDestination.routeWithArgs,
////            arguments = listOf(
////                navArgument(CounterStatisticsDestination.counterIdArg) {
////                    type = NavType.LongType
////                }
////            )) {
////            CounterStatisticsScreen(navigateBack = { navigateBack() })
////        }
////        settings screen
//        composable<SettingsRoute> {
//            SettingsScreen(
//                settingsDetails = settingsDetails,
//            )
//        }
//    }
//}