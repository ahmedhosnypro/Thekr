package com.thekr.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.thekr.data.settings.SettingsDetails
import com.thekr.ui.counter.ThekrScreen
import com.thekr.ui.home.HomeScreen
import com.thekr.ui.navigation.route.CounterEditRoute
import com.thekr.ui.navigation.route.CounterEntryRoute
import com.thekr.ui.navigation.route.HomeRoute
import com.thekr.ui.navigation.route.SettingsRoute
import com.thekr.ui.navigation.route.ThekrScreenRoute
import com.thekr.ui.settings.SettingsScreen
import com.thekr.ui.viewmodel.AppState
import com.thekr.ui.thekr.edit.CounterEditScreen
import com.thekr.ui.thekr.entry.CounterEntryScreen

@Composable
fun CounterNavyHost(
    appState: AppState,
    settingsDetails: SettingsDetails,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    // use when navigating up after finishing a process (background process)
    val navigateBack = { navController.popBackStack() }
    // when a user clicks on the back button
    val onNavigateUp = { navController.navigateUp() }


    NavHost(
        navController = navController,
        startDestination = HomeRoute.route,
        modifier = modifier
    ) {
        // home screen
        composable(HomeRoute.route) {
            HomeScreen(
                appState = appState,
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
            ThekrScreenRoute.routeWithArgs,
            arguments = listOf(
                navArgument(ThekrScreenRoute.CATEGORY_ID_ARG) {
                    type = NavType.LongType
                },
                navArgument(ThekrScreenRoute.ZEKR_ID_ARG) {
                    type = NavType.LongType
                },
                navArgument(ThekrScreenRoute.INITIAL_PAGE_ARG) {
                    type = NavType.IntType
                },
                navArgument(ThekrScreenRoute.PAGE_COUNT_ARG) {
                    type = NavType.IntType
                },
            )
        ) {
            ThekrScreen(
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
//        composable<ThekrScreenRoute> {
//            ThekrScreen(
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