// @Composable functions are PascalCase per the Compose API guidelines (detekt
// exempts them via naming.FunctionNaming ignoreAnnotated; ktlint's
// function-naming rule has no working equivalent in this setup).
@file:Suppress("ktlint:standard:function-naming")

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
import com.thekr.ui.navigation.route.CounterEntryRoute
import com.thekr.ui.navigation.route.HomeRoute
import com.thekr.ui.navigation.route.SettingsRoute
import com.thekr.ui.navigation.route.ThekrScreenRoute
import com.thekr.ui.settings.SettingsScreen
import com.thekr.ui.thekr.entry.CounterEntryScreen
import com.thekr.ui.viewmodel.AppState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CounterNavyHost(
    appState: StateFlow<AppState>,
    settingsDetails: SettingsDetails,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute.route,
        modifier = modifier,
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
            ),
        ) {
            ThekrScreen(
                settingsDetails = settingsDetails,
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
